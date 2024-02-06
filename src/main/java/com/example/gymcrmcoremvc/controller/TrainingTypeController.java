package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/training-types")
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @GetMapping
    public String getAllTrainingTypes(Model model) {
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        model.addAttribute("trainingTypes", trainingTypes);
        return "trainingtype/list";
    }
}

