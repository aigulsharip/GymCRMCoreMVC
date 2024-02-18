package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TraineeControllerTest {

    /*
    private TraineeService traineeService;
    private TraineeControllerOld traineeController;

    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        traineeController = new TraineeControllerOld(traineeService, trainerService);
    }

    @Test
    void testGetAllTrainees_NoSearch() {
        // Setup
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(new Trainee());
        trainees.add(new Trainee());

        when(traineeService.getAllTrainees()).thenReturn(trainees);
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.getAllTrainees(model, null);

        // Verification
        verify(traineeService).getAllTrainees();
        verify(model).addAttribute("trainees", trainees);
        assertEquals("trainee/list", viewName);
    }

    @Test
    void testGetAllTrainees_WithSearch() {
        // Setup
        String search = "username";
        Optional<Trainee> trainee = Optional.of(new Trainee());

        when(traineeService.getTraineeByUsername(search)).thenReturn(trainee);
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.getAllTrainees(model, search);

        // Verification
        verify(traineeService).getTraineeByUsername(search);
        verify(model).addAttribute("trainee", trainee.get());
        assertEquals("trainee/edit", viewName);
    }

    @Test
    void testShowAddForm() {
        // Setup
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.showAddForm(model);

        // Verification
        verify(model).addAttribute("trainee", new Trainee());
        assertEquals("trainee/add", viewName);
    }

    @Test
    void testAddTrainee_WithErrors() {
        // Setup
        Trainee trainee = new Trainee();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.addTrainee(trainee, bindingResult, model);

        // Verification
        verify(model).addAttribute("trainee", trainee);
        verify(model).addAttribute("errors", bindingResult.getAllErrors());
        assertEquals("trainee/add", viewName);
    }

    @Test
    void testAddTrainee_Success() {
        // Setup
        Trainee trainee = new Trainee();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Execution
        String viewName = traineeController.addTrainee(trainee, bindingResult, mock(Model.class));

        // Verification
        verify(traineeService).saveTrainee(trainee);
        assertEquals("redirect:/trainees", viewName);
    }

    @Test
    void testShowEditForm() {
        // Setup
        Long traineeId = 1L;
        Trainee trainee = new Trainee();
        trainee.setId(traineeId);
        when(traineeService.getTraineeById(traineeId)).thenReturn(Optional.of(trainee));
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.showEditForm(traineeId, model);

        // Verification
        verify(traineeService).getTraineeById(traineeId);
        verify(model).addAttribute("trainee", trainee);
        assertEquals("trainee/edit", viewName);
    }


    @Test
    void testDeleteTrainee() {
        // Setup
        Long traineeId = 1L;

        // Execution
        String viewName = traineeController.deleteTrainee(traineeId);

        // Verification
        verify(traineeService).deleteTrainee(traineeId);
        assertEquals("redirect:/trainees", viewName);
    }

    @Test
    void testViewTraineeProfile_Exists() {
        // Setup
        String username = "testUser";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(trainee));
        Model model = mock(Model.class);

        // Execution
        String viewName = traineeController.viewTraineeProfile(username, model);

        // Verification
        verify(traineeService).getTraineeByUsername(username);
        verify(model).addAttribute("trainee", trainee);
        assertEquals("trainee/edit", viewName);
    }

    @Test
    void testViewTraineeProfile_NotExists() {
        // Setup
        String username = "nonExistingUser";
        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.empty());

        // Execution
        String viewName = traineeController.viewTraineeProfile(username, mock(Model.class));

        // Verification
        verify(traineeService).getTraineeByUsername(username);
        assertEquals("redirect:/trainees", viewName);
    }

    @Test
    void testChangePassword_Success() {
        // Setup
        String username = "testUser";
        String newPassword = "newPassword";
        Trainee trainee = new Trainee();
        trainee.setUsername(username);
        when(traineeService.getTraineeByUsername(username)).thenReturn(Optional.of(trainee));

        // Execution
        String viewName = traineeController.changePassword(username, newPassword, mock(Model.class));

        // Verification
        verify(traineeService).updateTraineePassword(username, newPassword);
        assertEquals("redirect:/trainees/edit/" + trainee.getId() + "?successMessage=Password changed successfully", viewName);
    }

    @Test
    void testToggleTraineeStatus() {
        // Setup
        Long traineeId = 1L;

        // Execution
        String viewName = traineeController.toggleTraineeStatus(traineeId);

        // Verification
        verify(traineeService).toggleTraineeStatus(traineeId);
        assertEquals("redirect:/trainees", viewName);
    }

    @Test
    void testDeleteTraineeByUsername() {
        // Setup
        String username = "testUser";

        // Execution
        String viewName = traineeController.deleteTraineeByUsername(username);

        // Verification
        verify(traineeService).deleteTraineeByUsername(username);
        assertEquals("redirect:/trainees", viewName);
    }

    @Test
    void testGetTraineeTrainingList() {
        // Setup
        String username = "testUser";
        LocalDate fromDate = LocalDate.of(2022, 1, 1);
        LocalDate toDate = LocalDate.of(2022, 12, 31);
        String trainerName = "John Doe";
        String trainingTypeName = "Cardio";
        List<Training> trainingList = new ArrayList<>();
        when(traineeService.getTraineeTrainingList(username, fromDate, toDate, trainerName, trainingTypeName)).thenReturn(trainingList);
        Model model = mock(Model.class);
        List<Trainee> trainees = new ArrayList<>();
        when(traineeService.getAllTrainees()).thenReturn(trainees);
        Optional<Trainee> trainee = Optional.of(new Trainee());
        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);
        List<Trainer> availableTrainers = new ArrayList<>();
        when(trainerService.getAvailableTrainersByTrainee(trainee)).thenReturn(availableTrainers);

        // Execution
        String viewName = traineeController.getTraineeTrainingList(username, fromDate, toDate, trainerName, trainingTypeName, model);

        // Verification
        verify(traineeService).getTraineeTrainingList(username, fromDate, toDate, trainerName, trainingTypeName);
        verify(model).addAttribute("trainingList", trainingList);
        verify(model).addAttribute("trainees", trainees);
        verify(model).addAttribute("trainee", new Trainee());
        verify(model).addAttribute("trainer", new Trainer());
        verify(traineeService).getTraineeByUsername(username);
        verify(trainerService).getAvailableTrainersByTrainee(trainee);
        assertEquals("trainee/training-list", viewName);
    }

     */


}
