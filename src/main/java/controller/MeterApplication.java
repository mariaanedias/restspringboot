package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeterApplication {

	/*No web.xml. 
	This web application is 100% pure Java and you didn’t have to deal with configuring 
	any plumbing or infrastructure. */
	
	//Make the application executable - gradlew bootRun
    public static void main(String[] args) {
        SpringApplication.run(MeterApplication.class, args);
    }
}

/*
@SpringBootApplication is a convenience annotation that adds all of the following:

@Configuration tags the class as a source of bean definitions for the application context.

@EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, 
other beans, and various property settings.

Normally you would add @EnableWebMvc for a Spring MVC app, 
but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. 
This flags the application as a web application and activates key behaviors such 
as setting up a DispatcherServlet.

@ComponentScan tells Spring to look for other components, configurations, and services in the 
hello package, allowing it to find the controllers.

*/