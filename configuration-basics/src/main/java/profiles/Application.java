package profiles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class Application {

 private Log log = LogFactory.getLog(getClass());

 @Bean
 static PropertySourcesPlaceholderConfigurer pspc() {
  return new PropertySourcesPlaceholderConfigurer();
 }

 // <1>
 @Configuration
 @Profile("prod")
 @PropertySource("some-prod.properties")
 public static class ProdConfiguration {

  @Bean
  InitializingBean init() {
   return () -> LogFactory.getLog(getClass()).info("prod InitializingBean");
  }
 }

 @Configuration
 @Profile({ "default", "dev" })
 // <2>
 @PropertySource("some.properties")
 public static class DefaultConfiguration {

  @Bean
  InitializingBean init() {
   return () -> LogFactory.getLog(getClass()).info("default InitializingBean");
  }
 }

 // <3>
 @Bean
 InitializingBean which(Environment e,
                        @Value("${configuration.projectName}") String projectName) {
  return () -> {
   log.info("activeProfiles: '"
           + StringUtils.arrayToCommaDelimitedString(e.getActiveProfiles()) + "'");
   log.info("configuration.projectName: " + projectName);
  };
 }

 public static void main(String[] args) {
  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
  ac.getEnvironment().setActiveProfiles("dev"); // <4>
  ac.register(Application.class);
  ac.refresh();
 }
}
