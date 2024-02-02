package com.example.gymcrmcoremvc.service.login;

import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.repository.TrainerRepository;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainerLoginService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerLoginService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }


    public Optional<Trainer> login(String username, String password) {
        return trainerRepository.findByUsernameAndPassword(username, password);
    }
}
