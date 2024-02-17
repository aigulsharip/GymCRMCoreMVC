package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainee.TraineeRegistrationResponse;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationRequest;
import com.example.gymcrmcoremvc.entity.trainer.TrainerRegistrationResponse;
import com.example.gymcrmcoremvc.repository.TraineeRepository;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
@Slf4j
public class RegistrationLoginService {

    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;

    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;



    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RegistrationLoginService(TraineeRepository traineeRepository, TrainingRepository trainingRepository, TrainerRepository trainerRepository, TrainingTypeService trainingTypeService) {
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeService = trainingTypeService;
    }

    public TraineeRegistrationResponse registerTrainee (TraineeRegistrationRequest traineeRegistrationRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Trainee trainee = modelMapper.map(traineeRegistrationRequest, Trainee.class);
        trainee.setUsername(calculateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());
        trainee.setIsActive(true);
        trainee = traineeRepository.save(trainee);
        return modelMapper.map(trainee, TraineeRegistrationResponse.class);
    }

    public TrainerRegistrationResponse registerTrainer (TrainerRegistrationRequest trainerRegistrationRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Trainer trainer = modelMapper.map(trainerRegistrationRequest, Trainer.class);
        trainer.setUsername(calculateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());
        trainer.setIsActive(true);
        trainer = trainerRepository.save(trainer);
        return modelMapper.map(trainer, TrainerRegistrationResponse.class);
    }




    private String calculateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String calculatedUsername = baseUsername.toLowerCase(Locale.ROOT);
        int counter = 1;

        while (traineeRepository.existsByUsername(calculatedUsername) || trainerRepository.existsByUsername(calculatedUsername)) {
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




