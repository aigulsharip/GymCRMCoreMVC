package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.security.User;
import com.example.gymcrmcoremvc.security.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Random;

@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TraineeRepository traineeRepository;

    private final TrainerRepository trainerRepository;

    private final TrainingTypeService trainingTypeService;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, TraineeRepository traineeRepository, TrainerRepository trainerRepository, TrainingTypeService trainingTypeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    @Transactional
    public void registerUserAsTrainee(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_TRAINEE");
        userRepository.save(user);
    }

    @Transactional
    public void registerUserAsTrainer(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_TRAINER");
        userRepository.save(user);
    }

    @Transactional
    public Trainee saveTrainee(Trainee trainee) {
        log.info("Saving new trainee: {}", trainee);
        trainee.setUsername(calculateUsername(trainee.getFirstName(), trainee.getLastName()));
        // using password encoding
        // trainee.setPassword(passwordEncoder.encode(trainee.getPassword()));

        trainee.setPassword(trainee.getPassword());
        trainee.setIsActive(true);
        log.info("Trainee registered successfully: {}", trainee);
        return traineeRepository.save(trainee);
    }

    @Transactional
    public Trainer saveTrainer(Trainer trainer) {
        log.info("Saving new trainer: {}", trainer);
        trainer.setUsername(calculateUsername(trainer.getFirstName(), trainer.getLastName()));
        // using password encoding
        // trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));
        trainer.setPassword(trainer.getPassword());
        trainer.setIsActive(true);
        trainer.setTrainingType(trainingTypeService.getTrainingTypeById(trainer.getTrainingType().getId()).orElse(null));

        return trainerRepository.save(trainer);
    }

    public boolean isTrainer(String firstName, String lastName) {
        log.info("Checking if {} {} is a trainer", firstName, lastName);
        boolean result = trainerRepository.existsByFirstNameAndLastName(firstName, lastName);
        log.info("{} {} is{} a trainer", firstName, lastName, result ? "" : " not");
        return result;
    }

    public boolean isTrainee(String firstName, String lastName) {
        log.info("Checking if {} {} is a trainee", firstName, lastName);
        boolean result = traineeRepository.existsByFirstNameAndLastName(firstName, lastName);
        log.info("{} {} is{} a trainee", firstName, lastName, result ? "" : " not");
        return result;
    }

    private String calculateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String calculatedUsername = baseUsername.toLowerCase(Locale.ROOT);
        int counter = 1;

        while (traineeRepository.existsByUsername(calculatedUsername)) {
            calculatedUsername = baseUsername + counter++;
        }
        return calculatedUsername.toLowerCase();
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
