/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.marketplace;

import craterdog.accounting.Accounting;
import craterdog.accounting.DigitalTransaction;
import craterdog.accounting.V1AccountingProvider;
import craterdog.identities.DigitalIdentity;
import craterdog.identities.Identification;
import craterdog.identities.V1IdentificationProvider;
import craterdog.notary.DocumentCitation;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.V1NotarizationProvider;
import craterdog.notary.ValidationException;
import craterdog.tokens.DigitalToken;
import craterdog.tokens.Tokenization;
import craterdog.tokens.V1TokenizationProvider;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 *
 * @author Derk Norton
 */
public final class TokenManagementService implements TokenManagement {

    static private final XLogger logger = XLoggerFactory.getXLogger(TokenManagementService.class);

    private final Notarization notarizationProvider;
    private final Identification identificationProvider;
    private final Tokenization tokenizationProvider;
    private final Accounting accountingProvider;
    private final TokenManagementStorage storageProvider;
    private final IdentityManagement identityManager;
    private final NotaryKey notaryKey;

    public final URI accountantLocation;
    public final String name;


    public TokenManagementService(IdentityManagement identityManager, NotaryKey notaryKey, String name) {
        logger.entry();
        this.notarizationProvider = new V1NotarizationProvider();
        this.identificationProvider = new V1IdentificationProvider();
        this.tokenizationProvider = new V1TokenizationProvider();
        this.accountingProvider = new V1AccountingProvider();
        this.storageProvider = new TokenManagementStorage();
        this.identityManager = identityManager;
        this.name = name;
        this.accountantLocation = notaryKey.verificationCertificate.attributes.identityLocation;
        this.notaryKey = notaryKey;

        // register myself with the identity management service
        RegisterIdentityRequest request = new RegisterIdentityRequest();
        Map<String, Object> additionalAttributes = new LinkedHashMap<>();
        additionalAttributes.put("pseudonym", name);
        DigitalIdentity identity = identificationProvider.createIdentity(additionalAttributes, notaryKey);
        request.identity = identity;
        request.certificate = notaryKey.verificationCertificate;
        RegisterIdentityResponse response = identityManager.registerIdentity(request);
        logger.exit(response);
    }


