package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingTypeResponse;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
@Slf4j
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        log.info("Received request to get all training types");
        List<TrainingTypeResponse> trainingTypes = trainingTypeService.getAllTrainingTypesList();
        log.info("Returning all training types: {}", trainingTypes);
        return ResponseEntity.ok(trainingTypes);
    }
}
