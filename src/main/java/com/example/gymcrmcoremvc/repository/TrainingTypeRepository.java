package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
}
