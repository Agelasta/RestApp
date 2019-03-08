package pl.stanczyk.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "pl.stanczyk")
@PropertySource("file:properties/path.properties")
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
