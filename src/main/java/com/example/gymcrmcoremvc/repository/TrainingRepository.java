package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTrainee(Trainee trainee);



}

