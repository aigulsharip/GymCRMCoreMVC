package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeProfileResponse;
import com.example.gymcrmcoremvc.entity.trainee.TraineeUpdateRequest;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticateTrainee_ValidCredentials_ReturnsTrainee() {
        String username = "testUser";
        String password = "testPassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setPassword(password);
        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(trainee));

        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(username, password);

        assertThat(authenticatedTrainee).isPresent();
        assertThat(authenticatedTrainee.get()).isEqualTo(trainee);
    }

    @Test
    public void testAuthenticateTrainee_InvalidCredentials_ReturnsEmptyOptional() {
        String username = "testUser";
        String password = "testPassword";
        when(traineeRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(username, password);

        assertThat(authenticatedTrainee).isEmpty();
    }

    @Test
    public void testFindTraineeByUsername_TraineeExists_ReturnsTrainee() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        Optional<Trainee> foundTrainee = traineeService.findTraineeByUsername(username);

        assertThat(foundTrainee).isPresent();
        assertThat(foundTrainee.get()).isEqualTo(trainee);
    }

    @Test
    public void testFindTraineeByUsername_TraineeDoesNotExist_ReturnsEmptyOptional() {
        String username = "nonExistingUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Trainee> foundTrainee = traineeService.findTraineeByUsername(username);

        assertThat(foundTrainee).isEmpty();
    }

    @Test
    public void testChangePassword_ValidCredentials_PasswordChangedSuccessfully() {
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setPassword(oldPassword);
        when(traineeRepository.findByUsernameAndPassword(username, oldPassword)).thenReturn(Optional.of(trainee));

        traineeService.changePassword(username, oldPassword, newPassword);

        assertThat(trainee.getPassword()).isEqualTo(newPassword);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testChangePassword_InvalidCredentials_NoPasswordChange() {
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        when(traineeRepository.findByUsernameAndPassword(username, oldPassword)).thenReturn(Optional.empty());

        traineeService.changePassword(username, oldPassword, newPassword);

        verify(traineeRepository, never()).save(any());
    }

    @Test
    public void testGetTraineeProfile_TraineeDoesNotExist_ReturnsNull() {
        String username = "nonExistingUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        TraineeProfileResponse profileResponse = traineeService.getTraineeProfile(username);

        assertThat(profileResponse).isNull();
    }

    @Test
    public void testUpdateTraineeProfile_TraineeDoesNotExist_ReturnsNull() {
        String username = "nonExistingUser";
        TraineeUpdateRequest request = new TraineeUpdateRequest();
        request.setFirstName("NewFirstName");
        request.setLastName("NewLastName");
        // Set other fields as needed
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        TraineeProfileResponse updatedProfile = traineeService.updateTraineeProfile(username, request);

        assertThat(updatedProfile).isNull();
        verify(traineeRepository, never()).save(any());
    }


    @Test
    public void testDeleteTraineeProfile_TraineeExistsAndAssociatedTrainings_TraineeProfileDeletedSuccessfully() {
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainingRepository.findByTrainee(trainee)).thenReturn(Arrays.asList(new Training(), new Training()));

        boolean deleted = traineeService.deleteTraineeProfile(username);

        assertThat(deleted).isTrue();
        verify(trainingRepository, times(1)).deleteAll(any());
        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    public void testDeleteTraineeProfile_TraineeDoesNotExist_ReturnsFalse() {
        String username = "nonExistingUser";
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean deleted = traineeService.deleteTraineeProfile(username);

        assertThat(deleted).isFalse();
        verify(trainingRepository, never()).deleteAll(any());
        verify(traineeRepository, never()).delete(any());
    }

    @Test
    public void testActivateDeactivateTrainee_TraineeExistsAndIsActiveChanged_Successfully() {
        String username = "testUser";
        boolean isActive = false;
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.activateDeactivateTrainee(username, isActive);

        assertThat(trainee.getIsActive()).isEqualTo(isActive);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testActivateDeactivateTrainee_TraineeDoesNotExist_EntityNotFoundExceptionThrown() {
        String username = "nonExistingUser";
        boolean isActive = false;
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.empty());

        try {
            traineeService.activateDeactivateTrainee(username, isActive);
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Trainee not found with username: " + username);
            verify(traineeRepository, never()).save(any());
        }
    }

    @Test
    public void testGetNotAssignedActiveTrainers_TraineeDoesNotExist_RuntimeExceptionThrown() {
        String traineeUsername = "nonExistingUser";
        when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.empty());

        try {
            traineeService.getNotAssignedActiveTrainers(traineeUsername);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Trainee not found");
            verify(trainerRepository, never()).findAll();
            verify(trainerRepository, never()).findByIsActive(true);
        }
    }

    @Test
    public void testUpdateTraineeTrainers_TraineeExists_TrainersUpdatedSuccessfully() {
        String traineeUsername = "testUser";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2", "trainer3");
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer(), new Trainer());
        Trainee trainee = new Trainee();
        trainee.setUsername(traineeUsername);
        when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsernameIn(trainerUsernames)).thenReturn(trainers);

        List<TrainerInfo> updatedTrainers = traineeService.updateTraineeTrainers(traineeUsername, trainerUsernames);

        assertThat(updatedTrainers).hasSize(trainers.size());
        verify(traineeRepository, times(1)).findByUsername(traineeUsername);
        verify(trainerRepository, times(1)).findByUsernameIn(trainerUsernames);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    public void testUpdateTraineeTrainers_TraineeDoesNotExist_EntityNotFoundExceptionThrown() {
        String traineeUsername = "nonExistingUser";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2", "trainer3");
        when(traineeRepository.findByUsername(traineeUsername)).thenReturn(Optional.empty());

        try {
            traineeService.updateTraineeTrainers(traineeUsername, trainerUsernames);
        } catch (EntityNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Trainee not found");
            verify(trainerRepository, never()).findByUsernameIn(trainerUsernames);
            verify(traineeRepository, never()).save(any());
        }
    }
}
