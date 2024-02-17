package com.example.gymcrmcoremvc.entity.trainer;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import com.example.gymcrmcoremvc.entity.trainee.TraineeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponse {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;
    private List<TraineeInfo> trainees;
}

