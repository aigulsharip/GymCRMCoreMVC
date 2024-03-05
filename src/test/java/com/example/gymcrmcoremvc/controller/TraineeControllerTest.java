package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.controller.rest.TraineeController;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeProfileResponse;
import com.example.gymcrmcoremvc.entity.trainee.TraineeUpdateRequest;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.service.rest.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTrainees_ReturnsTraineesSuccessfully() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee());
        when(traineeService.getAllTrainees()).thenReturn(trainees);

        ResponseEntity<List<Trainee>> response = traineeController.getAllTrainees();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(trainees);
        verify(traineeService, times(1)).getAllTrainees();
    }

    @Test
    void getTraineeProfile_TraineeProfileExists_ReturnsProfileSuccessfully() {
        String username = "testUser";
        TraineeProfileResponse profileResponse = new TraineeProfileResponse();
        when(traineeService.getTraineeProfile(username)).thenReturn(profileResponse);

        ResponseEntity<TraineeProfileResponse> response = traineeController.getTraineeProfile(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(profileResponse);
        verify(traineeService, times(1)).getTraineeProfile(username);
    }

    @Test
    void updateTraineeProfile_ProfileUpdatedSuccessfully_ReturnsProfile() {
        String username = "testUser";
        TraineeUpdateRequest request = new TraineeUpdateRequest();
        TraineeProfileResponse updatedProfile = new TraineeProfileResponse();
        when(traineeService.updateTraineeProfile(username, request)).thenReturn(updatedProfile);

        ResponseEntity<TraineeProfileResponse> response = traineeController.updateTraineeProfile(username, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedProfile);
        verify(traineeService, times(1)).updateTraineeProfile(username, request);
    }

    @Test
    void deleteTraineeProfile_ProfileDeletedSuccessfully_ReturnsOkResponse() {
        String username = "testUser";
        when(traineeService.deleteTraineeProfile(username)).thenReturn(true);

        ResponseEntity<String> response = traineeController.deleteTraineeProfile(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Trainee profile deleted successfully");
        verify(traineeService, times(1)).deleteTraineeProfile(username);
    }

    @Test
    void deleteTraineeProfile_ProfileNotFound_ReturnsUnauthorizedResponse() {
        String username = "nonExistingUser";
        when(traineeService.deleteTraineeProfile(username)).thenReturn(false);

        ResponseEntity<String> response = traineeController.deleteTraineeProfile(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Invalid username");
        verify(traineeService, times(1)).deleteTraineeProfile(username);
    }

    @Test
    void getNotAssignedActiveTrainers_TrainersFound_ReturnsTrainersSuccessfully() {
        String username = "testUser";
        List<TrainerInfo> trainers = new ArrayList<>();
        trainers.add(new TrainerInfo());
        when(traineeService.getNotAssignedActiveTrainers(username)).thenReturn(trainers);

        ResponseEntity<List<TrainerInfo>> response = traineeController.getNotAssignedActiveTrainers(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(trainers);
        verify(traineeService, times(1)).getNotAssignedActiveTrainers(username);
    }

    @Test
    void activateDeactivateTrainee_TraineeActivated_ReturnsOkResponse() {
        String username = "testUser";
        boolean isActive = true;

        ResponseEntity<Void> response = traineeController.activateDeactivateTrainee(username, isActive);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(traineeService, times(1)).activateDeactivateTrainee(username, isActive);
    }

    @Test
    void updateTraineeTrainers_TrainersUpdated_ReturnsUpdatedTrainers() {
        String username = "testUser";
        List<String> trainerUsernames = new ArrayList<>();
        trainerUsernames.add("trainer1");

        List<TrainerInfo> updatedTrainers = new ArrayList<>();
        updatedTrainers.add(new TrainerInfo());

        when(traineeService.updateTraineeTrainers(username, trainerUsernames)).thenReturn(updatedTrainers);

        ResponseEntity<List<TrainerInfo>> response = traineeController.updateTraineeTrainers(username, trainerUsernames);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedTrainers);
        verify(traineeService, times(1)).updateTraineeTrainers(username, trainerUsernames);
    }
}
