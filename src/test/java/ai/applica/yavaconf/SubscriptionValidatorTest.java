package ai.applica.yavaconf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
@Transactional
public class SubscriptionValidatorTest extends IntegrationTest {
    @Autowired
    SubscriptionValidator subscriptionValidator;

    @Container
    static GenericContainer validatorService = new GenericContainer<>("validation")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/").forPort(8080))
            .withStartupTimeout(Duration.ofSeconds(5));

    @Container
    static GenericContainer subscriptionService = new GenericContainer<>("subscription")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/").forPort(8080))
            .withStartupTimeout(Duration.ofSeconds(5));

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("subscriptionUrl", () -> String.format("http://%s:%d/",
                subscriptionService.getHost(), subscriptionService.getMappedPort(8080)));
        registry.add("validationUrl", () -> String.format("http://%s:%d/",
                validatorService.getHost(), validatorService.getMappedPort(8080)));
    }

    @Test
    void shouldGetSubscriptionIdAndValidateIt() {
        Boolean subscriptionValid = subscriptionValidator.validate(Person.builder().name("Jan Kowalski").build());
        assertThat(subscriptionValid).isTrue();
    }
}
