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
import java.util.List;


/**
 * This class defines the attributes that are returned from a request to a token management
 * service to retrieve the tokens associated with the specified batch identifier.
 *
 * @author Derk Norton
 */
public final class RetrieveBatchResponse extends ServiceResponse<RetrieveBatchResponse> {

    /**
     * The tokens that match the specified batch identifier.
     */
    public List<DigitalToken> tokens;

}
