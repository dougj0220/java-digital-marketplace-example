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


/**
 * This class defines the attributes that are returned from a request to an identity management
 * service to retrieve an identity.
 *
 * @author Derk Norton
 */
public final class RetrieveIdentityResponse extends ServiceResponse<RetrieveIdentityResponse> {

    /**
     * The requested identity.
     */
    public DigitalIdentity identity;

}
