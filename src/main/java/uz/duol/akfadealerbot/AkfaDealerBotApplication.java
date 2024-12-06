package uz.duol.akfadealerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
public class AkfaDealerBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AkfaDealerBotApplication.class, args);
    }
}
