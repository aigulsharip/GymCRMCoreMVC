package com.example.gymcrmcoremvc.service;


import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.entity.training.TrainingRequest;
import com.example.gymcrmcoremvc.entity.training.TrainingTraineeResponse;
import com.example.gymcrmcoremvc.entity.training.TrainingTrainerResponse;
import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TrainingService {

    private final TrainingRepository trainingRepository;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        log.info("Fetching all trainings");
        return trainingRepository.findAll();
    }

    public void addTraining(TrainingRequest request) {
        // Convert the request to a Training entity if needed
        Training training = new Training();
        Trainee trainee = traineeRepository.findByUsername(request.getTraineeUsername()).orElse(null);
        Trainer trainer = trainerRepository.findByUsername(request.getTrainerUsername()).orElse(null);

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setTrainingDuration(request.getTrainingDuration());
        training.setTrainingType(request.getTrainingType());

        trainingRepository.save(training);
    }

    public List<TrainingTraineeResponse> getTraineeTrainingList(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingTypeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Training> root = criteriaQuery.from(Training.class);

        // Selecting specific fields to avoid fetching unnecessary data
        criteriaQuery.multiselect(
                root.get("trainingName"),
                root.get("trainingDate"),
                root.get("trainingType"),
                root.get("trainingDuration"),
                root.join("trainer").get("username")
        );

        List<Predicate> predicates = new ArrayList<>();

        // Add username condition
        predicates.add(criteriaBuilder.equal(root.join("trainee").get("username"), username));

        // Add fromDate condition if provided
        if (fromDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }

        // Add toDate condition if provided
        if (toDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }

        // Add trainerName condition if provided
        if (trainerName != null && !trainerName.isEmpty()) {
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            predicates.add(criteriaBuilder.equal(trainerJoin.get("username"), trainerName));
        }


        // Add trainingTypeName condition if provided
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("trainingType").get("trainingTypeName"), "%" + trainingTypeName + "%"));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Tuple> query = entityManager.createQuery(criteriaQuery);
        List<Tuple> tuples = query.getResultList();

        // Mapping Tuple results to TrainingResponse objects
        List<TrainingTraineeResponse> trainingResponses = new ArrayList<>();
        for (Tuple tuple : tuples) {
            TrainingTraineeResponse trainingResponse = new TrainingTraineeResponse();
            trainingResponse.setTrainingName((String) tuple.get(0));
            trainingResponse.setTrainingDate((LocalDate) tuple.get(1));
            trainingResponse.setTrainingType((TrainingType) tuple.get(2));
            trainingResponse.setTrainingDuration((int) tuple.get(3));
            trainingResponse.setTrainerName((String) tuple.get(4));
            trainingResponses.add(trainingResponse);
        }

        return trainingResponses;
    }

    public List<TrainingTrainerResponse> getTrainerTrainingsList(String username, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingTypeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Training> root = criteriaQuery.from(Training.class);

        // Selecting specific fields to avoid fetching unnecessary data
        criteriaQuery.multiselect(
                root.get("trainingName"),
                root.get("trainingDate"),
                root.get("trainingType"),
                root.get("trainingDuration"),
                root.join("trainee").get("username")
        );

        List<Predicate> predicates = new ArrayList<>();

        // Add username condition
        predicates.add(criteriaBuilder.equal(root.join("trainer").get("username"), username));

        // Add fromDate condition if provided
        if (fromDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("trainingDate"), fromDate));
        }

        // Add toDate condition if provided
        if (toDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("trainingDate"), toDate));
        }

        // Add traineeName condition if provided
        if (traineeName != null && !traineeName.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.join("trainee").get("username"), traineeName));
        }

        // Add trainingTypeName condition if provided
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("trainingType").get("trainingTypeName"), "%" + trainingTypeName + "%"));
        }


        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Tuple> query = entityManager.createQuery(criteriaQuery);
        List<Tuple> tuples = query.getResultList();

        // Mapping Tuple results to TrainingResponse objects
        List<TrainingTrainerResponse> trainingResponses = new ArrayList<>();
        for (Tuple tuple : tuples) {
            TrainingTrainerResponse trainingResponse = new TrainingTrainerResponse();
            trainingResponse.setTrainingName((String) tuple.get(0));
            trainingResponse.setTrainingDate((LocalDate) tuple.get(1));
            trainingResponse.setTrainingType((TrainingType) tuple.get(2));
            trainingResponse.setTrainingDuration((int) tuple.get(3));
            trainingResponse.setTraineeName((String) tuple.get(4));
            trainingResponses.add(trainingResponse);
        }

        return trainingResponses;
    }
}
