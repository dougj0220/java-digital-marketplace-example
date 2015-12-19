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

import com.fasterxml.jackson.databind.ObjectMapper;
import craterdog.accounting.Accounting;
import craterdog.accounting.DigitalTransaction;
import craterdog.accounting.V1AccountingProvider;
import craterdog.identities.DigitalIdentity;
import craterdog.identities.Identification;
import craterdog.identities.V1IdentificationProvider;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryCertificate;
import craterdog.notary.NotaryKey;
import craterdog.notary.V1NotarizationProvider;
import craterdog.primitives.Tag;
import craterdog.smart.SmartObject;
import craterdog.tokens.DigitalToken;
import craterdog.tokens.Tokenization;
import craterdog.tokens.V1TokenizationProvider;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.web.client.ResourceAccessException;

/**
 * This class provides a command line interface to the digital marketplace services.
 *
 * @author web-online
 * @author Derk Norton
 */
public class DigitalMarketplaceCli {

    static private final XLogger logger = XLoggerFactory.getXLogger(DigitalMarketplaceCli.class);
    static private final String defaultDigitalAccountantUri = "http://localhost:8080/DigitalMarketplace";
    static private final String dataDirectory = System.getProperty("user.home") + "/.cdt";
    static private final ObjectMapper mapper = SmartObject.createMapper();

    static private final String[] commands = {
        "register-identity",
        "retrieve-identity",
        "retrieve-identities",
        "query-identities",
        "retrieve-certificate",
        "renew-certificate",
        "certify-batch",
        "retrieve-batch",
        "retrieve-token",
        "transfer-token",
        "retrieve-transaction",
        "retrieve-ledger",
        "quit"
    };

    static private final Option pseudonymOption = Option.builder("pseudonym").
            hasArg().argName("pseudonym").desc("the pseudonym for the identity").required().build();

    static private final Option identityOption = Option.builder("identity").
            hasArg().argName("identity").desc("the identifier of the identity").required().build();

    static private final Option attributeOption = Option.builder("attribute").
            hasArg().argName("attribute").desc("the name of the attribute").required().build();

    static private final Option valueOption = Option.builder("value").
            hasArg().argName("value").desc("the value of the attribute").required().build();

    static private final Option merchantOption = Option.builder("merchant").
            hasArg().argName("merchant").desc("the name of the merchant").required().build();

    static private final Option accountantOption = Option.builder("accountant").
            hasArg().argName("accountant").desc("the name of the accountant").required().build();

    static private final Option senderOption = Option.builder("sender").
            hasArg().argName("sender").desc("the name of the sender").required().build();

    static private final Option receiverOption = Option.builder("receiver").
            hasArg().argName("receiver").desc("the name of the receiver").required().build();

    static private final Option typeOption = Option.builder("type").
            hasArg().argName("type").desc("the type of the token").required().build();

    static private final Option countOption = Option.builder("count").
            hasArg().argName("count").desc("the number of tokens").required().build();

    static private final Option batchOption = Option.builder("batch").
            hasArg().argName("batch").desc("the identifier of the batch of tokens").required().build();

    static private final Option tokenOption = Option.builder("token").
            hasArg().argName("token").desc("the identifier of the token").required().build();

    static private final Option transactionOption = Option.builder("transaction").
            hasArg().argName("transaction").desc("the identifier of the transaction").required().build();

    static private final Option ledgerOption = Option.builder("ledger").
            hasArg().argName("ledger").desc("the identifier of the ledger").required().build();

    private final Notarization notary;
    private final Identification identificationProvider;
    private final Tokenization tokenizationProvider;
    private final Accounting accountingProvider;
    private final URI digitalMarketplaceUri;
    private final DigitalMarketplaceClient digitalMarketplaceClient;

    private final Console console;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private boolean done = false;


    public DigitalMarketplaceCli(URI digitalMarketplaceUri) {
        this.digitalMarketplaceUri = digitalMarketplaceUri;
        digitalMarketplaceClient = new DigitalMarketplaceClient(digitalMarketplaceUri);
        notary = new V1NotarizationProvider();
        identificationProvider = new V1IdentificationProvider();
        tokenizationProvider = new V1TokenizationProvider();
        accountingProvider = new V1AccountingProvider();
        console = System.console();
        if (console != null) {
            reader = new BufferedReader(console.reader());
            writer = console.writer();
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(new OutputStreamWriter(System.out), true);  // autoflush
        }
    }


