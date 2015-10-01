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
import craterdog.notary.mappers.NotaryModule;


/**
 * This class defines the attributes that must be passed to an identity management service to
 * create a new identity.
 *
 * @author Derk Norton
 */
public final class RegisterIdentityRequest extends ServiceRequest<RegisterIdentityRequest> {

    /**
     * The actual attributes that make up the digital identity.
     */
    public DigitalIdentity identity;

    /**
     * The initial digital certificate for the new digital identity.
     */
    public NotaryCertificate certificate;


    /**
     * The default constructor makes sure that the public keys can be marshalled
     * properly into JSON.
     */
    public RegisterIdentityRequest() {
        this.addSerializableClass(new NotaryModule());
    }

}
