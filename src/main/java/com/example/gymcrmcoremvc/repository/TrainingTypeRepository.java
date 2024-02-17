package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
}
