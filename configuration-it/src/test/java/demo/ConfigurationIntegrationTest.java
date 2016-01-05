package demo;

import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CloudFoundryClientConfiguration.class)
public class ConfigurationIntegrationTest {

    private Log log = LogFactory.getLog(getClass());

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private CloudFoundryClient cloudFoundryClient;

    @Test
    public void clientIsConnectedToService() throws Exception {

        this.cloudFoundryClient
                .getApplications()
                .stream()
                .filter(ca -> ca.getName().equals("configuration-client"))
                .map(ca -> ca.getUris().stream().findFirst())
                .findFirst()
                .orElseThrow(AssertionFailedError::new)
                .ifPresent(uri -> {
                    log.info("the application is running at " + uri);

                    String url = "http://" + uri + "/project-name";
                    log.info("url: " + url);
                    ResponseEntity<String> entity = this.restTemplate
                            .getForEntity(url, String.class);
                    assertEquals(entity.getStatusCode(), HttpStatus.OK);
                    assertTrue(entity.getBody().contains("Spring Cloud"));
                });

    }
}

@SpringBootApplication
class CloudFoundryClientConfiguration {

    @Bean
    CloudCredentials cloudCredentials(
            @Value("${cf.user}") String email, @Value("${cf.password}") String pw) {
        return new CloudCredentials(email, pw);
    }

    @Bean
    CloudFoundryClient cloudFoundryClient(@Value("${cf.api}") String url,
                                          CloudCredentials cc) throws MalformedURLException {
        URI uri = URI.create(url);
        CloudFoundryClient cloudFoundryClient = new CloudFoundryClient(cc, uri.toURL());
        cloudFoundryClient.login();
        return cloudFoundryClient;
    }
}
