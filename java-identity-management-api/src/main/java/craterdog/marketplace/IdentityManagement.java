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
 * This interface defines the operations that must be supported by all identity management services.
 *
 * @author Derk
 */
public interface IdentityManagement {

    /**
     * This operation creates a new digital identity and places it in a publicly accessible area.
     *
     * @param request The request to create a new identity.
     * @return The response to the request.
     */
    RegisterIdentityResponse registerIdentity(RegisterIdentityRequest request);

    /**
     * This operation retrieves a digital identity based on its location.
     *
     * @param identityLocation The location of the identity to be retrieved.
     * @return The response to the request.
     */
    RetrieveIdentityResponse retrieveIdentity(URI identityLocation);

    /**
     * This operation retrieves all the registered digital identities.
     *
     * @return The response to the request.
     */
    QueryIdentitiesResponse queryAllIdentities();

    /**
     * This operation retrieves the digital identities that contain the specified name-value pair.
     *
     * @param name The name of the attribute to query by.
     * @param value The value of the named attribute to query by.
     * @return The response to the request.
     */
    QueryIdentitiesResponse queryIdentitiesByAttribute(String name, Object value);

    /**
     * This operation retrieves the latest public certificate for a digital identity.
     *
     * @param identityLocation The location of the identity whose certificate is to be retrieved.
     * @return The response to the request.
     */
    RetrieveCertificateResponse retrieveLatestCertificate(URI identityLocation);

    /**
     * This operation retrieves a public certificate.
     *
     * @param certificateLocation The location of the certificate to be retrieved.
     * @return The response to the request.
     */
    RetrieveCertificateResponse retrieveCertificate(URI certificateLocation);

    /**
     * This operation adds a new public certificate to the list of certificates for an identity.
     * The new certificate must be signed with the private key corresponding to the latest
     * public certificate associated with this identity.
     *
     * @param request The request to add a new certificate.
     * @return The response to the request.
     */
    RenewCertificateResponse renewCertificate(RenewCertificateRequest request);

}