    static public void main(String[] args) {
        logger.entry((Object) args);

        logger.debug("Ensuring the data directory exists...");
        File directory = new File(dataDirectory);
        if (!directory.exists() && !directory.mkdir()) {
            logger.error("Unable to create ~/.cdt/ directory, exiting...");
            System.exit(1);
        }

        logger.debug("Configuring the CLI...");
        String digitalMarketplaceUriString = defaultDigitalAccountantUri;
        if (args.length > 0) {
            digitalMarketplaceUriString = args[0];
        }
        URI digitalMarketplaceUri = URI.create(digitalMarketplaceUriString);
        DigitalMarketplaceCli cli = new DigitalMarketplaceCli(digitalMarketplaceUri);

        logger.debug("Running the CLI...");
        int status = cli.run();

        logger.exit(status);
    }


    private int run() {

        if (console != null) printCommands();

        while (!done) {

            if (console != null) writer.format("%nEnter command: ");

            String line;
            try {
                line = reader.readLine();
                if (line == null) line = "quit";  // user closed the CLI
            } catch (IOException e) {
                logger.error("Unable to read the command from standard input: {}", e);
                return 1;
            }

            String[] values = line.split("\\s");
            if (values.length == 0) {
                if (console != null) printCommands();
                continue;
            }

            String command = values[0];
            Options options = new Options();
            try {
                switch (command) {

                    case "":
                        if (console != null) printCommands();
                        break;

                    case "register-identity":
                        registerIdentity(options, values);
                        break;

                    case "retrieve-identity":
                        retrieveIdentity(options, values);
                        break;

                    case "retrieve-identities":
                        retrieveIdentities(options, values);
                        break;

                    case "query-identities":
                        queryIdentities(options, values);
                        break;

                    case "retrieve-certificate":
                        retrieveCertificate(options, values);
                        break;

                    case "renew-certificate":
                        renewCertificate(options, values);
                        break;

                    case "certify-batch":
                        certifyBatch(options, values);
                        break;

                    case "retrieve-batch":
                        retrieveBatch(options, values);
                        break;

                    case "retrieve-token":
                        retrieveToken(options, values);
                        break;

                    case "transfer-token":
                        transferToken(options, values);
                        break;

                    case "retrieve-transaction":
                        retrieveTransaction(options, values);
                        break;

                    case "retrieve-ledger":
                        retrieveLedger(options, values);
                        break;

                    case "quit":
                        quit(options, values);
                        break;

                    default:
                        logger.error("Unknown command entered: {}", command);
                        if (console != null) printCommands();
                }

            } catch (ParseException e) {
                if (console != null) printHelp(command, options);
            } catch (IOException | ResourceAccessException e) {
                logger.error("There was a problem processing the command: {}", e);
            }
        }
        return 0;
    }


    private CommandLine parse(Options options, String[] arguments) throws ParseException {
        CommandLineParser parser = new DefaultParser();

        Option helpOption = new Option("help", false, "print this message");
        options.addOption(helpOption);

        // NOTE: this parse method may throw a different ParseException and miss the code below...
        CommandLine commandLine = parser.parse(options, arguments);
        if (commandLine == null || commandLine.hasOption(helpOption.getOpt())) {
            throw new ParseException(helpOption.getOpt());
        }
        return commandLine;
    }


    private char[] readPassword(String owner) throws IOException {
        char[] password;
        if (console != null) {
            password = console.readPassword("Enter notary key password for the " + owner + ": ");
        } else {
            password = reader.readLine().toCharArray();
        }
        return password;
    }


    static private void printHelp(String command, Options options) {
        new HelpFormatter().printHelp(command, options, true);
    }


    private void printCommands() {
        writer.println("Possible commands:");
        for (String command : commands) {
            writer.println("  " + command);
        }
    }


