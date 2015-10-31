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

import craterdog.identities.DigitalIdentity;
import craterdog.identities.Identification;
import craterdog.identities.V1IdentificationProvider;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.Notarization;
import craterdog.notary.ValidationException;
import craterdog.notary.V1NotarizationProvider;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 *
 * @author Derk Norton
 */
public final class IdentityManagementService implements IdentityManagement {

    static private final XLogger logger = XLoggerFactory.getXLogger(IdentityManagementService.class);

    private final Notarization notarizationProvider = new V1NotarizationProvider();
    private final Identification identificationProvider = new V1IdentificationProvider();
    private final IdentityManagementStorage storageProvider = new IdentityManagementStorage();


    public IdentityManagementService() {
        logger.entry();
        logger.exit();
    }


    @Override
    public RegisterIdentityResponse registerIdentity(RegisterIdentityRequest request) {
        logger.entry(request);
        RegisterIdentityResponse response = new RegisterIdentityResponse();
        response.identityLocation = request.identity.attributes.myLocation;
        response.certificateLocation = request.certificate.attributes.myLocation;

        try {

            logger.debug("Validating the request...");
            DigitalIdentity identity = request.identity;
            URI identityLocation = identity.attributes.myLocation;
            NotaryCertificate certificate = request.certificate;
            URI certificateLocation = certificate.attributes.myLocation;
            Map<String, Object> errors = new LinkedHashMap<>();
            identificationProvider.validateIdentity(identity, certificate, errors);
            if (!errors.isEmpty()) {
                logger.error("The request is invalid...");
                response.status = RequestStatus.Failed;
                response.reason = "Bad Request";
                response.putAll(errors);

            } else if (storageProvider.identityExists(identityLocation)) {
                logger.error("The identity already exists in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Conflict";
                response.put("identity.already.exists", identityLocation);

            } else {
                logger.debug("Saving the new identity in the database...");
                storageProvider.createIdentity(identity);

                logger.debug("Saving the new certificate in the database...");
                storageProvider.createCertificate(certificateLocation, certificate);

                logger.debug("The identity creation request was successful...");
                response.status = RequestStatus.Succeeded;
                response.reason = "Created";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while creating a new identity: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while creating a new identity:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(request, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while creating a new identity:\n{}", (Object) e.getStackTrace());
            handleUnexpectedException(e, response);

        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveIdentityResponse retrieveIdentity(URI identityLocation) {
        logger.entry(identityLocation);
        RetrieveIdentityResponse response = new RetrieveIdentityResponse();

        try {

            logger.debug("Retrieve the identity from the database...");
            DigitalIdentity identity = storageProvider.retrieveIdentity(identityLocation);
            if (identity == null) {
                logger.error("The identity does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("identity.not.found", identityLocation);

            } else {
                logger.debug("The identity retrieval request was successful...");
                response.identity = identity;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving an identity: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving an identity:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(identityLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving identity: {}\n{}", identityLocation, e.getStackTrace());
            handleUnexpectedException(e, response);

        }

        logger.exit(response);
        return response;
    }


    @Override
    public QueryIdentitiesResponse queryAllIdentities() {
        logger.entry();
        QueryIdentitiesResponse response = new QueryIdentitiesResponse();

        try {

            logger.debug("Retrieve the identities from the database...");
            List<DigitalIdentity> identities = storageProvider.queryAllIdentities();

            logger.debug("The identity query request was successful...");
            response.identities = identities;
            response.status = RequestStatus.Succeeded;
            response.reason = "OK";

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving all identities: {}", e.errors);
            handleValidationException(e, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while querying all identities:\n{}", (Object) e.getStackTrace());
            handleUnexpectedException(e, response);

        }

        logger.exit(response);
        return response;
    }


    @Override
    public QueryIdentitiesResponse queryIdentitiesByAttribute(String name, Object value) {
        logger.entry(name, value);
        QueryIdentitiesResponse response = new QueryIdentitiesResponse();

        try {

            logger.debug("Retrieve the identities from the database...");
            List<DigitalIdentity> identities = storageProvider.queryIdentitiesByAttribute(name, value);

            logger.debug("The identity query request was successful...");
            response.identities = identities;
            response.status = RequestStatus.Succeeded;
            response.reason = "OK";

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while querying identities: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while querying identities:\n{}", (Object) e.getStackTrace());
            if (name == null) {
                handleNullPointerException("name", response);
            } else {
                handleNullPointerException("value", response);
            }

        } catch (Exception e) {
            logger.error("Unexpected exception caught while querying identities:\n{}", (Object) e.getStackTrace());
            handleUnexpectedException(e, response);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveCertificateResponse retrieveLatestCertificate(URI identityLocation) {
        logger.entry(identityLocation);
        RetrieveCertificateResponse response = new RetrieveCertificateResponse();

        try {

            logger.debug("Retrieve the certificate from the database...");
            NotaryCertificate certificate = storageProvider.retrieveLatestCertificate(identityLocation);
            if (certificate == null) {
                logger.error("A certificate for the identity does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("certificate.for.identity.not.found", identityLocation);

            } else {
                logger.debug("The certificate retrieval request was successful...");
                response.certificate = certificate;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving the latest certificate for an identity: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving the latest certificate for an identity:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(identityLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving the latest certificate for an identity: {}\n{}", identityLocation, e.getStackTrace());
            handleUnexpectedException(e, response);

        }

        logger.exit(response);
        return response;

    }


    @Override
    public RetrieveCertificateResponse retrieveCertificate(URI certificateLocation) {
        logger.entry(certificateLocation);
        RetrieveCertificateResponse response = new RetrieveCertificateResponse();

        try {

            logger.debug("Retrieve the certificate from the database...");
            NotaryCertificate certificate = storageProvider.retrieveCertificate(certificateLocation);
            if (certificate == null) {
                logger.error("The certificate does not exist in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Not Found";
                response.put("certificate.not.found", certificateLocation);

            } else {
                logger.debug("The certificate retrieval request was successful...");
                response.certificate = certificate;
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while retrieving a certificate: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while retrieving a certificate:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(certificateLocation, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while retrieving certificate: {}\n{}", certificateLocation, e.getStackTrace());
            handleUnexpectedException(e, response);

        }

        logger.exit(response);
        return response;
    }


    @Override
    public RenewCertificateResponse renewCertificate(RenewCertificateRequest request) {
        logger.entry(request);
        RenewCertificateResponse response = new RenewCertificateResponse();
        response.certificateLocation = request.newCertificate.attributes.myLocation;

        try {

            logger.debug("Validating the request...");
            NotaryCertificate newCertificate = request.newCertificate;
            URI newCertificateLocation = newCertificate.attributes.myLocation;
            NotaryCertificate previousCertificate = null;
            if (newCertificate.certificationSeal != null) {
                logger.debug("Retrieving the previous certificate from the database...");
                URI previousCertificateLocation = newCertificate.certificationSeal.attributes.verificationCitation.documentLocation;
                previousCertificate = storageProvider.retrieveCertificate(previousCertificateLocation);
            }
            Map<String, Object> errors = new LinkedHashMap<>();
            notarizationProvider.validateNotaryCertificate(newCertificate, previousCertificate, errors);
            if (!errors.isEmpty()) {
                logger.error("The request is invalid...");
                response.status = RequestStatus.Failed;
                response.reason = "Bad Request";
                response.putAll(errors);

            } else if (storageProvider.certificateExists(newCertificateLocation)) {
                logger.error("The certificate already exists in the database...");
                response.status = RequestStatus.Failed;
                response.reason = "Conflict";
                response.put("certificate.already.exists", newCertificateLocation);

            } else {
                logger.debug("Adding the new certificate to the database...");
                storageProvider.createCertificate(newCertificateLocation, newCertificate);

                logger.debug("The certificate renewal request was successful...");
                response.status = RequestStatus.Succeeded;
                response.reason = "OK";
            }

        } catch (ValidationException e) {
            logger.error("The following transaction exception was caught while renewing a certificate: {}", e.errors);
            handleValidationException(e, response);

        } catch (NullPointerException e) {
            logger.error("The following null pointer exception was caught while renewing a certificate:\n{}", (Object) e.getStackTrace());
            handleNullPointerException(request, response);

        } catch (Exception e) {
            logger.error("Unexpected exception caught while adding a new certificate:\n{}", (Object) e.getStackTrace());
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