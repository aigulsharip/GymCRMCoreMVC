package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainee.*;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trainees")
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
        List<Trainee> trainees = traineeService.getAllTrainees();
        if (!trainees.isEmpty()) {
            return ResponseEntity.ok(trainees);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@RequestParam String username) {
        TraineeProfileResponse profileResponse = traineeService.getTraineeProfile(username);

        if (profileResponse != null) {
            return ResponseEntity.ok(profileResponse); // Return 200 OK with profile information
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if trainee not found
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(@RequestParam String username,
                                                                       @RequestBody TraineeUpdateRequest request) {
        TraineeProfileResponse updatedProfile = traineeService.updateTraineeProfile(username, request);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTraineeProfile(@RequestParam String username) {
        boolean deleted = traineeService.deleteTraineeProfile(username);
        if (deleted) {
            return ResponseEntity.ok("Trainee profile deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        }
    }

    @GetMapping("/not-assigned-trainers")
    public ResponseEntity<List<TrainerInfo>> getNotAssignedActiveTrainers(@RequestParam String username) {
        List<TrainerInfo> notAssignedActiveTrainers = traineeService.getNotAssignedActiveTrainers(username);
        if (!notAssignedActiveTrainers.isEmpty()) {
            return ResponseEntity.ok(notAssignedActiveTrainers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/activate-deactivate")
    public ResponseEntity<Void> activateDeactivateTrainee(@RequestParam String username,
                                                          @RequestParam boolean isActive) {
        traineeService.activateDeactivateTrainee(username, isActive);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-trainers")
    public ResponseEntity<List<TrainerInfo>> updateTraineeTrainers(
            @RequestParam String username,
            @RequestBody List<String> trainerUsernames) {
        List<TrainerInfo> updatedTrainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        return ResponseEntity.ok(updatedTrainers);
    }


}
