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
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.V1NotarizationProvider;
import craterdog.primitives.Tag;
import craterdog.tokens.DigitalToken;
import craterdog.tokens.Tokenization;
import craterdog.tokens.V1TokenizationProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 *
 * @author Derk
 */
public class TokenManagementServiceTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(TokenManagementServiceTest.class);
    static private final Notarization notarizationProvider = new V1NotarizationProvider();
    static private final Identification identificationProvider = new V1IdentificationProvider();
    static private final Tokenization tokenizationProvider = new V1TokenizationProvider();
    static private final Accounting accountingProvider = new V1AccountingProvider();


    @BeforeClass
    public static void setUpClass() {
        logger.info("Running TokenManagementService Unit Tests...\n");
    }


    @AfterClass
    public static void tearDownClass() {
        logger.info("Completed TokenManagementService Unit Tests.\n");
    }


    @Test
    public void testHappyPath() {
        logger.info("Testing the happy path...");

        logger.info("Creating the services...");
        URI identityBaseUri = URI.create("http://foo.com/IdentityManagement");
        IdentityManagement identityService = new IdentityManagementService();
        URI tokenBaseUri = URI.create("http://foo.com/TokenManagement");

        logger.info("Creating a new accountant...");
        NotaryKey accountantNotaryKey = notarizationProvider.generateNotaryKey(identityBaseUri);
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("role", "Accountant");
        attributes.put("name", "Bob Newhart");
        DigitalIdentity accountantIdentity = identificationProvider.createIdentity(attributes, accountantNotaryKey);
        NotaryCertificate accountantCertificate = accountantNotaryKey.verificationCertificate;
        URI accountantLocation = accountantCertificate.attributes.identityLocation;
        RegisterIdentityRequest registerRequest = new RegisterIdentityRequest();
        registerRequest.identity = accountantIdentity;
        registerRequest.certificate = accountantCertificate;
        outputExample("RegisterIdentityRequest.json", registerRequest);
        RegisterIdentityResponse registerResponse = identityService.registerIdentity(registerRequest);
        assertEquals(RequestStatus.Succeeded, registerResponse.status);
        outputExample("RegisterIdentityResponse.json", registerResponse);
        TokenManagement tokenService = new TokenManagementService(identityService, accountantNotaryKey, "digital-accountant");

        logger.info("Creating a new merchant...");
        NotaryKey merchantNotaryKey = notarizationProvider.generateNotaryKey(identityBaseUri);
        attributes.put("role", "Merchant");
        attributes.put("name", "StarBucks");
        DigitalIdentity merchantIdentity = identificationProvider.createIdentity(attributes, merchantNotaryKey);
        NotaryCertificate merchantCertificate = merchantNotaryKey.verificationCertificate;
        URI merchantLocation = merchantCertificate.attributes.identityLocation;
        registerRequest = new RegisterIdentityRequest();
        registerRequest.identity = merchantIdentity;
        registerRequest.certificate = merchantCertificate;
        registerResponse = identityService.registerIdentity(registerRequest);
        assertEquals(RequestStatus.Succeeded, registerResponse.status);

        logger.info("Creating a new consumer...");
        NotaryKey consumerNotaryKey = notarizationProvider.generateNotaryKey(identityBaseUri);
        attributes = new LinkedHashMap<>();
        attributes.put("role", "Consumer");
        attributes.put("name", "Java Lover");
        DigitalIdentity consumerIdentity = identificationProvider.createIdentity(attributes, consumerNotaryKey);
        NotaryCertificate consumerCertificate = consumerNotaryKey.verificationCertificate;
        URI consumerLocation = consumerCertificate.attributes.identityLocation;
        registerRequest = new RegisterIdentityRequest();
        registerRequest.identity = consumerIdentity;
        registerRequest.certificate = consumerCertificate;
        identityService.registerIdentity(registerRequest);
        assertEquals(RequestStatus.Succeeded, registerResponse.status);

        logger.info("Creating a new escrow...");
        NotaryKey escrowNotaryKey = notarizationProvider.generateNotaryKey(identityBaseUri);
        attributes = new LinkedHashMap<>();
        attributes.put("role", "Escrow");
        attributes.put("name", "TrustUs");
        DigitalIdentity escrowIdentity = identificationProvider.createIdentity(attributes, escrowNotaryKey);
        NotaryCertificate escrowCertificate = escrowNotaryKey.verificationCertificate;
        URI escrowLocation = escrowCertificate.attributes.identityLocation;
        registerRequest = new RegisterIdentityRequest();
        registerRequest.identity = escrowIdentity;
        registerRequest.certificate = escrowCertificate;
        identityService.registerIdentity(registerRequest);
        assertEquals(RequestStatus.Succeeded, registerResponse.status);

        logger.info("The merchant asks the digital accountant to certify some new tokens...");
        URI batchLocation = URI.create(tokenBaseUri + "/batch/" + new Tag());
        String tokenType = "Star Bucks";
        int numberOfTokens = 5;
        List<DigitalToken> tokens = new ArrayList<>();
        for (int i = 1; i <= numberOfTokens; i++) {
            URI tokenLocation = URI.create(tokenBaseUri + "/token/" + new Tag());
            DigitalToken token = tokenizationProvider.mintToken(tokenLocation, batchLocation,
                    accountantLocation, tokenType, Notarization.VALID_FOR_ONE_YEAR, merchantNotaryKey);
            tokens.add(token);
        }
        CertifyBatchRequest certifyRequest = new CertifyBatchRequest();
        certifyRequest.guarantorLocation = merchantLocation;
        certifyRequest.batchLocation = batchLocation;
        certifyRequest.tokens = tokens;
        outputExample("CertifyBatchRequest.json", certifyRequest);
        CertifyBatchResponse certifyResponse = tokenService.certifyBatch(certifyRequest);
        assertEquals(RequestStatus.Succeeded, certifyResponse.status);
        outputExample("CertifyBatchResponse.json", certifyResponse);

        logger.info("The merchant retrieves all tokens from the batch...");
        RetrieveBatchResponse batchResponse = tokenService.retrieveBatch(batchLocation);
        assertEquals(RequestStatus.Succeeded, batchResponse.status);
        outputExample("RetrieveBatchResponse.json", batchResponse);

        logger.info("The merchant retrieves a token from the accountant...");
        DigitalToken token = tokens.get(0);
        RetrieveTokenResponse tokenResponse = tokenService.retrieveToken(token.attributes.myLocation);
        assertEquals(RequestStatus.Succeeded, tokenResponse.status);
        assertEquals(token, tokenResponse.token);
        outputExample("RetrieveTokenResponse.json", tokenResponse);

        logger.info("The merchant notarizes a transfer transaction and sends them to the consumer...");
        URI transactionLocation = URI.create(tokenBaseUri + "/transaction/" + new Tag());
        String transactionType = "Transfer Bucks";
        token = tokens.get(0);
        DigitalTransaction transaction = accountingProvider.initiateTransaction(transactionLocation, merchantLocation, consumerLocation, escrowLocation, transactionType, token, merchantNotaryKey);

        logger.info("The consumer notarizes the transactions and sends them to the escrow provider...");
        accountingProvider.approveTransaction(transaction, token, merchantCertificate, consumerNotaryKey);

        logger.info("The escrow provider notarizes the transactions and has them recorded...");
        accountingProvider.certifyTransaction(transaction, token, merchantCertificate, consumerCertificate, escrowNotaryKey);
        RecordTransactionRequest recordRequest = new RecordTransactionRequest();
        recordRequest.transaction = transaction;
        outputExample("RecordTransactionRequest.json", recordRequest);
        RecordTransactionResponse recordResponse = tokenService.recordTransaction(recordRequest);
        assertEquals(RequestStatus.Succeeded, recordResponse.status);
        outputExample("RecordTransactionResponse.json", recordResponse);
        assertEquals(transactionLocation, recordResponse.transactionLocation);

        logger.info("The merchant retrieves the recorded transaction...");
        RetrieveTransactionResponse transactionResponse = tokenService.retrieveTransaction(transactionLocation);
        outputExample("RetrieveTransactionResponse.json", transactionResponse);

        logger.info("The merchant retrieves the ledger for a token...");
        URI tokenLedgerLocation = URI.create(token.attributes.myLocation.toString().replaceFirst("/token/", "/ledger/"));
        RetrieveLedgerResponse ledgerResponse = tokenService.retrieveLedger(tokenLedgerLocation);
        assertEquals(RequestStatus.Succeeded, ledgerResponse.status);
        outputExample("RetrieveLedgerResponse.json", ledgerResponse);

        logger.info("Testing of the happy path complete.\n");
    }


    void outputExample(String filename, Object object) {
        File examples = new File("target/examples");
        examples.mkdirs();
        String fullFilename = examples + File.separator + filename;
        try (PrintWriter writer = new PrintWriter(fullFilename)) {
            writer.print(object);
        } catch (IOException e) {
            fail("Unable to open the following file: " + filename);
        }
    }

}
