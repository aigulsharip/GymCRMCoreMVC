package com.example.gymcrmcoremvc.entity.training;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRequest {
    private String traineeUsername;
    private String trainerUsername;
    private TrainingType trainingType;
    private String trainingName;
    private LocalDate trainingDate;
    private int trainingDuration;
}