package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingTypeResponse;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> trainingTypes = trainingTypeService.getAllTrainingTypesList();
        return ResponseEntity.ok(trainingTypes);
    }
}