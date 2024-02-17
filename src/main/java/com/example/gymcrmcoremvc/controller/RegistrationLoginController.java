package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.trainee.ChangeLoginRequest;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationResponse;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
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
    public TraineeRegistrationResponse registerTrainee(@RequestBody TraineeRegistrationRequest traineeRegistrationRequest) {
        return registrationLoginService.registerTrainee(traineeRegistrationRequest);
    }

    @PostMapping("/register/trainer")
    public TrainerRegistrationResponse registerTrainer(@RequestBody TrainerRegistrationRequest trainerRegistrationRequest) {
        return registrationLoginService.registerTrainer(trainerRegistrationRequest);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // Perform login logic here
        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(username, password);
        Optional<Trainer> authenticatedTrainer = trainerService.authenticateTrainer(username, password);
        if (authenticatedTrainee.isPresent() || authenticatedTrainer.isPresent()) {
            return ResponseEntity.ok("Login successful"); // Return 200 OK if login successful
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if login fails
        }
    }

    @PutMapping("/change-login")
    public ResponseEntity<String> changeLogin(@RequestBody ChangeLoginRequest request) {
        Optional<Trainee> authenticatedTrainee = traineeService.authenticateTrainee(request.getUsername(), request.getOldPassword());
        Optional<Trainer> authenticatedTrainer = trainerService.authenticateTrainer(request.getUsername(), request.getOldPassword());

        if (authenticatedTrainee.isPresent()) {
            traineeService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Trainee Password changed successfully"); // Return 200 OK if password changed
        }
        else if (authenticatedTrainer.isPresent()) {
            trainerService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Trainer Password changed successfully"); // Return 200 OK if password changed
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if authentication fails
        }
    }
}

