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

import craterdog.notary.NotaryCertificate;
import craterdog.notary.mappers.NotaryModule;


/**
 * This class defines the attributes that must be passed to an identity management service to
 * replace the current public certificate associated with the identity with a new one.
 *
 * @author Derk Norton
 */
public final class RenewCertificateRequest extends ServiceRequest<RenewCertificateRequest> {

    /**
     * The actual attributes that make up the new digital certificate.
     */
    public NotaryCertificate newCertificate;


    /**
     * The default constructor makes sure that the public keys can be marshalled
     * properly into JSON.
     */
    public RenewCertificateRequest() {
        this.addSerializableClass(new NotaryModule());
    }

}
