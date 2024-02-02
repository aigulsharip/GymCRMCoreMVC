package com.example.gymcrmcoremvc.repository;


import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    boolean existsByUsername(String username);

    Optional<Trainer> findByUsernameAndPassword (String username, String password);

    Optional<Trainer> findByUsername (String username);

}
