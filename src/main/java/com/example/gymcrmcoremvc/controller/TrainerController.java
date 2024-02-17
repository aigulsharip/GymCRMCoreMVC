package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationResponse;
import com.example.gymcrmcoremvc.service.RegistrationLoginService;
import com.example.gymcrmcoremvc.service.TrainerService;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private RegistrationLoginService registrationLoginService;

    @GetMapping
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();
        if (!trainers.isEmpty()) {
            return ResponseEntity.ok(trainers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
