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
import craterdog.notary.NotaryCertificate;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Derk Norton
 */
public class IdentityManagementStorage {

    private final Map<URI, DigitalIdentity> identities = new LinkedHashMap<>();
    private final Map<URI, NotaryCertificate> certificates = new LinkedHashMap<>();
    private final Map<URI, List<NotaryCertificate>> certificatesByIdentity = new LinkedHashMap<>();


    public void createIdentity(DigitalIdentity identity) {
        URI identityLocation = identity.attributes.myLocation;
        identities.put(identityLocation, identity);
        certificatesByIdentity.put(identityLocation, new ArrayList<>());
    }


    public boolean identityExists(URI identityLocation) {
        return identities.get(identityLocation) != null;
    }


    public List<DigitalIdentity> queryAllIdentities() {
        return new ArrayList<>(identities.values());
    }


    public List<DigitalIdentity> queryIdentitiesByAttribute(String name, Object value) {
        List<DigitalIdentity> matches = new ArrayList<>();
        for (DigitalIdentity identity : identities.values()) {
            if (value.equals(identity.attributes.get(name))) {
                matches.add(identity);
            }
        }
        return matches;
    }


    public DigitalIdentity retrieveIdentity(URI identityLocation) {
        DigitalIdentity identity = identities.get(identityLocation);
        return identity;
    }


    public void createCertificate(URI certificateLocation, NotaryCertificate certificate) {
        URI identityLocation = certificate.attributes.identityLocation;
        certificates.put(certificateLocation, certificate);
        List<NotaryCertificate> list = certificatesByIdentity.get(identityLocation);
        list.add(certificate);
    }


    public boolean certificateExists(URI certificateLocation) {
        return certificates.get(certificateLocation) != null;
    }


    public NotaryCertificate retrieveCertificate(URI certificateLocation) {
        NotaryCertificate certificate = certificates.get(certificateLocation);
        return certificate;
    }


    public NotaryCertificate retrieveLatestCertificate(URI identityLocation) {
        List<NotaryCertificate> list = certificatesByIdentity.get(identityLocation);
        NotaryCertificate certificate = list.get(list.size() - 1);  // last element
        return certificate;
    }

}