    @Override
    public CertifyBatchResponse certifyBatch(CertifyBatchRequest request) {
        logger.entry(request);
        CertifyBatchResponse response = new CertifyBatchResponse();

        try {

            logger.debug("Validating the request...");
            Map<String, Object> errors = new LinkedHashMap<>();
            URI batchLocation = request.batchLocation;
            if (storageProvider.batchExists(batchLocation)) {
                errors.put("batch.already.exists", batchLocation);
            }
            List<DigitalToken> tokens = request.tokens;
            for (DigitalToken token : tokens) {
                URI tokenLocation = token.attributes.myLocation;
                if (storageProvider.tokenExists(tokenLocation)) {
                    errors.put("token.already.exists", tokenLocation);
                }
            }
            if (!errors.isEmpty()) {
                logger.error("The request is invalid...");
                response.status = RequestStatus.Failed;
                response.reason = "Conflict";
                response.putAll(errors);

            } else {
                logger.debug("Certifying the tokens...");
                List<URI> tokenLocations = new ArrayList<>();
                for (DigitalToken token : tokens) {
                    DocumentCitation guarantorCitation = token.guarantorSeal.attributes.verificationCitation;
                    URI certificateLocation = guarantorCitation.documentLocation;
                    RetrieveCertificateResponse retrieveResponse = identityManager.retrieveCertificate(certificateLocation);
                    NotaryCertificate guarantorCertificate = retrieveResponse.certificate;
                    if (guarantorCertificate == null) {
                        errors.put("guarantor.certificate.not.found", certificateLocation);
                    }
                    tokenizationProvider.certifyToken(token, batchLocation, guarantorCertificate, notaryKey);
                    tokenLocations.add(token.attributes.myLocation);
                }

                logger.debug("Saving the certified tokens in the datastore...");
                storageProvider.createTokens(batchLocation, tokens);

                logger.debug("The certification request was successful...");
                response.status = RequestStatus.Succeeded;
                response.reason = "Created";
                response.batchLocation = batchLocation;
                response.tokenLocations = tokenLocations;
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while certifying a batch of tokens: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while certifying a batch of tokens:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(request, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while certifying a batch of tokens:\n{}", (Object) e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveTokenResponse retrieveToken(URI tokenLocation) {
        logger.entry(tokenLocation);
        RetrieveTokenResponse response = new RetrieveTokenResponse();

        try {

            logger.debug("Retrieve the token from the database...");
            DigitalToken token = storageProvider.retrieveToken(tokenLocation);
            if (token == null) {
                logger.error("The token does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("token.not.found", tokenLocation);

            } else {
                logger.debug("The token retrieval request was successful...");
                response.token = token;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving a token: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving a token:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(tokenLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving token: {}\n{}", tokenLocation, e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveBatchResponse retrieveBatch(URI batchLocation) {
        logger.entry(batchLocation);
        RetrieveBatchResponse response = new RetrieveBatchResponse();

        try {

            logger.debug("Retrieve the batch from the database...");
            List<DigitalToken> tokens = storageProvider.retrieveBatch(batchLocation);
            if (tokens == null) {
                logger.error("The batch does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("batch.not.found", batchLocation);

            } else {
                logger.debug("The batch retrieval request was successful...");
                response.tokens = tokens;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving a batch: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving a batch:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(batchLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving batch: {}\n{}", batchLocation, e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RecordTransactionResponse recordTransaction(RecordTransactionRequest request) {
        logger.entry(request);
        RecordTransactionResponse response = new RecordTransactionResponse();
        response.transactionLocation = request.transaction.attributes.myLocation;

        try {

            logger.debug("Validating the request...");
            Map<String, Object> errors = new LinkedHashMap<>();
            DigitalTransaction transaction = request.transaction;
            URI transactionLocation = transaction.attributes.myLocation;
            if (storageProvider.transactionExists(transactionLocation)) {
                errors.put("transaction.already.exists", transactionLocation);
            }

            logger.debug("Validating the transaction...");

            logger.debug("Retrieving the token...");
            DocumentCitation tokenCitation = transaction.attributes.tokenCitation;
            URI tokenLocation = tokenCitation.documentLocation;
            DigitalToken token = storageProvider.retrieveToken(tokenLocation);
            if (token == null) {
                logger.error("The token associated with the transaction was not found: {}", tokenLocation);
                errors.put("transaction.token.not.found", transaction);
            } else {
                String document = token.toString();
                notarizationProvider.validateDocumentCitation(tokenCitation, document, errors);

                logger.debug("Retrieving the guarantor certificate...");
                DocumentCitation guarantorCitation = token.guarantorSeal.attributes.verificationCitation;
                URI guarantorLocation = guarantorCitation.documentLocation;
                RetrieveCertificateResponse retrieveResponse = identityManager.retrieveCertificate(guarantorLocation);
                NotaryCertificate guarantorCertificate = retrieveResponse.certificate;
                if (guarantorCertificate == null) {
                    logger.error("The guarantor certificate associated with the transaction was not found: {}", guarantorLocation);
                    errors.put("guarantor.certificate.not.found", guarantorCitation.documentLocation);
                } else {
                    document = guarantorCertificate.toString();
                    notarizationProvider.validateDocumentCitation(guarantorCitation, document, errors);
                }

                logger.debug("Retrieving the accountant certificate...");
                DocumentCitation accountantCitation = token.accountantSeal.attributes.verificationCitation;
                URI certificateLocation = accountantCitation.documentLocation;
                retrieveResponse = identityManager.retrieveCertificate(certificateLocation);
                NotaryCertificate accountantCertificate = retrieveResponse.certificate;
                if (accountantCertificate == null) {
                    logger.error("The accountant certificate associated with the transaction was not found: {}", certificateLocation);
                    errors.put("accountant.certificate.not.found", certificateLocation);
                } else {
                    document = accountantCertificate.toString();
                    notarizationProvider.validateDocumentCitation(accountantCitation, document, errors);
                }

                logger.debug("Retrieving the sender certificate...");
                DocumentCitation senderCitation = transaction.senderSeal.attributes.verificationCitation;
                certificateLocation = senderCitation.documentLocation;
                retrieveResponse = identityManager.retrieveCertificate(certificateLocation);
                NotaryCertificate senderCertificate = retrieveResponse.certificate;
                if (senderCertificate == null) {
                    logger.error("The sender certificate associated with the transaction was not found: {}", certificateLocation);
                    errors.put("sender.certificate.not.found", certificateLocation);
                } else {
                    document = senderCertificate.toString();
                    notarizationProvider.validateDocumentCitation(senderCitation, document, errors);
                }

                logger.debug("Retrieving the receiver certificate...");
                DocumentCitation receiverCitation = transaction.receiverSeal.attributes.verificationCitation;
                certificateLocation = receiverCitation.documentLocation;
                retrieveResponse = identityManager.retrieveCertificate(certificateLocation);
                NotaryCertificate receiverCertificate = retrieveResponse.certificate;
                if (receiverCertificate == null) {
                    logger.error("The receiver certificate associated with the transaction was not found: {}", certificateLocation);
                    errors.put("receiver.certificate.not.found", certificateLocation);
                } else {
                    document = receiverCertificate.toString();
                    notarizationProvider.validateDocumentCitation(receiverCitation, document, errors);
                }

                NotaryCertificate escrowCertificate = null;
                if (request.transaction.attributes.escrowLocation != null) {
                    logger.debug("Retrieving the escrow certificate...");
                    DocumentCitation escrowCitation = transaction.escrowSeal.attributes.verificationCitation;
                    certificateLocation = escrowCitation.documentLocation;
                    retrieveResponse = identityManager.retrieveCertificate(certificateLocation);
                    escrowCertificate = retrieveResponse.certificate;
                    if (escrowCertificate == null) {
                        logger.error("The escrow certificate associated with the transaction was not found: {}", certificateLocation);
                        errors.put("escrow.certificate.not.found", certificateLocation);
                    } else {
                        document = escrowCertificate.toString();
                        notarizationProvider.validateDocumentCitation(escrowCitation, document, errors);
                    }
                }

                accountingProvider.validateTransaction(transaction, token, guarantorCertificate, accountantCertificate, senderCertificate, receiverCertificate, escrowCertificate, errors);
            }

            if (!errors.isEmpty()) {
                logger.error("The request is invalid...");
                response.status = RequestStatus.Failed;
                response.reason = "Conflict";
                response.putAll(errors);

            } else {
                logger.debug("Saving the certified transaction in the datastore...");
                storageProvider.createTransaction(transaction);
                URI tokenLedgerLocation = URI.create(tokenLocation.toString().replaceFirst("/token/", "/ledger/"));
                storageProvider.createLedgerEntry(tokenLedgerLocation, transaction);
                URI senderLedgerLocation = URI.create(transaction.attributes.senderLocation.toString().replaceFirst("/identity/", "/ledger/"));
                storageProvider.createLedgerEntry(senderLedgerLocation, transaction);
                URI receiverLedgerLocation = URI.create(transaction.attributes.receiverLocation.toString().replaceFirst("/identity/", "/ledger/"));
                storageProvider.createLedgerEntry(receiverLedgerLocation, transaction);

                logger.debug("The transaction recording request was successful...");
                response.status = RequestStatus.Succeeded;
                response.reason = "Created";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while recording transactions: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while recording transactions:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(request, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while recording transactions:\n{}", (Object) e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveTransactionResponse retrieveTransaction(URI transactionLocation) {
        logger.entry(transactionLocation);
        RetrieveTransactionResponse response = new RetrieveTransactionResponse();

        try {

            logger.debug("Retrieve the transaction from the database...");
            DigitalTransaction transaction = storageProvider.retrieveTransaction(transactionLocation);
            if (transaction == null) {
                logger.debug("The transaction does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("transaction.not.found", transactionLocation);

            } else {
                logger.debug("The transaction retrieval request was successful...");
                response.transaction = transaction;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving a transaction: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving a transaction:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(transactionLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving transaction: {}\n{}", transactionLocation, e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveLedgerResponse retrieveLedger(URI ledgerLocation) {
        logger.entry(ledgerLocation);
        RetrieveLedgerResponse response = new RetrieveLedgerResponse();

        try {

            logger.debug("Retrieve the ledger from the database...");
            List<DigitalTransaction> ledger = storageProvider.retrieveLedger(ledgerLocation);
            if (ledger == null || ledger.isEmpty()) {
                logger.debug("The ledger does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("ledger.not.found", ledgerLocation);

            } else {
                logger.debug("The ledger retrieval request was successful...");
                response.ledger = ledger;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving a ledger: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving a ledger:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(ledgerLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving ledger: {}\n{}", ledgerLocation, e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    static private void handleValidationException(ValidationException exception, ServiceResponse<? extends ServiceResponse<?>> response) {
        response.status = RequestStatus.Failed;
        response.reason = "Bad Request";
        response.putAll(exception.errors);
    }


    static private void handleNullPointerException(Object request, ServiceResponse<? extends ServiceResponse<?>> response) {
        response.status = RequestStatus.Failed;
        response.reason = "Bad Request";
        response.put("request.is.missing.an.attribute", request);
    }


    static private void handleUnexpectedException(Exception exception, ServiceResponse<? extends ServiceResponse<?>> response) {
        response.status = RequestStatus.Failed;
        response.reason = "Internal Server Error";
        response.put("unexpected.error", exception.getMessage());
    }

}

