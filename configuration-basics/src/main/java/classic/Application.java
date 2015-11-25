package classic;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classic.xml");
    }

    public void setConfigurationProjectName(String pn) {
        System.out.println("the configuration project name is " + pn);
    }
}