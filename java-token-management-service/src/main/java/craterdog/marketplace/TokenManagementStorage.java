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
import craterdog.accounting.DigitalTransaction;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * This class provides an in-memory database for the management of digital tokens and transactions.
 *
 * @author Derk Norton
 */
public class TokenManagementStorage {

    private final Map<URI, DigitalToken> tokens = new LinkedHashMap<>();
    private final Map<URI, List<DigitalToken>> batches = new LinkedHashMap<>();
    private final Map<URI, DigitalTransaction> transactions = new LinkedHashMap<>();
    private final Map<URI, List<DigitalTransaction>> ledgers = new LinkedHashMap<>();


    public void createTokens(URI batchLocation, List<DigitalToken> tokens) {
        for (DigitalToken token : tokens) {
            URI tokenLocation = token.attributes.myLocation;
            this.tokens.put(tokenLocation, token);
        }
        batches.put(batchLocation, tokens);
    }


    public boolean tokenExists(URI tokenLocation) {
        return tokens.get(tokenLocation) != null;
    }


    public DigitalToken retrieveToken(URI tokenLocation) {
        DigitalToken token = tokens.get(tokenLocation);
        return token;
    }


    public boolean batchExists(URI batchLocation) {
        return batches.get(batchLocation) != null;
    }


    public List<DigitalToken> retrieveBatch(URI batchLocation) {
        List<DigitalToken> batch = batches.get(batchLocation);
        return batch;
    }


    public void createTransaction(DigitalTransaction transaction) {
        transactions.put(transaction.attributes.myLocation, transaction);
    }


    public boolean transactionExists(URI transactionLocation) {
        return transactions.get(transactionLocation) != null;
    }


    public DigitalTransaction retrieveTransaction(URI transactionLocation) {
        return transactions.get(transactionLocation);
    }


    public DigitalTransaction retrieveLatestTransaction(URI tokenLocation) {
        DigitalTransaction latestTransaction = null;
        List<DigitalTransaction> list = ledgers.get(tokenLocation);
        if (list != null && !list.isEmpty()) {
            latestTransaction = list.get(list.size() - 1);  // last one
        }
        return latestTransaction;
    }


    public void createLedgerEntry(URI ledgerLocation, DigitalTransaction transaction) {
        List<DigitalTransaction> list = ledgers.get(ledgerLocation);
        if (list == null) {
            // the ledger does not yet exist so create it
            list = new ArrayList<>();
            ledgers.put(ledgerLocation, list);
        }
        list.add(transaction);
    }


    public List<DigitalTransaction> retrieveLedger(URI ledgerLocation) {
        List<DigitalTransaction> list = ledgers.get(ledgerLocation);
        if (list == null) list = new ArrayList<>();
        return list;
    }

}
