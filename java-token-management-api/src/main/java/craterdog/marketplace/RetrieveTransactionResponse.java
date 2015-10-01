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

import craterdog.accounting.DigitalTransaction;


/**
 * This class defines the attributes that are returned from a request to a transaction management
 * service to retrieve a digital transaction.
 *
 * @author Derk Norton
 */
public final class RetrieveTransactionResponse extends ServiceResponse<RetrieveTransactionResponse> {

    /**
     * The requested transaction.
     */
    public DigitalTransaction transaction;

}
