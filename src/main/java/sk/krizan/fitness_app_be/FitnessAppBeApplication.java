package sk.krizan.fitness_app_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FitnessAppBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessAppBeApplication.class, args);
    }

}
