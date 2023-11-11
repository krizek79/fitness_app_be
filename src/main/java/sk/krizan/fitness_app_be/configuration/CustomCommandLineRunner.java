package sk.krizan.fitness_app_be.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCommandLineRunner implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    private static final String STARTED_REGISTERING_USERS = "%s - Started registering users";
    private static final String FINISHED_REGISTERING_USERS = "%s - Finished registering users";
    private static final String OPENAPI_MESSAGE =
        "Link to the OpenApi documentation: %s";

    @Override
    public void run(String... args) {
        printOpenApiUrl();
        registerUsers();
    }

    private void printOpenApiUrl() {
        String swaggerUrl = "http://localhost:" + serverPort + swaggerPath;
        log.info(OPENAPI_MESSAGE.formatted(swaggerUrl));
    }

    private void registerUsers() {
//        log.info(STARTED_REGISTERING_USERS.formatted(getClass().getSimpleName()));

//        log.info(FINISHED_REGISTERING_USERS.formatted(getClass().getSimpleName()));
    }
}
