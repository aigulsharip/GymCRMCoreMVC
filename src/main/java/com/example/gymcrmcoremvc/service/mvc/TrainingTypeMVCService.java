package com.example.gymcrmcoremvc.service.mvc;

import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeMVCService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeMVCService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TrainingType> getTrainingTypeById(Long id) {
        return trainingTypeRepository.findById(id);
    }

}
