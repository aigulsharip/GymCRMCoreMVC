package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeServiceTest {
    /*
    @Mock
    private TraineeRepository traineeRepository;

    @InjectMocks
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTrainees() {
        when(traineeRepository.findAll()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), traineeService.getAllTrainees());
    }

    @Test
    void testGetTraineeById() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));

        assertEquals(Optional.of(trainee), traineeService.getTraineeById(traineeId));
    }

    @Test
    void testSaveTrainee() {
        Trainee trainee = new Trainee();
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        assertEquals(trainee, traineeService.saveTrainee(trainee));
    }

    @Test
    void testUpdateTrainee() {
        Long traineeId = 1L;
        Trainee existingTrainee = new Trainee();
        existingTrainee.setId(traineeId);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(existingTrainee));
        when(traineeRepository.save(existingTrainee)).thenReturn(existingTrainee);

        assertEquals(existingTrainee, traineeService.updateTrainee(traineeId, existingTrainee));
    }

    @Test
    void testToggleTraineeStatus() {
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        trainee.setIsActive(true);
        when(traineeRepository.findById(traineeId)).thenReturn(Optional.of(trainee));

        traineeService.toggleTraineeStatus(traineeId);

        verify(traineeRepository).save(trainee);
        assertEquals(false, trainee.getIsActive());
    }

    @Test
    void testGetTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        assertEquals(Optional.of(trainee), traineeService.getTraineeByUsername(username));
    }

    @Test
    void testUpdateTraineePassword() {
        String username = "testuser";
        String newPassword = "newpassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        assertEquals(trainee, traineeService.updateTraineePassword(username, newPassword));
        assertEquals(newPassword, trainee.getPassword());
    }

    @Test
    void testDeleteTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeByUsername(username);

        verify(traineeRepository).delete(trainee);
    }

    // Additional tests for other methods can be added similarly

     */
}
