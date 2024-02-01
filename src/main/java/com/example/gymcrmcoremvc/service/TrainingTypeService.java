package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    public Optional<TrainingType> getTrainingTypeById(Long id) {
        return trainingTypeRepository.findById(id);
    }

    public TrainingType saveTrainingType(TrainingType trainingType) {
        return trainingTypeRepository.save(trainingType);
    }

    public TrainingType updateTrainingType(Long id, TrainingType updatedTrainingType) {
        Optional<TrainingType> existingTrainingTypeOptional = trainingTypeRepository.findById(id);

        if (existingTrainingTypeOptional.isPresent()) {
            TrainingType existingTrainingType = existingTrainingTypeOptional.get();
            existingTrainingType.setTrainingTypeName(updatedTrainingType.getTrainingTypeName());

            return trainingTypeRepository.save(existingTrainingType);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TrainingType not found");
        }
    }

    public void deleteTrainingType(Long id) {
        trainingTypeRepository.deleteById(id);
    }
}
