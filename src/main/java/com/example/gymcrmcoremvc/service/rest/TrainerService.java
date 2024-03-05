package com.example.gymcrmcoremvc.service.rest;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeInfo;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerUpdateRequest;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
//import jakarta.persistence.EntityNotFoundException;
import javax.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingTypeService trainingTypeService) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
    }

    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        log.info("Fetching all trainers");
        return trainerRepository.findAll();
    }

    public Optional<Trainer> authenticateTrainer(String username, String password) {
        log.info("Authenticating trainer with username: {}", username);
        Optional<Trainer> result = trainerRepository.findByUsernameAndPassword(username, password);
        if (result.isPresent()) {
            log.info("Trainer authentication successful for username: {}", username);
        } else {
            log.warn("Trainer authentication failed for username: {}", username);
        }
        return result;
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for trainer with username: {}", username);
        Optional<Trainer> authenticatedTrainer = authenticateTrainer(username, oldPassword);
        if (authenticatedTrainer.isPresent()) {
            Trainer trainer = authenticatedTrainer.get();
            trainer.setPassword(newPassword);
            trainerRepository.save(trainer);
            log.info("Password changed successfully for trainer with username: {}", username);
        } else {
            log.warn("Password change failed. Trainer with username {} is not authenticated.", username);
        }
    }

    public TrainerProfileResponse getTrainerProfile(String username) {
        log.info("Fetching trainer profile for username: {}", username);
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            log.info("Trainer profile found for username: {}", username);
            return buildTrainerProfileResponse(trainer);
        } else {
            log.warn("Trainer profile not found for username: {}", username);
            return null;
        }
    }

    public TrainerProfileResponse updateTrainerProfile(String username, TrainerUpdateRequest request) {
        log.info("Updating trainer profile for username: {}", username);
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            // Update fields
            trainer.setFirstName(request.getFirstName());
            trainer.setLastName(request.getLastName());
            trainer.setIsActive(request.isActive());

            trainerRepository.save(trainer);
            log.info("Trainer profile updated successfully for username: {}", username);
            return buildTrainerProfileResponse(trainer);
        } else {
            log.warn("Trainer profile update failed. Trainer with username {} not found.", username);
            return null;
        }
    }

    public boolean deleteTrainerProfile(String username) {
        log.info("Deleting trainer profile for username: {}", username);
        Optional<Trainer> authenticatedTrainer = trainerRepository.findByUsername(username);
        if (authenticatedTrainer.isPresent()) {
            Trainer trainer = authenticatedTrainer.get();
            trainerRepository.delete(trainer);
            log.info("Trainer profile deleted successfully for username: {}", username);
            return true; // Trainer profile deleted successfully
        } else {
            log.warn("Trainer profile deletion failed. Trainer with username {} not found.", username);
            return false; // Authentication failed or trainer profile not found
        }
    }

    private TrainerProfileResponse buildTrainerProfileResponse(Trainer trainer) {
        log.info("Building trainer profile response for trainer: {}", trainer.getUsername());
        TrainerProfileResponse profileResponse = new TrainerProfileResponse();
        profileResponse.setFirstName(trainer.getFirstName());
        profileResponse.setLastName(trainer.getLastName());
        profileResponse.setSpecialization(trainer.getTrainingType());
        profileResponse.setActive(trainer.getIsActive());

        List<TraineeInfo> traineeInfos = new ArrayList<>();
        for (Trainee trainee : trainer.getTrainees()) {
            TraineeInfo traineeInfo = new TraineeInfo();
            traineeInfo.setUsername(trainee.getUsername());
            traineeInfo.setFirstName(trainee.getFirstName());
            traineeInfo.setLastName(trainee.getLastName());
            traineeInfos.add(traineeInfo);
        }
        profileResponse.setTrainees(traineeInfos);
        log.info("Trainer profile response built successfully for trainer: {}", trainer.getUsername());
        return profileResponse;
    }

    public void activateDeactivateTrainer(String username, boolean isActive) {
        log.info("Activating/deactivating trainer with username: {}", username);
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.setIsActive(isActive);
            trainerRepository.save(trainer);
            log.info("Trainer {} successfully", isActive ? "activated" : "deactivated");
        } else {
            log.warn("Trainer activation/deactivation failed. Trainer with username {} not found.", username);
            throw new EntityNotFoundException("Trainer not found with username: " + username);
        }
    }
}
