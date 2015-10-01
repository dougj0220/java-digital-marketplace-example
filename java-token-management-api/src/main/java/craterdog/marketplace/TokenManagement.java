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
 * This interface defines the operations that must be supported by all token management services.
 *
 * @author Derk
 */
public interface TokenManagement {

    /**
     * This operation certifies a new batch of tokens that were minted for an identity that
     * acts as the guarantor for the value of the tokens.
     *
     * @param request The request to certify a new batch of tokens.
     * @return The response to the request.
     */
    CertifyBatchResponse certifyBatch(CertifyBatchRequest request);

    /**
     * This operation retrieves the specified token.
     *
     * @param tokenLocation The location of the token to be retrieved.
     * @return The requested token.
     */
    RetrieveTokenResponse retrieveToken(URI tokenLocation);

    /**
     * This operation retrieves the tokens associated with the specified batch location.
     *
     * @param batchLocation The location of the batch of tokens to be retrieved.
     * @return The requested tokens.
     */
    RetrieveBatchResponse retrieveBatch(URI batchLocation);

    /**
     * This operation records a new digital transaction and places it in a publicly
     * accessible area.
     *
     * @param request The request to record a new transaction.
     * @return The response to the request.
     */
    RecordTransactionResponse recordTransaction(RecordTransactionRequest request);

    /**
     * This operation retrieves a specific digital transaction.
     *
     * @param transactionLocation The location of the digital transaction.
     * @return The response to the request.
     */
    RetrieveTransactionResponse retrieveTransaction(URI transactionLocation);

    /**
     * This operation retrieves the digital ledger for all transactions performed on a
     * specific digital token or by a specific digital identity.
     *
     * @param ledgerLocation The location of the digital ledger.
     * @return The response to the request.
     */
    RetrieveLedgerResponse retrieveLedger(URI ledgerLocation);

}
