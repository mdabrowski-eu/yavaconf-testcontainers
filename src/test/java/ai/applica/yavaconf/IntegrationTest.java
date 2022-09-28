package ai.applica.yavaconf;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.transaction.Transactional;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@SpringBootTest
@Transactional
public abstract class IntegrationTest {

    private static final String POSTGRES_IMAGE = "postgres:12";

    protected static GenericContainer testPostgres;

    static {
        testPostgres = new GenericContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                .withNetworkAliases("postgres")
                .withExposedPorts(5432)
                .waitingFor(new LogMessageWaitStrategy()
                        .withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(2)
                        .withStartupTimeout(Duration.of(60, SECONDS)))
                .withEnv("POSTGRES_USER", "test")
                .withEnv("POSTGRES_PASSWORD", "secret")
                .withEnv("POSTGRES_DB", "test");
        testPostgres.start();
    }

    @DynamicPropertySource
    static void testPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> String.format("jdbc:postgresql://%s:%d/test",
                testPostgres.getHost(), testPostgres.getMappedPort(5432)));
    }
}

