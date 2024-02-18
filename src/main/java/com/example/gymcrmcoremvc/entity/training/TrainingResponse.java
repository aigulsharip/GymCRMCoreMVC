package com.example.gymcrmcoremvc.entity.training;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponse {
    private String trainingName;
    private LocalDate trainingDate;
    private TrainingType trainingType;
    private int trainingDuration;
    private String trainerName;
}

