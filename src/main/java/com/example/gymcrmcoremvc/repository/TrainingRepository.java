package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTraineeId(Long traineeId);
}
