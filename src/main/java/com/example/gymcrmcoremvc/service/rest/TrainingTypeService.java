package com.example.gymcrmcoremvc.service.rest;

import com.example.gymcrmcoremvc.entity.trainingType.TrainingType;
import com.example.gymcrmcoremvc.entity.trainingType.TrainingTypeResponse;
import com.example.gymcrmcoremvc.repository.TrainingTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<TrainingTypeResponse> getAllTrainingTypesList() {
        log.info("Fetching all training types");
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        log.info("Total {} training types fetched", trainingTypes.size());
        return trainingTypes.stream()
                .map(this::mapToTrainingTypeResponse)
                .collect(Collectors.toList());
    }

    private TrainingTypeResponse mapToTrainingTypeResponse(TrainingType trainingType) {
        log.debug("Mapping training type to response: {}", trainingType);
        TrainingTypeResponse response = new TrainingTypeResponse();
        response.setId(trainingType.getId());
        response.setTrainingTypeName(trainingType.getTrainingTypeName());
        log.debug("Training type mapped to response: {}", response);
        return response;
    }
}