    private void registerIdentity(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(pseudonymOption);
        CommandLine commandLine = parse(options, values);
        String pseudonym = commandLine.getOptionValue("pseudonym");
        File notaryFile = new File(dataDirectory, pseudonym + "-notary-key.json");
        File identityFile = new File(dataDirectory, pseudonym + "-identity.json");

        logger.debug("Generating a new notary key...");
        char[] password = readPassword(pseudonym);
        if (password == null) return;
        NotaryKey notaryKey = notary.generateNotaryKey(digitalMarketplaceUri);
        mapper.writeValue(notaryFile, notaryKey);
        FileUtils.writeStringToFile(notaryFile, notary.serializeNotaryKey(notaryKey, password));
        Arrays.fill(password, ' ');

        logger.debug("Creating a new identity...");
        Map<String, Object> additionalAttributes = new LinkedHashMap<>();
        additionalAttributes.put("pseudonym", pseudonym);
        DigitalIdentity identity = identificationProvider.createIdentity(additionalAttributes, notaryKey);
        mapper.writeValue(identityFile, identity);

        logger.info("Registering the new identity...");
        RegisterIdentityRequest registerIdentityRequest = new RegisterIdentityRequest();
        registerIdentityRequest.identity = identity;
        registerIdentityRequest.certificate = notaryKey.verificationCertificate;
        RegisterIdentityResponse registerResponse = digitalMarketplaceClient.registerIdentity(registerIdentityRequest);
        logger.info("registerResponse: {}", registerResponse);
    }


