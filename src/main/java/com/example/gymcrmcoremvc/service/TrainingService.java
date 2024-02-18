package com.example.gymcrmcoremvc.service;


import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.training.Training;
import com.example.gymcrmcoremvc.entity.training.TrainingRequest;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrainingService {

    private final TrainingRepository trainingRepository;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

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

        // Save the training entity to the database
        trainingRepository.save(training);
    }

    @Transactional(readOnly = true)
    public Optional<Training> getTrainingById(Long id) {
        return trainingRepository.findById(id);
    }

    @Transactional
    public Training saveTraining(Training training) {
        log.info("Saving new training: {}", training);
        return trainingRepository.save(training);
    }

    @Transactional
    public Training updateTraining(Long id, Training updatedTraining) {
        log.info("Updating training with ID {}: {}", id, updatedTraining);
        Optional<Training> existingTrainingOptional = trainingRepository.findById(id);

        if (existingTrainingOptional.isPresent()) {
            Training existingTraining = existingTrainingOptional.get();

            // Update the fields you want to allow modification
            existingTraining.setTrainee(updatedTraining.getTrainee());
            existingTraining.setTrainer(updatedTraining.getTrainer());
            existingTraining.setTrainingType(updatedTraining.getTrainingType());
            existingTraining.setTrainingName(updatedTraining.getTrainingName());
            existingTraining.setTrainingDate(updatedTraining.getTrainingDate());
            existingTraining.setTrainingDuration(updatedTraining.getTrainingDuration());

            Training updated = trainingRepository.save(existingTraining);
            log.info("Training with ID {} updated successfully", id);
            return updated;
        } else {
            // Handle the case where the training with the given id is not found
            log.warn("Training with ID {} not found", id);
            // You may throw an exception or handle it according to your application's logic
            // For simplicity, I'm returning null here
            return null;
        }
    }
    @Transactional
    public void deleteTraining(Long id) {
        log.info("Deleting training with ID: {}", id);
        trainingRepository.deleteById(id);
    }
}
