package sk.krizan.fitness_app_be.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private static final String STARTED_REGISTERING_USERS = "%s - Started registering users";
    private static final String FINISHED_REGISTERING_USERS = "%s - Finished registering users";

    @Override
    public void run(String... args) {
        registerUsers();
    }

    private void registerUsers() {
        log.info(STARTED_REGISTERING_USERS.formatted(getClass().getSimpleName()));

        log.info(FINISHED_REGISTERING_USERS.formatted(getClass().getSimpleName()));
    }
}
