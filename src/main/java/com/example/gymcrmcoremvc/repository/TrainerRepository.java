package com.example.gymcrmcoremvc.repository;


import com.example.gymcrmcoremvc.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    boolean existsByUsername(String username);

}
