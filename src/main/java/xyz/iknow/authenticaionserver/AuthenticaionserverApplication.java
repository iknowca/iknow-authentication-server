package xyz.iknow.authenticaionserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.PriorityQueue;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AuthenticaionserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticaionserverApplication.class, args);
	}

}
