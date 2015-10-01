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

import craterdog.tokens.DigitalToken;


/**
 * This class defines the attributes that are returned from a request to a token management
 * service to retrieve a token associated with the specified token identifier.
 *
 * @author Derk Norton
 */
public final class RetrieveTokenResponse extends ServiceResponse<RetrieveTokenResponse> {

    /**
     * The token that matches the specified token identifier.
     */
    public DigitalToken token;

}
