package boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties // <1>
public class Application {

	@Autowired
	void setConfigurationProjectProperties(ConfigurationProjectProperties cp) {
		System.out.println("configurationProjectProperties.projectName = "
				+ cp.getProjectName());
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

}

// <2>
@Component
@ConfigurationProperties("configuration")
class ConfigurationProjectProperties {

	private String projectName; // <3>

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
