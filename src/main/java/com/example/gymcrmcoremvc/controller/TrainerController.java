package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerUpdateRequest;
import com.example.gymcrmcoremvc.service.TrainerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;


    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();
        if (!trainers.isEmpty()) {
            return ResponseEntity.ok(trainers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@RequestParam @NotBlank String username) {
        TrainerProfileResponse profileResponse = trainerService.getTrainerProfile(username);
        if (profileResponse != null) {
            return ResponseEntity.ok(profileResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(@RequestParam @NotBlank String username,
                                                                       @RequestBody @Valid TrainerUpdateRequest request) {
        TrainerProfileResponse updatedProfile = trainerService.updateTrainerProfile(username, request);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteTrainerProfile(@RequestParam @NotBlank String username) {
        boolean deleted = trainerService.deleteTrainerProfile(username);
        if (deleted) {
            return ResponseEntity.ok("Trainer profile deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username");
        }
    }

    @PatchMapping("/activate-deactivate")
    public ResponseEntity<Void> activateDeactivateTrainer(@RequestParam @NotBlank String username,
                                                          @RequestParam boolean isActive) {
        trainerService.activateDeactivateTrainer(username, isActive);
        return ResponseEntity.ok().build();
    }


}
