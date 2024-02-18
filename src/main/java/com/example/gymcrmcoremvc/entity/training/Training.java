package com.example.gymcrmcoremvc.entity.training;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "trainings")
@Data
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TrainingType trainingType;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "training_duration", nullable = false)
    private int trainingDuration;
}

