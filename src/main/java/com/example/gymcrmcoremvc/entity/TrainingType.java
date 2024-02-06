package com.example.gymcrmcoremvc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

//@Entity
//@Table(name = "training_types")
//@Data
//public class TrainingType implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, unique = true)
//    private TrainingTypeName name;
//}


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

