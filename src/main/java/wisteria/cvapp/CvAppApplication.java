package wisteria.cvapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("wisteria.cvapp.repository")
@EntityScan("wisteria.cvapp.model")
public class CvAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CvAppApplication.class, args);
    }

}
