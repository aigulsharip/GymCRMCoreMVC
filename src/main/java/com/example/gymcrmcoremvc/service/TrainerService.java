package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeInfo;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.trainer.TrainerProfileResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerUpdateRequest;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return trainerRepository.findByUsernameAndPassword(username, password);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<Trainer> authenticatedTrainer = authenticateTrainer(username, oldPassword);
        if (authenticatedTrainer.isPresent()) {
            Trainer trainer = authenticatedTrainer.get();
            trainer.setPassword(newPassword);
            trainerRepository.save(trainer);
        }
    }

    public TrainerProfileResponse getTrainerProfile(String username) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            return buildTrainerProfileResponse(trainer);
        }
        return null;
    }

    public TrainerProfileResponse updateTrainerProfile(String username, TrainerUpdateRequest request) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            // Update fields
            trainer.setFirstName(request.getFirstName());
            trainer.setLastName(request.getLastName());
            trainer.setIsActive(request.isActive());

            trainerRepository.save(trainer);
            return buildTrainerProfileResponse(trainer);
        }
        return null;
    }

    public boolean deleteTrainerProfile(String username) {
        Optional<Trainer> authenticatedTrainer = trainerRepository.findByUsername(username);
        if (authenticatedTrainer.isPresent()) {
            Trainer trainer = authenticatedTrainer.get();
            trainerRepository.delete(trainer);
            return true; // Trainee profile deleted successfully
        }
        return false; // Authentication failed or trainee profile not found
    }

    private TrainerProfileResponse buildTrainerProfileResponse(Trainer trainer) {
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
        return profileResponse;
    }

    public void activateDeactivateTrainer(String username, boolean isActive) {
        Optional<Trainer> optionalTrainer = trainerRepository.findByUsername(username);
        if (optionalTrainer.isPresent()) {
            Trainer trainer = optionalTrainer.get();
            trainer.setIsActive(isActive);
            trainerRepository.save(trainer);
        } else {
            throw new EntityNotFoundException("Trainer not found with username: " + username);
        }
    }

}
