package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingTypeResponse;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-types")
@Api(tags = "Training Types")
@Slf4j
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @GetMapping
    @Operation(summary = "Get all training types\", notes = \"Retrieve a list of all available training types.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list of training types"),
            @ApiResponse(code = 500, message = "Internal server error") })
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        log.info("Received request to get all training types");
        List<TrainingTypeResponse> trainingTypes = trainingTypeService.getAllTrainingTypesList();
        log.info("Returning all training types: {}", trainingTypes);
        return ResponseEntity.ok(trainingTypes);
    }
}
