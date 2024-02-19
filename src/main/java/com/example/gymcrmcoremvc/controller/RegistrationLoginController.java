package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.registration.*;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Slf4j
public class RegistrationLoginController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final RegistrationLoginService registrationLoginService;

    public RegistrationLoginController(TraineeService traineeService, TrainerService trainerService, RegistrationLoginService registrationLoginService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.registrationLoginService = registrationLoginService;
    }

    @PostMapping("/register/trainee")
    public ResponseEntity<?> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest) {
        log.info("Received request to register trainee: {}", traineeRegistrationRequest);
        if (registrationLoginService.isTrainer(traineeRegistrationRequest.getFirstName(), traineeRegistrationRequest.getLastName())) {
            log.warn("User {} is already registered as a trainer", traineeRegistrationRequest);
            return ResponseEntity.badRequest().body("User is already registered as a trainer");
        }
        if (registrationLoginService.isTrainee(traineeRegistrationRequest.getFirstName(), traineeRegistrationRequest.getLastName())) {
            log.warn("User {} is already registered as a trainee", traineeRegistrationRequest);
            return ResponseEntity.badRequest().body("User is already registered as a trainee");
        }
        TraineeRegistrationResponse traineeRegistrationResponse = registrationLoginService.registerTrainee(traineeRegistrationRequest);
        log.info("Trainee registered successfully: {}", traineeRegistrationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeRegistrationResponse);
    }

    @PostMapping("/register/trainer")
    public ResponseEntity<?> registerTrainer(@Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest) {
        log.info("Received request to register trainer: {}", trainerRegistrationRequest);
        if (registrationLoginService.isTrainee(trainerRegistrationRequest.getFirstName(), trainerRegistrationRequest.getLastName())) {
            log.warn("User {} is already registered as a trainee", trainerRegistrationRequest);
            return ResponseEntity.badRequest().body("User is already registered as a trainee");
        }
        if (registrationLoginService.isTrainer(trainerRegistrationRequest.getFirstName(), trainerRegistrationRequest.getLastName())) {
            log.warn("User {} is already registered as a trainer", trainerRegistrationRequest);
            return ResponseEntity.badRequest().body("User is already registered as a trainer");
        }
        TrainerRegistrationResponse trainerRegistrationResponse = registrationLoginService.registerTrainer(trainerRegistrationRequest);
        log.info("Trainer registered successfully: {}", trainerRegistrationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerRegistrationResponse);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam @NotBlank String username,
                                        @RequestParam @NotBlank String password) {
        log.info("Received login request for user: {}", username);
        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(username, password);
        Optional<Trainer> authenticatedTrainer = trainerService.authenticateTrainer(username, password);
        if (authenticatedTrainee.isPresent() || authenticatedTrainer.isPresent()) {
            log.info("Login successful for user: {}", username);
            return ResponseEntity.ok("Login successful"); // Return 200 OK if login successful
        } else {
            log.warn("Invalid username or password for user: {}", username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if login fails
        }
    }

    @PutMapping("/change-login")
    public ResponseEntity<String> changeLogin(@Valid @RequestBody ChangeLoginRequest request) {
        log.info("Received request to change login for user: {}", request.getUsername());
        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(request.getUsername(), request.getOldPassword());
        Optional<Trainer> authenticatedTrainer = trainerService.authenticateTrainer(request.getUsername(), request.getOldPassword());

        if (authenticatedTrainee.isPresent()) {
            traineeService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            log.info("Password changed successfully for trainee: {}", request.getUsername());
            return ResponseEntity.ok("Trainee Password changed successfully"); // Return 200 OK if password changed
        } else if (authenticatedTrainer.isPresent()) {
            trainerService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            log.info("Password changed successfully for trainer: {}", request.getUsername());
            return ResponseEntity.ok("Trainer Password changed successfully"); // Return 200 OK if password changed
        } else {
            log.warn("Invalid username or password for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if authentication fails
        }
    }
}
