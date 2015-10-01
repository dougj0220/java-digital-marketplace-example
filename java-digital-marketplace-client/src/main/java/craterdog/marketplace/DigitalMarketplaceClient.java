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

import com.fasterxml.jackson.databind.ObjectMapper;
import craterdog.identities.DigitalIdentity;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.mappers.NotaryModule;
import craterdog.smart.SmartObjectMapper;
import craterdog.tokens.DigitalToken;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


/**
 * This class acts as a web client for any service that supports the <code>IdentityManagement</code>
 * and <code>TokenManagement</code> interfaces.
 *
 * @author Derk Norton
 */
public final class DigitalMarketplaceClient implements IdentityManagement, TokenManagement {

    static private final XLogger logger = XLoggerFactory.getXLogger(DigitalMarketplaceClient.class);
    static private final RestTemplate restTemplate = new RestTemplate();

    private final Map<URI, DigitalIdentity> identities = new LinkedHashMap<>();
    private final Map<URI, NotaryCertificate> certificates = new LinkedHashMap<>();
    private final Map<URI, DigitalToken> tokens = new LinkedHashMap<>();


    static {
        ObjectMapper mapper = new SmartObjectMapper();
        // handle conversion of public keys in certificates
        mapper.registerModule(new NotaryModule());
        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(mapper));
        restTemplate.setErrorHandler(new ServiceResponseErrorHandler());
    }

    private final URI serviceUri;

    public DigitalMarketplaceClient(URI serviceUri) {
        logger.entry();
        this.serviceUri = serviceUri;
        logger.exit();
    }


    @Override
    public RegisterIdentityResponse registerIdentity(RegisterIdentityRequest request) {
        logger.entry(request);
        RegisterIdentityResponse response = restTemplate.postForObject(serviceUri + "/identity", request, RegisterIdentityResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveIdentityResponse retrieveIdentity(URI identityLocation) {
        logger.entry(identityLocation);
        RetrieveIdentityResponse response;

        DigitalIdentity identity = identities.get(identityLocation);
        if (identity != null) {
            logger.debug("Found the following identity in the cache: {}", identity);
            response = new RetrieveIdentityResponse();
            response.identity = identity;
            response.status = RequestStatus.Succeeded;
            response.reason = "OK";

        } else {
            logger.debug("Retrieving the identity from the registry...");
            response = restTemplate.getForObject(identityLocation.toString(), RetrieveIdentityResponse.class);
            identity = response.identity;
            if (identity != null) {
                logger.debug("Caching the following identity: {}", identity);
                identities.put(identityLocation, identity);
            }
        }

        logger.exit(response);
        return response;
    }


    @Override
    public QueryIdentitiesResponse queryAllIdentities() {
        logger.entry();
        QueryIdentitiesResponse response = restTemplate.getForObject(serviceUri + "/identities/all", QueryIdentitiesResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public QueryIdentitiesResponse queryIdentitiesByAttribute(String name, Object value) {
        logger.entry(name, value);
        QueryIdentitiesResponse response = restTemplate.getForObject(serviceUri + "/identities/?name={name}&value={value}",
                QueryIdentitiesResponse.class, name, value);
        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveCertificateResponse retrieveLatestCertificate(URI identityLocation) {
        logger.entry(identityLocation);

        logger.debug("Retrieving the certificate from the registry...");
        RetrieveCertificateResponse response = restTemplate.getForObject(identityLocation.toString() + "/certificate", RetrieveCertificateResponse.class);
        NotaryCertificate certificate = response.certificate;
        if (certificate != null) {
            logger.debug("Caching the following certificate: {}", certificate);
            URI certificateLocation = certificate.attributes.myLocation;
            certificates.put(certificateLocation, certificate);
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveCertificateResponse retrieveCertificate(URI certificateLocation) {
        logger.entry(certificateLocation);
        RetrieveCertificateResponse response;

        NotaryCertificate certificate = certificates.get(certificateLocation);
        if (certificate != null) {
            logger.debug("Found the following certificate in the cache: {}", certificate);
            response = new RetrieveCertificateResponse();
            response.certificate = certificate;
            response.status = RequestStatus.Succeeded;
            response.reason = "OK";

        } else {
            logger.debug("Retrieving the certificate from the registry...");
            response = restTemplate.getForObject(certificateLocation.toString(), RetrieveCertificateResponse.class);
            certificate = response.certificate;
            if (certificate != null) {
                logger.debug("Caching the following certificate: {}", certificate);
                certificates.put(certificateLocation, certificate);
            }
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RenewCertificateResponse renewCertificate(RenewCertificateRequest request) {
        logger.entry(request);
        RenewCertificateResponse response = restTemplate.postForObject(serviceUri + "/certificate", request, RenewCertificateResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public CertifyBatchResponse certifyBatch(CertifyBatchRequest request) {
        logger.entry(request);
        CertifyBatchResponse response = restTemplate.postForObject(serviceUri + "/batch", request, CertifyBatchResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveTokenResponse retrieveToken(URI tokenLocation) {
        logger.entry(tokenLocation);
        RetrieveTokenResponse response;

        DigitalToken token = tokens.get(tokenLocation);
        if (token != null) {
            logger.debug("Found the following token in the cache: {}", token);
            response = new RetrieveTokenResponse();
            response.token = token;
            response.status = RequestStatus.Succeeded;
            response.reason = "OK";

        } else {
            logger.debug("Retrieving the token from the registry...");
            response = restTemplate.getForObject(tokenLocation.toString(), RetrieveTokenResponse.class);
            token = response.token;
            if (token != null) {
                logger.debug("Caching the following token: {}", token);
                tokens.put(tokenLocation, token);
            }
        }

        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveBatchResponse retrieveBatch(URI batchLocation) {
        logger.entry(batchLocation);
        RetrieveBatchResponse response = restTemplate.getForObject(batchLocation.toString(), RetrieveBatchResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public RecordTransactionResponse recordTransaction(RecordTransactionRequest request) {
        logger.entry(request);
        RecordTransactionResponse response = restTemplate.postForObject(serviceUri + "/transaction", request, RecordTransactionResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveTransactionResponse retrieveTransaction(URI transactionLocation) {
        logger.entry(transactionLocation);
        RetrieveTransactionResponse response = restTemplate.getForObject(transactionLocation.toString(), RetrieveTransactionResponse.class);
        logger.exit(response);
        return response;
    }


    @Override
    public RetrieveLedgerResponse retrieveLedger(URI ledgerLocation) {
        logger.entry(ledgerLocation);
        RetrieveLedgerResponse response = restTemplate.getForObject(ledgerLocation.toString(), RetrieveLedgerResponse.class);
        logger.exit(response);
        return response;
    }

}
