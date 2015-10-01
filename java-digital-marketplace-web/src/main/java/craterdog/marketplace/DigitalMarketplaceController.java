package craterdog.marketplace;

import craterdog.primitives.Tag;
import java.net.URI;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/DigitalMarketplace")
public class DigitalMarketplaceController {

    static private final XLogger logger = XLoggerFactory.getXLogger(DigitalMarketplaceController.class);

    @Autowired private IdentityManagement identityManagementService;

    @Autowired private TokenManagement tokenManagementService;

    @RequestMapping(value = "/identity", method = RequestMethod.POST)
    public @ResponseBody RegisterIdentityResponse registerIdentity(@RequestBody RegisterIdentityRequest request) {
        logger.info("Processing a register identity request: {}", request);
        return identityManagementService.registerIdentity(request);
    }

    @RequestMapping(value = "/identity/{identityId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveIdentityResponse retrieveIdentity(@RequestHeader("Host") String host, @PathVariable Tag identityId) {
        URI identityLocation = generateLocation(host, "identity", identityId);
        logger.info("Retrieving the identity for: {}", identityLocation);
        return identityManagementService.retrieveIdentity(identityLocation);
    }

    @RequestMapping(value = "/identities/all", method = RequestMethod.GET)
    public @ResponseBody QueryIdentitiesResponse queryAllIdentities() {
        logger.info("Querying all identities...");
        return identityManagementService.queryAllIdentities();
    }

    @RequestMapping(value = "/identities", method = RequestMethod.GET)
    public @ResponseBody QueryIdentitiesResponse queryIdentitiesByAttribute(@RequestParam String name, @RequestParam Object value) {
        logger.info("Querying identities with attribute: {} equal to {}", name, value);
        return identityManagementService.queryIdentitiesByAttribute(name, value);
    }

    @RequestMapping(value = "/identity/{identityId}/certificate", method = RequestMethod.GET)
    public @ResponseBody RetrieveCertificateResponse retrieveLatestCertificate(@RequestHeader("Host") String host, @PathVariable Tag identityId) {
        URI identityLocation = generateLocation(host, "identity", identityId);
        logger.info("Retrieving the latest certificate for: {}", identityLocation);
        return identityManagementService.retrieveLatestCertificate(identityLocation);
    }

    @RequestMapping(value = "/certificate/{certificateId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveCertificateResponse retrieveCertificate(@RequestHeader("Host") String host, @PathVariable Tag certificateId) {
        URI certificateLocation = generateLocation(host, "certificate", certificateId);
        logger.info("Retrieving the certificate: {}", certificateLocation);
        return identityManagementService.retrieveCertificate(certificateLocation);
    }

    @RequestMapping(value = "/certificate", method = RequestMethod.POST)
    public @ResponseBody RenewCertificateResponse renewCertificate(@RequestBody RenewCertificateRequest request) {
        logger.info("Processing a renew certificate request: {}", request);
        return identityManagementService.renewCertificate(request);
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public @ResponseBody CertifyBatchResponse certifyBatch(@RequestBody CertifyBatchRequest request) {
        logger.info("Processing a certify batch request: {}", request);
        return tokenManagementService.certifyBatch(request);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public @ResponseBody RecordTransactionResponse recordTransaction(@RequestBody RecordTransactionRequest request) {
        logger.info("Processing a record transaction request: {}", request);
        return tokenManagementService.recordTransaction(request);
    }

    @RequestMapping(value = "/token/{tokenId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveTokenResponse retrieveToken(@RequestHeader("Host") String host, @PathVariable Tag tokenId) {
        URI tokenLocation = generateLocation(host, "token", tokenId);
        logger.info("Retrieving the token: {}", tokenLocation);
        return tokenManagementService.retrieveToken(tokenLocation);
    }

    @RequestMapping(value = "/batch/{batchId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveBatchResponse retrieveBatch(@RequestHeader("Host") String host, @PathVariable Tag batchId) {
        URI batchLocation = generateLocation(host, "batch", batchId);
        logger.info("Retrieving the batch: {}", batchLocation);
        return tokenManagementService.retrieveBatch(batchLocation);
    }

    @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveTransactionResponse retrieveTransaction(@RequestHeader("Host") String host, @PathVariable Tag transactionId) {
        URI transactionLocation = generateLocation(host, "transaction", transactionId);
        logger.info("Retrieving the transaction: {}", transactionLocation);
        return tokenManagementService.retrieveTransaction(transactionLocation);
    }

    @RequestMapping(value = "/ledger/{ledgerId}", method = RequestMethod.GET)
    public @ResponseBody RetrieveLedgerResponse retrieveLedger(@RequestHeader("Host") String host, @PathVariable Tag ledgerId) {
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
