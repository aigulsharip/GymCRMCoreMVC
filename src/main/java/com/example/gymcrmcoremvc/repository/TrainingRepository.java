package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
