package ai.applica.yavaconf;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SubscriptionValidator {
    private static final RestTemplate restTemplate = new RestTemplate();
    @Value("${subscriptionUrl}")
    String subscriptionService = "http://localhost:8080/";
    @Value("${validationUrl}")
    String validationService = "http://localhost:8081/";

    public Boolean validate(Person person) {
        ResponseEntity<String> response = restTemplate.getForEntity(subscriptionService + person.getId(), String.class);
        return restTemplate.getForEntity(validationService + response.getBody(), Boolean.class).getBody();
    }
}
