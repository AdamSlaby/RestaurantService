package pl.restaurant.supplyservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@OpenAPIDefinition(info =
    @Info(title = "Supply API", version = "1.0", description = "Documentation Supply API v1.0")
)
public class SupplyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplyServiceApplication.class, args);
    }

}
