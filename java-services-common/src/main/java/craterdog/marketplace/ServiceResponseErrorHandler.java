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

import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;


/**
 * This class checks to see if the HTTP response is unexpected.
 *
 * @author Derk Norton
 */
public final class ServiceResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected boolean hasError(HttpStatus statusCode) {
        // only throw exception when the status code is not expected
        boolean result = statusCode.is1xxInformational() || statusCode.is3xxRedirection();
        return result;
    }

}
