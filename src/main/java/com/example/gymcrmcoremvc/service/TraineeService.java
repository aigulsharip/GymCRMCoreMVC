package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.*;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeProfileResponse;
import com.example.gymcrmcoremvc.entity.trainee.TraineeUpdateRequest;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
    }



    public Optional<Trainee> authenticateTrainee(String username, String password) {
        // Check if the provided username and password match any trainee in the database
        return traineeRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Trainee> findTraineeByUsername(String username) {
        // Check if the provided username and password match any trainee in the database
        return traineeRepository.findByUsername(username);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<Trainee> authenticatedTrainee = authenticateTrainee(username, oldPassword);
        if (authenticatedTrainee.isPresent()) {
            Trainee trainee = authenticatedTrainee.get();
            trainee.setPassword(newPassword);
            traineeRepository.save(trainee);
        }

    }


    public TraineeProfileResponse getTraineeProfile(String username) {
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            return buildTraineeProfileResponse(trainee);
        }
        return null;
    }


    public TraineeProfileResponse updateTraineeProfile(String username, TraineeUpdateRequest request) {
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            trainee.setFirstName(request.getFirstName());
            trainee.setLastName(request.getLastName());
            trainee.setDateOfBirth(request.getDateOfBirth());
            trainee.setAddress(request.getAddress());
            trainee.setIsActive(request.isActive());

            traineeRepository.save(trainee);
            return buildTraineeProfileResponse(trainee);
        }
        return null;
    }



    private TraineeProfileResponse buildTraineeProfileResponse(Trainee trainee) {
        TraineeProfileResponse profileResponse = new TraineeProfileResponse();
        profileResponse.setFirstName(trainee.getFirstName());
        profileResponse.setLastName(trainee.getLastName());
        profileResponse.setDateOfBirth(trainee.getDateOfBirth());
        profileResponse.setAddress(trainee.getAddress());
        profileResponse.setActive(trainee.getIsActive());

        List<TrainerInfo> trainerInfos = new ArrayList<>();
        for (Trainer trainer : trainee.getTrainers()) {
            TrainerInfo trainerInfo = new TrainerInfo();
            trainerInfo.setUsername(trainer.getUsername());
            trainerInfo.setFirstName(trainer.getFirstName());
            trainerInfo.setLastName(trainer.getLastName());
            trainerInfo.setTrainingType(trainer.getTrainingType());
            trainerInfos.add(trainerInfo);
        }
        profileResponse.setTrainers(trainerInfos);

        return profileResponse;
    }



    public boolean deleteTraineeProfile(String username) {
        Optional<Trainee> authenticatedTrainee = findTraineeByUsername(username);
        if (authenticatedTrainee.isPresent()) {
            Trainee trainee = authenticatedTrainee.get();
            traineeRepository.delete(trainee);
            return true; // Trainee profile deleted successfully
        }
        return false; // Authentication failed or trainee profile not found
    }

    public List<TrainerInfo> getNotAssignedActiveTrainers(String username) {
        Trainee trainee = traineeRepository.findByUsername(username).orElse(null);
        if (trainee != null && trainee.getIsActive()) {
            List<Trainer> allActiveTrainers = trainerRepository.findByIsActiveTrue();
            List<TrainerInfo> notAssignedActiveTrainers = new ArrayList<>();
            for (Trainer trainer : allActiveTrainers) {
                if (!trainee.getTrainers().contains(trainer)) {
                    TrainerInfo trainerInfo = new TrainerInfo();
                    trainerInfo.setUsername(trainer.getUsername());
                    trainerInfo.setFirstName(trainer.getFirstName());
                    trainerInfo.setLastName(trainer.getLastName());
                    trainerInfo.setTrainingType(trainer.getTrainingType());
                    notAssignedActiveTrainers.add(trainerInfo);
                }
            }
            return notAssignedActiveTrainers;
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        log.info("Fetching all trainees");
        return traineeRepository.findAll();
    }

    private String calculateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String calculatedUsername = baseUsername.toLowerCase(Locale.ROOT);
        int counter = 1;

        while (traineeRepository.existsByUsername(calculatedUsername)) {
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


    @Transactional(readOnly = true)
    public Optional<Trainee> getTraineeById(Long id) {
        log.info("Fetching trainee by ID: {}", id);
        return traineeRepository.findById(id);
    }

    @Transactional
    public Trainee saveTrainee(Trainee trainee) {
        log.info("Saving new trainee: {}", trainee);
        trainee.setUsername(calculateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());
        trainee.setIsActive(true);
        return traineeRepository.save(trainee);
    }

    @Transactional
    public Trainee updateTrainee(Long id, Trainee updatedTrainee) {
        log.info("Updating trainee with ID {}: {}", id, updatedTrainee);
        Optional<Trainee> existingTraineeOptional = traineeRepository.findById(id);

        if (existingTraineeOptional.isPresent()) {
            Trainee existingTrainee = existingTraineeOptional.get();

            // Update the fields you want to allow modification
            existingTrainee.setFirstName(updatedTrainee.getFirstName());
            existingTrainee.setLastName(updatedTrainee.getLastName());
            existingTrainee.setUsername(updatedTrainee.getUsername());
            existingTrainee.setPassword(updatedTrainee.getPassword());
            existingTrainee.setIsActive(updatedTrainee.getIsActive());
            existingTrainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            existingTrainee.setAddress(updatedTrainee.getAddress());

            Trainee updated = traineeRepository.save(existingTrainee);
            log.info("Trainee with ID {} updated successfully", id);
            return updated;
        } else {
            // Handle the case where the trainee with the given id is not found
            log.warn("Trainee with ID {} not found", id);
            throw new EntityNotFoundException("Trainee with id " + id + " not found");
        }
    }

    @Transactional
    public void toggleTraineeStatus(Long traineeId) {
        Optional<Trainee> optionalTrainee = traineeRepository.findById(traineeId);

        optionalTrainee.ifPresent(trainee -> {
            trainee.setIsActive(!trainee.getIsActive());
            traineeRepository.save(trainee);
        });
    }

    @Transactional
    public void deleteTrainee(Long traineeId) {
        Trainee trainee = traineeRepository.findById(traineeId)
                .orElseThrow(() -> new EntityNotFoundException("Trainee not found with id: " + traineeId));

        List<Training> trainings = trainingRepository.findByTraineeId(traineeId);
        for (Training training : trainings) {
            trainingRepository.delete(training);
        }

        traineeRepository.delete(trainee);
    }

    @Transactional(readOnly = true)
    public Optional<Trainee> getTraineeByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        return traineeRepository.findByUsername(username);
    }

    @Transactional
    public Trainee updateTraineePassword(String username, String newPassword) {
        Optional<Trainee> traineeOptional = getTraineeByUsername(username);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.setPassword(newPassword);
            return traineeRepository.save(trainee);
        } else {
            // Handle the case where the trainee with the given username is not found
            throw new EntityNotFoundException("Trainee with username " + username + " not found");
        }
    }



    @Transactional
    public void deleteTraineeByUsername(String username) {
        Optional<Trainee> traineeOptional = traineeRepository.findByUsername(username);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            traineeRepository.delete(trainee);
            log.info("Trainee with username {} deleted successfully", username);
        } else {
            throw new EntityNotFoundException("Trainee with username " + username + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainingList(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingTypeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);

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
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(trainerJoin.get("firstName"), "%" + trainerName + "%"),
                    criteriaBuilder.like(trainerJoin.get("lastName"), "%" + trainerName + "%")
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








}
