package nl.brandfonds.Brandfonds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@EnableWebMvc
@SpringBootApplication
public class BrandfondsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandfondsApplication.class, args);
    }
}
