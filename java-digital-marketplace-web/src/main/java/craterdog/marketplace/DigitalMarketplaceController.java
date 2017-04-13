package craterdog.marketplace;

import craterdog.primitives.Tag;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/DigitalMarketplace")
public class DigitalMarketplaceController {

    static private final XLogger logger = XLoggerFactory.getXLogger(DigitalMarketplaceController.class);

    private final IdentityManagement identityManagementService;

    private final TokenManagement tokenManagementService;

    @Autowired
    public DigitalMarketplaceController(IdentityManagement identityManagementService, TokenManagement tokenManagementService) {
        this.identityManagementService = identityManagementService;
        this.tokenManagementService = tokenManagementService;
    }

    @Consumes("application/json")
    @Produces("application/json")
    @RequestMapping(value = "/identity", method = RequestMethod.POST)
    public RegisterIdentityResponse registerIdentity(@RequestBody RegisterIdentityRequest request) {
        logger.info("Processing a register identity request: {}", request);
        return identityManagementService.registerIdentity(request);
    }

    @Produces("application/json")
    @RequestMapping(value = "/identity/{identityId}", method = RequestMethod.GET)
    public RetrieveIdentityResponse retrieveIdentity(@RequestHeader("Host") String host, @PathVariable Tag identityId) {
        URI identityLocation = generateLocation(host, "identity", identityId);
        logger.info("Retrieving the identity for: {}", identityLocation);
        return identityManagementService.retrieveIdentity(identityLocation);
    }

    @Produces("application/json")
    @RequestMapping(value = "/identities/all", method = RequestMethod.GET)
    public QueryIdentitiesResponse queryAllIdentities() {
        logger.info("Querying all identities...");
        return identityManagementService.queryAllIdentities();
    }

    @Produces("application/json")
    @RequestMapping(value = "/identities", method = RequestMethod.GET)
    public QueryIdentitiesResponse queryIdentitiesByAttribute(@RequestParam String name, @RequestParam Object value) {
        logger.info("Querying identities with attribute: {} equal to {}", name, value);
        return identityManagementService.queryIdentitiesByAttribute(name, value);
    }

    @Produces("application/json")
    @RequestMapping(value = "/identity/{identityId}/certificate", method = RequestMethod.GET)
    public RetrieveCertificateResponse retrieveLatestCertificate(@RequestHeader("Host") String host, @PathVariable Tag identityId) {
        URI identityLocation = generateLocation(host, "identity", identityId);
        logger.info("Retrieving the latest certificate for: {}", identityLocation);
        return identityManagementService.retrieveLatestCertificate(identityLocation);
    }

    @Produces("application/json")
    @RequestMapping(value = "/certificate/{certificateId}", method = RequestMethod.GET)
    public RetrieveCertificateResponse retrieveCertificate(@RequestHeader("Host") String host, @PathVariable Tag certificateId) {
        URI certificateLocation = generateLocation(host, "certificate", certificateId);
        logger.info("Retrieving the certificate: {}", certificateLocation);
        return identityManagementService.retrieveCertificate(certificateLocation);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    public RenewCertificateResponse renewCertificate(@RequestBody RenewCertificateRequest request) {
        logger.info("Processing a renew certificate request: {}", request);
        return identityManagementService.renewCertificate(request);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public CertifyBatchResponse certifyBatch(@RequestBody CertifyBatchRequest request) {
        logger.info("Processing a certify batch request: {}", request);
        return tokenManagementService.certifyBatch(request);
    }

    @Consumes("application/json")
    @Produces("application/json")
    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public RecordTransactionResponse recordTransaction(@RequestBody RecordTransactionRequest request) {
        logger.info("Processing a record transaction request: {}", request);
        return tokenManagementService.recordTransaction(request);
    }

    @Produces("application/json")
    @RequestMapping(value = "/token/{tokenId}", method = RequestMethod.GET)
    public RetrieveTokenResponse retrieveToken(@RequestHeader("Host") String host, @PathVariable Tag tokenId) {
        URI tokenLocation = generateLocation(host, "token", tokenId);
        logger.info("Retrieving the token: {}", tokenLocation);
        return tokenManagementService.retrieveToken(tokenLocation);
    }

    @Produces("application/json")
    @RequestMapping(value = "/batch/{batchId}", method = RequestMethod.GET)
    public RetrieveBatchResponse retrieveBatch(@RequestHeader("Host") String host, @PathVariable Tag batchId) {
        URI batchLocation = generateLocation(host, "batch", batchId);
        logger.info("Retrieving the batch: {}", batchLocation);
        return tokenManagementService.retrieveBatch(batchLocation);
    }

    @Produces("application/json")
    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET)
    public RetrieveTransactionResponse retrieveTransaction(@RequestHeader("Host") String host, @PathVariable Tag transactionId) {
        URI transactionLocation = generateLocation(host, "transaction", transactionId);
        logger.info("Retrieving the transaction: {}", transactionLocation);
        return tokenManagementService.retrieveTransaction(transactionLocation);
    }

    @Produces("application/json")
    @RequestMapping(value = "/ledger/{ledgerId}", method = RequestMethod.GET)
    public RetrieveLedgerResponse retrieveLedger(@RequestHeader("Host") String host, @PathVariable Tag ledgerId) {
        URI ledgerLocation = generateLocation(host, "ledger", ledgerId);
        logger.info("Retrieving the ledger: {}", ledgerLocation);
        return tokenManagementService.retrieveLedger(ledgerLocation);
    }

    private URI generateLocation(String host, String type, Tag tag) {
        String locationString = "http://" + host + "/DigitalMarketplace/" + type + "/" + tag;
        URI location = URI.create(locationString);
        return location;
    }

}
