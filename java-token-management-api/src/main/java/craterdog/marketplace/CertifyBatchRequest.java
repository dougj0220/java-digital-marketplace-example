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
import java.net.URI;
import java.util.List;


/**
 * This class defines the attributes that must be passed to a token management service to
 * pay for and certify a new batch of tokens.  The certified tokens will then be published
 * in a public place.  The confirmed digital transactions used as payment will also be
 * published in a public place.
 *
 * @author Derk Norton
 */
public final class CertifyBatchRequest extends ServiceRequest<CertifyBatchRequest> {

    /**
     * The location of the identity that will be guaranteeing the value of the
     * tokens.
     */
    public URI guarantorLocation;

    /**
     * The location of the batch of new tokens.
     */
    public URI batchLocation;

    /**
     * The list of requested digital tokens.  Each token has both a valid mint seal and
     * guarantor seal on it.
     */
    public List<DigitalToken> tokens;

}
