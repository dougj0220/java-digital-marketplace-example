package craterdog.marketplace;

import com.fasterxml.jackson.databind.ObjectMapper;
import craterdog.notary.Notarization;
import craterdog.notary.NotaryKey;
import craterdog.notary.V1NotarizationProvider;
import craterdog.notary.mappers.NotaryModule;
import craterdog.smart.SmartObjectMapper;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@SpringBootApplication
public class DigitalMarketplaceConfiguration {

    @Value("${digitalmarketplace.url:http://localhost:8080/DigitalMarketplace}")
    private String digitalMarketplaceUrlString;

    public static void main(String[] args) {
        SpringApplication.run(DigitalMarketplaceConfiguration.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
        SmartObjectMapper mapper = new SmartObjectMapper();
        // handle conversion of public keys in certificates
        mapper.registerModule(new NotaryModule());
        return mapper;
    }

    @Bean
    public IdentityManagement getIdentityManagement() {
        return new IdentityManagementService();
    }

    @Bean
    public TokenManagement getTokenManagement() {
        Notarization notarizationProvider = new V1NotarizationProvider();
        URI digitalMarketplaceUri = URI.create(digitalMarketplaceUrlString);
        NotaryKey notaryKey = notarizationProvider.generateNotaryKey(digitalMarketplaceUri);
        return new TokenManagementService(getIdentityManagement(), notaryKey, "digital-accountant");
    }

}
