package env;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

// <1>
@Configuration
@PropertySource("some.properties")
public class Application {

	public static void main(String[] args) throws Throwable {
		new AnnotationConfigApplicationContext(Application.class);
	}

	// <2>
	@Bean
	static PropertySourcesPlaceholderConfigurer pspc() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Value("${configuration.projectName}")
	private String configurationProjectNameField;

	// <3>
	@Value("${configuration.projectName}")
	void setProjectName(String projectName) {
		System.out.println("setProjectName: " + projectName);
	}

	// <4>
	@Autowired
	void setEnvironment(Environment env) {
		System.out.println("setEnvironment: "
				+ env.getProperty("configuration.projectName"));
	}

	@PostConstruct
	void afterPropertiesSet() throws Throwable {
		System.out.println("configurationProjectNameField: "
				+ this.configurationProjectNameField);
	}

	// <5>
	@Bean
	InitializingBean both(Environment env,
			@Value("${configuration.projectName}") String projectName) {
		return () -> {
			System.out.println("@Bean with both dependencies (projectName): "
					+ projectName);
			System.out.println("@Bean with both dependencies (env): "
					+ env.getProperty("configuration.projectName"));
		};
	}
}
