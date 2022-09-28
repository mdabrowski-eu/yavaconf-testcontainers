package ai.applica.yavaconf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTest extends IntegrationTest {
    @Autowired
    PersonRepository personRepository;

    @Test
    void shouldSaveAndFindPerson() {
        String id = UUID.randomUUID().toString();
        personRepository.save(Person.builder()
                                .id(id)
                                .name("Jan Kowalski")
                                .build());
        assertThat(personRepository.findById(id).get().getName()).isEqualTo("Jan Kowalski");
    }
}
