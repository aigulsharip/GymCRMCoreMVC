package com.example.gymcrmcoremvc.entity.trainingType;

//import jakarta.persistence.*;
import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "training_types")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;

}

