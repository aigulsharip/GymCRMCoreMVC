package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainee.*;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees")
@Slf4j
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private final RegistrationLoginService registrationLoginService;

    public TraineeController(TraineeService traineeService, TrainerService trainerService, RegistrationLoginService registrationLoginService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.registrationLoginService = registrationLoginService;
    }

    @GetMapping
    public ResponseEntity<List<Trainee>> getAllTrainees() {
        log.info("Received request to get all trainees");
        List<Trainee> trainees = traineeService.getAllTrainees();
        if (!trainees.isEmpty()) {
            log.info("Returning all trainees: {}", trainees);
            return ResponseEntity.ok(trainees);
        } else {
            log.warn("No trainees found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@RequestParam @NotBlank String username) {
        log.info("Received request to get trainee profile for username: {}", username);
        TraineeProfileResponse profileResponse = traineeService.getTraineeProfile(username);

        if (profileResponse != null) {
            log.info("Returning trainee profile: {}", profileResponse);
            return ResponseEntity.ok(profileResponse); // Return 200 OK with profile information
        } else {
            log.warn("Trainee profile not found for username: {}", username);
            return ResponseEntity.notFound().build(); // Return 404 Not Found if trainee not found
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(@RequestParam @NotBlank String username,
                                                                       @Valid @RequestBody TraineeUpdateRequest request) {
        log.info("Received request to update trainee profile for username: {}, request: {}", username, request);
        TraineeProfileResponse updatedProfile = traineeService.updateTraineeProfile(username, request);
        if (updatedProfile != null) {
            log.info("Trainee profile updated successfully: {}", updatedProfile);
            return ResponseEntity.ok(updatedProfile);
        } else {
            log.warn("Trainee profile not found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTraineeProfile(@RequestParam @NotBlank String username) {
        log.info("Received request to delete trainee profile for username: {}", username);
        boolean deleted = traineeService.deleteTraineeProfile(username);
        if (deleted) {
            log.info("Trainee profile deleted successfully for username: {}", username);
            return ResponseEntity.ok("Trainee profile deleted successfully");
        } else {
            log.warn("Invalid username: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        }
    }

    @GetMapping("/not-assigned-trainers")
    public ResponseEntity<List<TrainerInfo>> getNotAssignedActiveTrainers(@RequestParam @NotBlank String username) {
        log.info("Received request to get not assigned active trainers for trainee username: {}", username);
        List<TrainerInfo> notAssignedActiveTrainers = traineeService.getNotAssignedActiveTrainers(username);
        if (!notAssignedActiveTrainers.isEmpty()) {
            log.info("Returning not assigned active trainers: {}", notAssignedActiveTrainers);
            return ResponseEntity.ok(notAssignedActiveTrainers);
        } else {
            log.warn("No not assigned active trainers found for trainee username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/activate-deactivate")
    public ResponseEntity<Void> activateDeactivateTrainee(@RequestParam @NotBlank String username,
                                                          @RequestParam boolean isActive) {
        log.info("Received request to activate/deactivate trainee for username: {}, isActive: {}", username, isActive);
        traineeService.activateDeactivateTrainee(username, isActive);
        log.info("Trainee activated/deactivated successfully for username: {}", username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-trainers")
    public ResponseEntity<List<TrainerInfo>> updateTraineeTrainers(
            @RequestParam @NotBlank String username,
            @RequestBody List<String> trainerUsernames) {
        log.info("Received request to update trainers for trainee username: {}, trainers: {}", username, trainerUsernames);
        List<TrainerInfo> updatedTrainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        log.info("Updated trainers for trainee username {}: {}", username, updatedTrainers);
        return ResponseEntity.ok(updatedTrainers);
    }
}