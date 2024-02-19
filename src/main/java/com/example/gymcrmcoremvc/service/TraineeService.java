package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeProfileResponse;
import com.example.gymcrmcoremvc.entity.trainee.TraineeUpdateRequest;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerInfo;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingRepository trainingRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
    }

    public Optional<Trainee> authenticateTrainee(String username, String password) {
        log.info("Authenticating trainee with username: {}", username);
        return traineeRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Trainee> findTraineeByUsername(String username) {
        log.info("Finding trainee by username: {}", username);
        return traineeRepository.findByUsername(username);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for trainee with username: {}", username);
        Optional<Trainee> authenticatedTrainee = authenticateTrainee(username, oldPassword);
        if (authenticatedTrainee.isPresent()) {
            Trainee trainee = authenticatedTrainee.get();
            trainee.setPassword(newPassword);
            traineeRepository.save(trainee);
            log.info("Password changed successfully for trainee with username: {}", username);
        }
    }

    public TraineeProfileResponse getTraineeProfile(String username) {
        log.info("Getting profile for trainee with username: {}", username);
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            return buildTraineeProfileResponse(trainee);
        }
        return null;
    }

    public TraineeProfileResponse updateTraineeProfile(String username, TraineeUpdateRequest request) {
        log.info("Updating profile for trainee with username: {}", username);
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
        log.info("Deleting profile for trainee with username: {}", username);
        Optional<Trainee> authenticatedTrainee = findTraineeByUsername(username);
        if (authenticatedTrainee.isPresent()) {
            Trainee trainee = authenticatedTrainee.get();
            List<Training> trainings = trainingRepository.findByTrainee(trainee);
            if (!trainings.isEmpty()) {
                trainingRepository.deleteAll(trainings);
                log.info("Deleted {} trainings associated with trainee username: {}", trainings.size(), username);
            }
            traineeRepository.delete(trainee);
            log.info("Deleted trainee profile with username: {}", username);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        log.info("Fetching all trainees");
        return traineeRepository.findAll();
    }

    public void activateDeactivateTrainee(String username, boolean isActive) {
        log.info("Activating/deactivating trainee with username: {}", username);
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            trainee.setIsActive(isActive);
            traineeRepository.save(trainee);
        } else {
            throw new EntityNotFoundException("Trainee not found with username: " + username);
        }
    }

    public List<TrainerInfo> getNotAssignedActiveTrainers(String traineeUsername) {
        log.info("Fetching not assigned active trainers for trainee with username: {}", traineeUsername);
        Trainee trainee = traineeRepository.findByUsername(traineeUsername).orElseThrow(() -> new RuntimeException("Trainee not found"));
        List<Trainer> assignedTrainers = trainee.getTrainers();
        List<Trainer> allTrainers = trainerRepository.findAll();
        List<TrainerInfo> trainersNotAssigned = new ArrayList<>();
        for (Trainer trainer : allTrainers) {
            if (!assignedTrainers.contains(trainer) && trainer.getIsActive()) {
                TrainerInfo trainerInfo = new TrainerInfo();
                trainerInfo.setUsername(trainer.getUsername());
                trainerInfo.setFirstName(trainer.getFirstName());
                trainerInfo.setLastName(trainer.getLastName());
                trainerInfo.setTrainingType(trainer.getTrainingType());
                trainersNotAssigned.add(trainerInfo);
            }
        }
        return trainersNotAssigned;
    }

    public List<TrainerInfo> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        log.info("Updating trainers for trainee with username: {}", traineeUsername);
        Optional<Trainee> optionalTrainee = traineeRepository.findByUsername(traineeUsername);
        Trainee trainee = optionalTrainee.orElseThrow(() -> new EntityNotFoundException("Trainee not found"));
        List<Trainer> trainers = trainerRepository.findByUsernameIn(trainerUsernames);
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
        return trainers.stream()
                .map(this::mapToTrainerProfileResponse)
                .collect(Collectors.toList());
    }

    private TrainerInfo mapToTrainerProfileResponse(Trainer trainer) {
        return new TrainerInfo(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getTrainingType()
        );
    }
}
