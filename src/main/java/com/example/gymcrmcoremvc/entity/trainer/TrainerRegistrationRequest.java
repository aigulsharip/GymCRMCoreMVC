package com.example.gymcrmcoremvc.entity.trainer;

import com.example.gymcrmcoremvc.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerRegistrationRequest {
    private String firstName;
    private String lastName;
    private TrainingType trainingType;
}