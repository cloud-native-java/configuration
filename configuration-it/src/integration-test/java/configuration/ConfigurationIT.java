package configuration;

import cnj.CloudFoundryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ConfigurationIT.Config.class)
public class ConfigurationIT {

 @SpringBootApplication
 public static class Config {
 }

 private Log log = LogFactory.getLog(getClass());

 private RestTemplate restTemplate = new RestTemplate();

 @Autowired
 private CloudFoundryService service;

 @Before
 public void before() throws Throwable {

  File root = new File(".");
  File configClientManifest = new File(root,
   "../configuration-client/manifest.yml");
  File configServiceManifest = new File(root,
   "../configuration-service/manifest.yml");

  String rmqService = "rabbitmq-bus";
  this.service.createServiceIfMissing("cloudamqp", "lemur", rmqService);
  this.service
   .pushApplicationAndCreateUserDefinedServiceUsingManifest(configServiceManifest);
  this.service.pushApplicationUsingManifest(configClientManifest);
 }

 @Test
 public void clientIsConnectedToService() throws Exception {

  String configClientUrl = this.service
   .urlForApplication("configuration-client");
  log.info("the application is running at " + configClientUrl);
  String url = configClientUrl + "/project-name";
  log.info("url: " + url);
  ResponseEntity<String> entity = this.restTemplate.getForEntity(url,
   String.class);
  assertEquals(entity.getStatusCode(), HttpStatus.OK);
  assertTrue(entity.getBody().contains("Spring Cloud"));
 }
}
