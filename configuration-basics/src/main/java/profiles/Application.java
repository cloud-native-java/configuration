package profiles;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
public class Application {

    @Bean
    static PropertySourcesPlaceholderConfigurer pspc() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Configuration
    @Profile("prod")  // <1>
    @PropertySource("some-prod.properties")
    static class ProdConfiguration {

        @Bean
        InitializingBean init() {
            return () -> System.out.println("prod InitializingBean");
        }
    }

    @Configuration
    @Profile({"default", "dev"})  // <2>
    @PropertySource("some.properties")
    static class DefaultConfiguration {

        @Bean
        InitializingBean init() {
            return () -> System.out.println("default InitializingBean");
        }
    }

    // <3>
    @Bean
    InitializingBean which(Environment e, @Value("${configuration.projectName}") String projectName) {
        return () -> {
            System.out.println("activeProfiles: '" +
                    StringUtils.arrayToCommaDelimitedString(
                            e.getActiveProfiles()) + "'");
            System.out.println("configuration.projectName: "
                    + projectName);
        };
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("dev"); // <4>
        ac.register(Application.class);
        ac.refresh();
    }

}
