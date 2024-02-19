package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.registration.*;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<?> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest traineeRegistrationRequest) {
        if (registrationLoginService.isTrainer(traineeRegistrationRequest.getFirstName(), traineeRegistrationRequest.getLastName())) {
            return ResponseEntity.badRequest().body("User is already registered as a trainer");
        }
        if (registrationLoginService.isTrainee(traineeRegistrationRequest.getFirstName(), traineeRegistrationRequest.getLastName())) {
            return ResponseEntity.badRequest().body("User is already registered as a trainee");
        }
        TraineeRegistrationResponse traineeRegistrationResponse = registrationLoginService.registerTrainee(traineeRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeRegistrationResponse);
    }

    @PostMapping("/register/trainer")
    public ResponseEntity<?> registerTrainer(@Valid @RequestBody TrainerRegistrationRequest trainerRegistrationRequest) {
        if (registrationLoginService.isTrainee(trainerRegistrationRequest.getFirstName(), trainerRegistrationRequest.getLastName())) {
            return ResponseEntity.badRequest().body("User is already registered as a trainee");
        }
        if (registrationLoginService.isTrainer(trainerRegistrationRequest.getFirstName(), trainerRegistrationRequest.getLastName())) {
            return ResponseEntity.badRequest().body("User is already registered as a trainer");
        }
        TrainerRegistrationResponse trainerRegistrationResponse = registrationLoginService.registerTrainer(trainerRegistrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainerRegistrationResponse);
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
        } else if (authenticatedTrainer.isPresent()) {
            trainerService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Trainer Password changed successfully"); // Return 200 OK if password changed
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password"); // Return 401 Unauthorized if authentication fails
        }
    }
    // ExceptionHandler on the controller allow exception handling on the is only active for that particular Controller,
    // not globally for the entire application. Whereas, @ControllerAdvice allows the exception handling for all endpoints
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
//                .map(FieldError::getDefaultMessage)
//                .findFirst()
//                .orElse("Validation error occurred");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }
//
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
//        String errorMessage = ex.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessage)
//                .findFirst()
//                .orElse("Validation error occurred");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//    }

}

