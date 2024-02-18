package com.example.gymcrmcoremvc.repository;


import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    boolean existsByUsername(String username);
    Optional<Trainer> findByUsernameAndPassword (String username, String password);
    Optional<Trainer> findByUsername (String username);
    List<Trainer> findByUsernameIn(List<String> trainerUsernames);

}