    private void retrieveIdentity(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(identityOption);
        CommandLine commandLine = parse(options, values);
        String identityId = commandLine.getOptionValue("identity");
        String identityUri = digitalMarketplaceUri + "/identity/" + identityId;
        URI identityLocation = URI.create(identityUri);

        logger.info("Retrieving the identity...");
        RetrieveIdentityResponse retrieveResponse = digitalMarketplaceClient.retrieveIdentity(identityLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void retrieveIdentities(Options options, String[] values) throws ParseException, IOException {
        logger.info("Retrieving all identities...");
        QueryIdentitiesResponse queryResponse = digitalMarketplaceClient.queryAllIdentities();
        logger.info("retrieveResponse: {}", queryResponse);
    }


    private void queryIdentities(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(attributeOption);
        options.addOption(valueOption);
        CommandLine commandLine = parse(options, values);
        String attributeName = commandLine.getOptionValue("attribute");
        String attributeValue = commandLine.getOptionValue("value");

        logger.info("Querying for identities matching: {} = {}.", attributeName, attributeValue);
        QueryIdentitiesResponse queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute(attributeName, attributeValue);
        logger.info("queryResponse: {}", queryResponse);
    }


    private void retrieveCertificate(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(pseudonymOption);
        CommandLine commandLine = parse(options, values);
        String pseudonym = commandLine.getOptionValue("pseudonym");

        logger.debug("Retrieving the identity...");
        QueryIdentitiesResponse queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute("pseudonym", pseudonym);
        if (queryResponse.status != RequestStatus.Succeeded || queryResponse.identities.isEmpty()) {
            logger.error("Attempt to retrieve identity information failed: {}", queryResponse);
            return;
        }
        URI identityLocation = queryResponse.identities.get(0).attributes.myLocation;

        logger.info("Retrieving the latest certificate for the identity...");
        RetrieveCertificateResponse retrieveResponse = digitalMarketplaceClient.retrieveLatestCertificate(identityLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void renewCertificate(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(pseudonymOption);
        CommandLine commandLine = parse(options, values);
        String pseudonym = commandLine.getOptionValue("pseudonym");
        RenewCertificateRequest renewRequest = new RenewCertificateRequest();

        logger.debug("Retrieving the existing notary key...");
        char[] password = readPassword(pseudonym);
        File notaryFile = new File(dataDirectory, pseudonym + "-notary-key.json");
        if (!notaryFile.canRead()) {
            logger.error("The specified pseudonym does not exist: {}", pseudonym);
            return;
        }
        NotaryKey oldKey = notary.deserializeNotaryKey(FileUtils.readFileToString(notaryFile), password);

        logger.debug("Generating a new notary key...");
        NotaryKey newKey = notary.generateNotaryKey(digitalMarketplaceUri, oldKey);
        FileUtils.writeStringToFile(notaryFile, notary.serializeNotaryKey(newKey, password));
        Arrays.fill(password, ' ');

        logger.info("Renewing the certificate...");
        renewRequest.newCertificate = newKey.verificationCertificate;
        RenewCertificateResponse renewResponse = digitalMarketplaceClient.renewCertificate(renewRequest);
        logger.info("renewResponse: {}", renewResponse);
    }


    private void certifyBatch(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(merchantOption);
        options.addOption(accountantOption);
        options.addOption(typeOption);
        options.addOption(countOption);
        CommandLine commandLine = parse(options, values);
        String merchant = commandLine.getOptionValue("merchant");
        String accountant = commandLine.getOptionValue("accountant");
        String tokenType = commandLine.getOptionValue("type");
        int numberOfTokens = Integer.parseInt(commandLine.getOptionValue("count"));

        logger.debug("Retrieving the identity of the merchant...");
        QueryIdentitiesResponse queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute("pseudonym", merchant);
        if (queryResponse.status != RequestStatus.Succeeded || queryResponse.identities.isEmpty()) {
            logger.error("Attempt to retrieve merchant identity information failed: {}", queryResponse);
            return;
        }
        URI merchantLocation = queryResponse.identities.get(0).attributes.myLocation;

        logger.debug("Retrieving the identity of the accountant...");
        queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute("pseudonym", accountant);
        if (queryResponse.status != RequestStatus.Succeeded || queryResponse.identities.isEmpty()) {
            logger.error("Attempt to retrieve accountant identity information failed: {}", queryResponse);
            return;
        }
        URI accountantLocation = queryResponse.identities.get(0).attributes.myLocation;

        logger.debug("Retrieving the merchant's notary key...");
        char[] password = readPassword(merchant);
        File notaryFile = new File(dataDirectory, merchant + "-notary-key.json");
        if (!notaryFile.canRead()) {
            logger.error("The specified merchant does not exist: {}", merchant);
            return;
        }
        NotaryKey merchantKey = notary.deserializeNotaryKey(FileUtils.readFileToString(notaryFile), password);
        Arrays.fill(password, ' ');

        logger.debug("Minting the new tokens...");
        URI batchLocation = URI.create(digitalMarketplaceUri + "/batch/" + new Tag());
        List<DigitalToken> tokens = new ArrayList<>();
        for (int i = 1; i <= numberOfTokens; i++) {
            URI tokenLocation = URI.create(digitalMarketplaceUri + "/token/" + new Tag());
            DigitalToken token = tokenizationProvider.mintToken(tokenLocation, batchLocation,
                    accountantLocation, tokenType, Notarization.VALID_FOR_ONE_YEAR, merchantKey);
            tokens.add(token);
        }

        logger.info("Sending the digital accountant a request to certify the new tokens...");
        CertifyBatchRequest certifyRequest = new CertifyBatchRequest();
        certifyRequest.guarantorLocation = merchantLocation;
        certifyRequest.batchLocation = batchLocation;
        certifyRequest.tokens = tokens;
        CertifyBatchResponse certifyResponse = digitalMarketplaceClient.certifyBatch(certifyRequest);
        logger.info("certifyResponse: {}", certifyResponse);
    }


    private void retrieveBatch(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(batchOption);
        CommandLine commandLine = parse(options, values);
        String batchId = commandLine.getOptionValue("batch");
        String batchUri = digitalMarketplaceUri + "/batch/" + batchId;
        URI batchLocation = URI.create(batchUri);

        logger.info("Retrieving the batch of tokens...");
        RetrieveBatchResponse retrieveResponse = digitalMarketplaceClient.retrieveBatch(batchLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void retrieveToken(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(tokenOption);
        CommandLine commandLine = parse(options, values);
        String tokenId = commandLine.getOptionValue("token");
        String tokenUri = digitalMarketplaceUri + "/token/" + tokenId;
        URI tokenLocation = URI.create(tokenUri);

        logger.info("Retrieving the token...");
        RetrieveTokenResponse retrieveResponse = digitalMarketplaceClient.retrieveToken(tokenLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void transferToken(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(senderOption);
        options.addOption(receiverOption);
        options.addOption(tokenOption);
        CommandLine commandLine = parse(options, values);
        String sender = commandLine.getOptionValue("sender");
        String receiver = commandLine.getOptionValue("receiver");
        String tokenId = commandLine.getOptionValue("token");

        logger.debug("Retrieving the identity of the sender...");
        QueryIdentitiesResponse queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute("pseudonym", sender);
        if (queryResponse.status != RequestStatus.Succeeded || queryResponse.identities.isEmpty()) {
            logger.error("Attempt to retrieve sender identity information failed: {}", queryResponse);
            return;
        }
        URI senderLocation = queryResponse.identities.get(0).attributes.myLocation;

        logger.debug("Retrieving the identity of the receiver...");
        queryResponse = digitalMarketplaceClient.queryIdentitiesByAttribute("pseudonym", receiver);
        if (queryResponse.status != RequestStatus.Succeeded || queryResponse.identities.isEmpty()) {
            logger.error("Attempt to retrieve receiver identity information failed: {}", queryResponse);
            return;
        }
        URI receiverLocation = queryResponse.identities.get(0).attributes.myLocation;

        logger.debug("Retrieving the sender's notary key...");
        char[] password = readPassword(sender);
        File notaryFile = new File(dataDirectory, sender + "-notary-key.json");
        if (!notaryFile.canRead()) {
            logger.error("The specified sender does not exist: {}", sender);
            return;
        }
        NotaryKey senderKey = notary.deserializeNotaryKey(FileUtils.readFileToString(notaryFile), password);
        Arrays.fill(password, ' ');

        logger.debug("Retrieving the receiver's notary key...");
        password = readPassword(receiver);
        notaryFile = new File(dataDirectory, receiver + "-notary-key.json");
        if (!notaryFile.canRead()) {
            logger.error("The specified receiver does not exist:{} ", receiver);
            return;
        }
        NotaryKey receiverKey = notary.deserializeNotaryKey(FileUtils.readFileToString(notaryFile), password);
        Arrays.fill(password, ' ');

        logger.debug("Retrieving the token...");
        String tokenUri = digitalMarketplaceUri + "/token/" + tokenId;
        URI tokenLocation = URI.create(tokenUri);
        RetrieveTokenResponse retrieveTokenResponse = digitalMarketplaceClient.retrieveToken(tokenLocation);
        if (retrieveTokenResponse.status != RequestStatus.Succeeded) {
            logger.error("Attempt to retrieve token failed: {}", retrieveTokenResponse);
            return;
        }
        DigitalToken token = retrieveTokenResponse.token;

        logger.info("Transfering the token from the sender to the receiver...");
        URI transactionLocation = URI.create(digitalMarketplaceUri + "/transaction/" + new Tag());
        DigitalTransaction transaction = accountingProvider.initiateTransaction(transactionLocation, senderLocation, receiverLocation, "Token Transfer", token, senderKey);
        URI certificateLocation = transaction.senderSeal.attributes.verificationCitation.documentLocation;
        RetrieveCertificateResponse retrieveCertificateResponse = digitalMarketplaceClient.retrieveCertificate(certificateLocation);
        if (retrieveCertificateResponse.status != RequestStatus.Succeeded) {
            logger.error("Attempt to retrieve sender certificate failed: {}", retrieveCertificateResponse);
            return;
        }
        NotaryCertificate senderCertificate = retrieveCertificateResponse.certificate;
        accountingProvider.approveTransaction(transaction, token, senderCertificate, receiverKey);

        logger.info("Sending the digital accountant a request to certify the transaction...");
        RecordTransactionRequest recordRequest = new RecordTransactionRequest();
        recordRequest.transaction = transaction;
        RecordTransactionResponse recordResponse = digitalMarketplaceClient.recordTransaction(recordRequest);
        logger.info("recordResponse: {}", recordResponse);
    }


    private void retrieveTransaction(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(transactionOption);
        CommandLine commandLine = parse(options, values);
        String transactionId = commandLine.getOptionValue("transaction");

        logger.info("Retrieving the transaction...");
        String transactionUri = digitalMarketplaceUri + "/transaction/" + transactionId;
        URI transactionLocation = URI.create(transactionUri);
        RetrieveTransactionResponse retrieveResponse = digitalMarketplaceClient.retrieveTransaction(transactionLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void retrieveLedger(Options options, String[] values) throws ParseException, IOException {
        logger.debug("Parsing the command line options...");
        options.addOption(ledgerOption);
        CommandLine commandLine = parse(options, values);
        String ledgerId = commandLine.getOptionValue("ledger");

        logger.info("Retrieving the ledger...");
        String ledgerUri = digitalMarketplaceUri + "/ledger/" + ledgerId;
        URI ledgerLocation = URI.create(ledgerUri);
        RetrieveLedgerResponse retrieveResponse = digitalMarketplaceClient.retrieveLedger(ledgerLocation);
        logger.info("retrieveResponse: {}", retrieveResponse);
    }


    private void quit(Options options, String[] values) throws ParseException, IOException {
        done = true;
        if (console != null) writer.format("Goodbye!%n");
    }

}
