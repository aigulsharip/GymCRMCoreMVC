package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public List<Trainee> getAllTrainees() {
        log.info("Fetching all trainees");
        return traineeRepository.findAll();
    }

    public Optional<Trainee> getTraineeById(Long id) {
        return traineeRepository.findById(id);
    }

    public Trainee saveTrainee(Trainee trainee) {
        log.info("Saving new trainee: {}", trainee);
        trainee.setUsername(calculateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());
        trainee.setIsActive(true);
        return traineeRepository.save(trainee);
    }

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

    public void deleteTrainee(Long id) {
        log.info("Deleting trainee with ID: {}", id);
        traineeRepository.deleteById(id);
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        return traineeRepository.findByUsername(username);
    }

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

    public void updateTraineeStatus(Long id, boolean isActive) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(id);

        if (traineeOptional.isPresent()) {
            Trainee trainee = traineeOptional.get();
            trainee.setIsActive(isActive);
            traineeRepository.save(trainee);
            log.info("Trainee with ID {} status updated to isActive={}", id, isActive);
        } else {
            throw new EntityNotFoundException("Trainee with ID " + id + " not found");
        }
    }

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
}
