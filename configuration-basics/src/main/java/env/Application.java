package env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

// <1>
@Configuration
@PropertySource("some.properties")
public class Application {

	private Log log = LogFactory.getLog(getClass());

	@Value("${configuration.projectName}")
	private String configurationProjectNameField;

	public static void main(String[] args) throws Throwable {
		new AnnotationConfigApplicationContext(Application.class);
	}

	// <2>
	@Bean
	static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	// <3>
	@Value("${configuration.projectName}")
	void setProjectName(String projectName) {
		log.info("setProjectName: " + projectName);
	}

	// <4>
	@Autowired
	void setEnvironment(Environment env) {
		log.info("setEnvironment: " + env.getProperty("configuration.projectName"));
	}

	@PostConstruct
	void afterPropertiesSet() throws Throwable {
		log.info("configurationProjectNameField: " + this.configurationProjectNameField);
	}

	// <5>
	@Bean
	InitializingBean both(Environment env,
			@Value("${configuration.projectName}") String projectName) {
		return () -> {
			log.info("@Bean with both dependencies (projectName): " + projectName);
			log.info("@Bean with both dependencies (env): "
					+ env.getProperty("configuration.projectName"));
		};
	}
}
