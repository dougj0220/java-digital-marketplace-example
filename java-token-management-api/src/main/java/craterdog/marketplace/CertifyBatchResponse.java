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

import java.net.URI;
import java.util.List;


/**
 * This class defines the attributes that are returned from a request to a token management
 * service to pay for and certify a new batch of digital tokens.
 *
 * @author Derk Norton
 */
public final class CertifyBatchResponse extends ServiceResponse<CertifyBatchResponse> {

    /**
     * The location of the batch of new tokens.
     */
    public URI batchLocation;

    /**
     * The list of certified digital token locations.
     */
    public List<URI> tokenLocations;

}
