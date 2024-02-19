package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerUpdateRequest;
import com.example.gymcrmcoremvc.service.TrainerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
@Slf4j
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        log.info("Received request to get all trainers");
        List<Trainer> trainers = trainerService.getAllTrainers();
        if (!trainers.isEmpty()) {
            log.info("Returning all trainers: {}", trainers);
            return ResponseEntity.ok(trainers);
        } else {
            log.warn("No trainers found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@RequestParam @NotBlank String username) {
        log.info("Received request to get trainer profile for username: {}", username);
        TrainerProfileResponse profileResponse = trainerService.getTrainerProfile(username);
        if (profileResponse != null) {
            log.info("Returning trainer profile: {}", profileResponse);
            return ResponseEntity.ok(profileResponse);
        } else {
            log.warn("Trainer profile not found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(@RequestParam @NotBlank String username,
                                                                       @RequestBody @Valid TrainerUpdateRequest request) {
        log.info("Received request to update trainer profile for username: {}, request: {}", username, request);
        TrainerProfileResponse updatedProfile = trainerService.updateTrainerProfile(username, request);
        if (updatedProfile != null) {
            log.info("Trainer profile updated successfully: {}", updatedProfile);
            return ResponseEntity.ok(updatedProfile);
        } else {
            log.warn("Trainer profile not found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTrainerProfile(@RequestParam @NotBlank String username) {
        log.info("Received request to delete trainer profile for username: {}", username);
        boolean deleted = trainerService.deleteTrainerProfile(username);
        if (deleted) {
            log.info("Trainer profile deleted successfully for username: {}", username);
            return ResponseEntity.ok("Trainer profile deleted successfully");
        } else {
            log.warn("Invalid username: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        }
    }

    @PatchMapping("/activate-deactivate")
    public ResponseEntity<Void> activateDeactivateTrainer(@RequestParam @NotBlank String username,
                                                          @RequestParam boolean isActive) {
        log.info("Received request to activate/deactivate trainer for username: {}, isActive: {}", username, isActive);
        trainerService.activateDeactivateTrainer(username, isActive);
        log.info("Trainer activated/deactivated successfully for username: {}", username);
        return ResponseEntity.ok().build();
    }
}
