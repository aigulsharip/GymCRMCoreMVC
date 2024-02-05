package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.Training;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingTypeService trainingTypeService) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
    }

    public List<Trainer> getAllTrainers() {
        log.info("Fetching all trainers");
        return trainerRepository.findAll();
    }

    public Optional<Trainer> getTrainerById(Long id) {
        return trainerRepository.findById(id);
    }

    public Trainer saveTrainer(Trainer trainer) {
        log.info("Saving new trainer: {}", trainer);
        trainer.setUsername(calculateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());
        trainer.setIsActive(true);
        trainer.setTrainingType(trainingTypeService.getTrainingTypeById(trainer.getTrainingType().getId()).orElse(null));
        return trainerRepository.save(trainer);
    }

    public Trainer updateTrainer(Long id, Trainer updatedTrainer) {
        log.info("Updating trainer with ID {}: {}", id, updatedTrainer);
        Optional<Trainer> existingTrainerOptional = trainerRepository.findById(id);

        if (existingTrainerOptional.isPresent()) {
            Trainer existingTrainer = existingTrainerOptional.get();

            existingTrainer.setFirstName(updatedTrainer.getFirstName());
            existingTrainer.setLastName(updatedTrainer.getLastName());
            existingTrainer.setUsername(updatedTrainer.getUsername());
            existingTrainer.setPassword(updatedTrainer.getPassword());
            existingTrainer.setIsActive(updatedTrainer.getIsActive());
            existingTrainer.setTrainingType(trainingTypeService.getTrainingTypeById(updatedTrainer.getTrainingType().getId()).orElse(null));

            Trainer updated = trainerRepository.save(existingTrainer);
            log.info("Trainer with ID {} updated successfully", id);
            return updated;
        } else {
            log.warn("Trainer with ID {} not found", id);
            throw new EntityNotFoundException("Trainer with id " + id + " not found");
        }
    }

    public void deleteTrainer(Long id) {
        log.info("Deleting trainer with ID: {}", id);
        trainerRepository.deleteById(id);
    }

    public Optional<Trainer> getTrainerByUsername(String username) {
        return trainerRepository.findByUsername(username);
    }

    public void updateTrainerPassword(String username, String newPassword) {
        Optional<Trainer> trainerOptional = trainerRepository.findByUsername(username);

        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.setPassword(newPassword);
            trainerRepository.save(trainer);
        } else {
            throw new EntityNotFoundException("Trainer with username " + username + " not found");
        }
    }

    public void updateTrainerStatus(Long id, boolean isActive) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(id);

        if (trainerOptional.isPresent()) {
            Trainer trainer = trainerOptional.get();
            trainer.setIsActive(isActive);
            trainerRepository.save(trainer);
            log.info("Trainer with ID {} status updated to isActive={}", id, isActive);
        } else {
            throw new EntityNotFoundException("Trainer with ID " + id + " not found");
        }
    }

    public List<Training> getTrainerTrainingList(String username, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingTypeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);

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
            Join<Training, Trainer> trainerJoin = root.join("trainee");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(trainerJoin.get("firstName"), "%" + traineeName + "%"),
                    criteriaBuilder.like(trainerJoin.get("lastName"), "%" + traineeName + "%")
            ));
        }

        // Add trainingTypeName condition if provided
        if (trainingTypeName != null && !trainingTypeName.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("trainingType").get("trainingTypeName"), "%" + trainingTypeName + "%"));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Training> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Trainer> getAvailableTrainersByTrainee(Optional<Trainee> traineeOptional) {
        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Trainer> criteriaQuery = criteriaBuilder.createQuery(Trainer.class);
            Root<Trainer> root = criteriaQuery.from(Trainer.class);

            Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
            Root<Training> trainingRoot = subquery.from(Training.class);
            subquery.select(trainingRoot.get("trainer").get("id"));
            subquery.where(criteriaBuilder.equal(trainingRoot.get("trainee"), trainee));

            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.not(root.get("id").in(subquery)));

            TypedQuery<Trainer> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        } else {
            // If trainee is not present, return an empty list or handle accordingly
            return Collections.emptyList();
        }
    }



    private String calculateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String calculatedUsername = baseUsername.toLowerCase(Locale.ROOT);
        int counter = 1;

        while (trainerRepository.existsByUsername(calculatedUsername)) {
            calculatedUsername = baseUsername + counter++;
        }
        return calculatedUsername;
    }

    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
