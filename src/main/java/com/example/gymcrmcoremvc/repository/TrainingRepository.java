package com.example.gymcrmcoremvc.repository;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t JOIN t.trainer tr JOIN t.trainingType tt JOIN t.trainee tn " +
            "WHERE tn.username = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:trainerName IS NULL OR CONCAT(tr.firstName, ' ', tr.lastName) LIKE %:trainerName%) " +
            "AND (:trainingTypeName IS NULL OR tt.trainingTypeName LIKE %:trainingTypeName%)")
    List<Training> findByTraineeAndCriteria(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingTypeName") String trainingTypeName
    );
}
