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

import craterdog.identities.Identification;
import craterdog.identities.DigitalIdentity;
import craterdog.identities.V1IdentificationProvider;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.V1NotarizationProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.LinkedHashMap;
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
public class IdentityManagementServiceTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(IdentityManagementServiceTest.class);
    static private final Notarization notarizationProvider = new V1NotarizationProvider();
    static private final Identification identificationProvider = new V1IdentificationProvider();
    static private final IdentityManagement identityService = new IdentityManagementService();


    @BeforeClass
    public static void setUpClass() {
        logger.info("Running IdentityManagementService Unit Tests...\n");
    }


    @AfterClass
    public static void tearDownClass() {
        logger.info("Completed IdentityManagementService Unit Tests.\n");
    }


    @Test
    public void testHappyPath() {
        logger.info("Testing the happy path...");

        logger.info("Creating a new identity...");
        URI baseUri = URI.create("http://foo.com/IdentityManagement");
        NotaryKey notaryKey = notarizationProvider.generateNotaryKey(baseUri);
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("foo", 5);
        attributes.put("bar", "baz");
        DigitalIdentity identity = identificationProvider.createIdentity(attributes, notaryKey);
        NotaryCertificate certificate = notaryKey.verificationCertificate;
        RegisterIdentityRequest createRequest = new RegisterIdentityRequest();
        createRequest.identity = identity;
        createRequest.certificate = certificate;
        outputExample("RegisterIdentityRequest.json", createRequest);
        RegisterIdentityResponse createResponse = identityService.registerIdentity(createRequest);
        outputExample("RegisterIdentityResponse.json", createResponse);

        logger.info("Retrieving the new identity...");
        URI identityLocation = createResponse.identityLocation;
        RetrieveIdentityResponse retrieveIdentityResponse = identityService.retrieveIdentity(identityLocation);
        outputExample("RetrieveIdentityResponse.json", retrieveIdentityResponse);

        logger.info("Retrieving the certificate for the identity...");
        URI certificateLocation = createResponse.certificateLocation;
        RetrieveCertificateResponse retrieveResponse = identityService.retrieveCertificate(certificateLocation);
        outputExample("RetrieveCertificateResponse.json", retrieveResponse);

        logger.info("Querying identities...");
        String name = "foo";
        int value = 5;
        QueryIdentitiesResponse queryByAttributeResponse = identityService.queryIdentitiesByAttribute(name, value);
        QueryIdentitiesResponse queryAllResponse = identityService.queryAllIdentities();
        assertEquals(queryByAttributeResponse, queryAllResponse);

        logger.info("Renewing the certificate for the identity...");
        NotaryKey newNotaryKey = notarizationProvider.generateNotaryKey(baseUri, notaryKey);
        notaryKey = newNotaryKey;  // destroy the old key!
        certificate = notaryKey.verificationCertificate;
        RenewCertificateRequest renewRequest = new RenewCertificateRequest();
        renewRequest.newCertificate = certificate;
        outputExample("RenewCertificateRequest.json", renewRequest);
        RenewCertificateResponse renewResponse = identityService.renewCertificate(renewRequest);
        outputExample("RenewCertificateResponse.json", renewResponse);

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
