package com.example.gymcrmcoremvc.service;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import com.example.gymcrmcoremvc.entity.trainingType.TrainingTypeResponse;
import com.example.gymcrmcoremvc.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<TrainingTypeResponse> getAllTrainingTypesList() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream()
                .map(this::mapToTrainingTypeResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }

    private TrainingTypeResponse mapToTrainingTypeResponse(TrainingType trainingType) {
        TrainingTypeResponse response = new TrainingTypeResponse();
        response.setId(trainingType.getId());
        response.setTrainingTypeName(trainingType.getTrainingTypeName());
        return response;
    }

    @Transactional(readOnly = true)
    public Optional<TrainingType> getTrainingTypeById(Long id) {
        return trainingTypeRepository.findById(id);
    }

}
