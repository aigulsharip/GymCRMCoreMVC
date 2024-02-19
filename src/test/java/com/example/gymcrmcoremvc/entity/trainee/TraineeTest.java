package com.example.gymcrmcoremvc.entity.trainee;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

public class TraineeTest {

    @Test
    public void testTraineeConstructor() {
        // Create a Trainee object
        Trainee trainee = new Trainee(1L, "John", "Doe", "johndoe", "password", true, LocalDate.of(1990, 1, 1), "123 Street", null);

        // Verify that the object is constructed correctly
        assertThat(trainee).isNotNull();
        assertThat(trainee.getId()).isEqualTo(1L);
        assertThat(trainee.getFirstName()).isEqualTo("John");
        assertThat(trainee.getLastName()).isEqualTo("Doe");
        assertThat(trainee.getUsername()).isEqualTo("johndoe");
        assertThat(trainee.getPassword()).isEqualTo("password");
        assertThat(trainee.getIsActive()).isTrue();
        assertThat(trainee.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(trainee.getAddress()).isEqualTo("123 Street");
        assertThat(trainee.getTrainers()).isNull();
    }

    @Test
    public void testGettersAndSetters() {
        // Create a Trainee object
        Trainee trainee = new Trainee();

        // Set values using setters
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("johndoe");
        trainee.setPassword("password");
        trainee.setIsActive(true);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Street");
        trainee.setTrainers(null);

        // Verify that getters return the correct values
        assertThat(trainee.getId()).isEqualTo(1L);
        assertThat(trainee.getFirstName()).isEqualTo("John");
        assertThat(trainee.getLastName()).isEqualTo("Doe");
        assertThat(trainee.getUsername()).isEqualTo("johndoe");
        assertThat(trainee.getPassword()).isEqualTo("password");
        assertThat(trainee.getIsActive()).isTrue();
        assertThat(trainee.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(trainee.getAddress()).isEqualTo("123 Street");
        assertThat(trainee.getTrainers()).isNull();
    }
}
