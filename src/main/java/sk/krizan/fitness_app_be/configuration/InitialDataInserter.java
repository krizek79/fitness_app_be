package sk.krizan.fitness_app_be.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.model.entity.Profile;
import sk.krizan.fitness_app_be.model.entity.User;
import sk.krizan.fitness_app_be.model.enums.Role;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;
import sk.krizan.fitness_app_be.repository.ProfileRepository;
import sk.krizan.fitness_app_be.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataInserter implements CommandLineRunner {

    @Value("${server.port}")
    private String serverPort;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Value("${inserter.enabled}")
    private Boolean inserterEnabled;

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    private static final String STARTED_INSERTING_SAMPLE_DATA = "%s - Started inserting sample data.";
    private static final String FINISHED_INSERTING_SAMPLE_DATA = "%s - Finished inserting sample data.";
    private static final String OPENAPI_MESSAGE = "Link to the OpenApi documentation: %s";

    @Override
    public void run(String... args) {
        printOpenApiUrl();

        if (inserterEnabled) {
            insertSampleData();
        }
    }

    private void printOpenApiUrl() {
        String swaggerUrl = "http://localhost:" + serverPort + swaggerPath;
        log.info(OPENAPI_MESSAGE.formatted(swaggerUrl));
    }

    private void insertSampleData() {
        log.info(STARTED_INSERTING_SAMPLE_DATA.formatted(getClass().getSimpleName()));
        try (Connection connection = dataSource.getConnection()) {
            insertUserIfNotExists("john.doe@example.com", "John Doe", "Lifter and coach", WeightUnit.KG);
            insertUserIfNotExists("jane.smith@example.com", "Jane Smith", "Fitness enthusiast", WeightUnit.LB);

            Resource resource = resourceLoader.getResource("classpath:sample_data/data.sql");
            ScriptUtils.executeSqlScript(connection, resource);
            log.info(FINISHED_INSERTING_SAMPLE_DATA.formatted(getClass().getSimpleName()));
        } catch (Exception e) {
            log.error("Error inserting sample data", e);
        }
    }

    private void insertUserIfNotExists(String email, String name, String bio, WeightUnit weightUnit) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {
                    user.setPassword(passwordEncoder.encode("pass"));
                    userRepository.save(user);
                },
                () -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(passwordEncoder.encode("pass"));
                    user.setActive(true);
                    user.setLocked(false);
                    user.setEnabled(true);
                    user.setCredentialsNonExpired(true);
                    user.setCreatedAt(Instant.now());
                    user.setUpdatedAt(Instant.now());
                    user.addToRoleSet(Set.of(Role.USER));
                    userRepository.save(user);

                    Profile profile = new Profile();
                    profile.setUser(user);
                    profile.setName(name);
                    profile.setBio(bio);
                    profile.setPreferredWeightUnit(weightUnit);
                    profile.setDeleted(false);
                    profileRepository.save(profile);

                    user.setProfile(profile);
                    userRepository.save(user);
                }
        );
    }
}
