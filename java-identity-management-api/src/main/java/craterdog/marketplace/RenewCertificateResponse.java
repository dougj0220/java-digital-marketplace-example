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


/**
 * This class defines the attributes that are returned from a request to an identity management
 * service to replace the current certificate with a new one.
 *
 * @author Derk Norton
 */
public final class RenewCertificateResponse extends ServiceResponse<RenewCertificateResponse> {

    /**
     * The location of the new certificate for the identity.
     */
    public URI certificateLocation;

}
