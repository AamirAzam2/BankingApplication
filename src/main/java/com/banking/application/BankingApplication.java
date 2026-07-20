package main.java.com.banking.application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Banking App",
                description = "Backend Rest APIs for Banking App",
                version = "v1.0",
                contact = @Contact(
                        name = "Aamir Azam",
                        email = "aamir.azam2@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Banking App Documentation"
        )
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
