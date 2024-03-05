package com.example.gymcrmcoremvc.controller.rest;

import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.entity.training.TrainingRequest;
import com.example.gymcrmcoremvc.entity.training.TrainingTraineeResponse;
import com.example.gymcrmcoremvc.entity.training.TrainingTrainerResponse;
import com.example.gymcrmcoremvc.service.rest.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@Slf4j
public class TrainingController {

    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        log.info("Received request to get all trainings");
        List<Training> trainings = trainingService.getAllTrainings();
        if (!trainings.isEmpty()) {
            log.info("Returning all trainings: {}", trainings);
            return ResponseEntity.ok(trainings);
        } else {
            log.warn("No trainings found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> addTraining(@RequestBody @Valid TrainingRequest request) {
        log.info("Received request to add new training: {}", request);
        trainingService.addTraining(request);
        log.info("Training created successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Training created successfully");
    }

    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<TrainingTraineeResponse>> getTraineeTrainingsList(
            @RequestParam(required = true) @NotBlank String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingTypeName) {
        log.info("Received request to get trainee trainings for username: {}, periodFrom: {}, periodTo: {}, trainerName: {}, trainingTypeName: {}",
                username, periodFrom, periodTo, trainerName, trainingTypeName);
        List<TrainingTraineeResponse> trainings = trainingService.getTraineeTrainingList(username, periodFrom, periodTo, trainerName, trainingTypeName);
        log.info("Returning trainee trainings: {}", trainings);
        return ResponseEntity.ok().body(trainings);
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainingTrainerResponse>> getTrainerTrainingsList(
            @RequestParam(required = true) @NotBlank String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName,
            @RequestParam(required = false) String trainingTypeName) {
        log.info("Received request to get trainer trainings for username: {}, periodFrom: {}, periodTo: {}, traineeName: {}, trainingTypeName: {}",
                username, periodFrom, periodTo, traineeName, trainingTypeName);
        List<TrainingTrainerResponse> trainings = trainingService.getTrainerTrainingsList(username, periodFrom, periodTo, traineeName, trainingTypeName);
        log.info("Returning trainer trainings: {}", trainings);
        return ResponseEntity.ok().body(trainings);
    }
}
