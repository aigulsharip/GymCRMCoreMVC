package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.entity.training.TrainingRequest;
import com.example.gymcrmcoremvc.entity.training.TrainingTraineeResponse;
import com.example.gymcrmcoremvc.entity.training.TrainingTrainerResponse;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import com.example.gymcrmcoremvc.service.TrainingService;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService,
                              TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
        if (!trainings.isEmpty()) {
            return ResponseEntity.ok(trainings);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> addTraining(@RequestBody TrainingRequest request) {
        trainingService.addTraining(request);
        return ResponseEntity.status(HttpStatus.OK).body("Training created succesfully");
    }

    @GetMapping("/trainee-trainings")
    public ResponseEntity<List<TrainingTraineeResponse>> getTraineeTrainingsList(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingTypeName) {

        List<TrainingTraineeResponse> trainings = trainingService.getTraineeTrainingList(username, periodFrom, periodTo, trainerName, trainingTypeName);

        return ResponseEntity.ok().body(trainings);
    }

    @GetMapping("/trainer-trainings")
    public ResponseEntity<List<TrainingTrainerResponse>> getTrainerTrainingsList(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) LocalDate periodFrom,
            @RequestParam(required = false) LocalDate periodTo,
            @RequestParam(required = false) String traineeName,
            @RequestParam(required = false) String trainingTypeName) {

        List<TrainingTrainerResponse> trainings = trainingService.getTrainerTrainingsList(username, periodFrom, periodTo, traineeName, trainingTypeName);

        return ResponseEntity.ok().body(trainings);
    }

}
