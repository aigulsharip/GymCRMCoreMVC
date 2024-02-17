package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    boolean existsByUsername(String username);
    Optional<Trainee> findByUsernameAndPassword(String username, String password);
    Optional<Trainee> findByUsername(String username);

    List<Trainer> findByIsActiveTrue();
}
