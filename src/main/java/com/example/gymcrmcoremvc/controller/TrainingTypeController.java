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

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trainingType", new TrainingType());
        return "trainingtype/create";
    }

    @PostMapping("/create")
    public String createTrainingType(@ModelAttribute("trainingType") TrainingType trainingType) {
        trainingTypeService.saveTrainingType(trainingType);
        return "redirect:/training-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<TrainingType> trainingType = trainingTypeService.getTrainingTypeById(id);
        model.addAttribute("trainingType", trainingType.orElse(null));
        return "trainingtype/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTrainingType(@PathVariable("id") Long id, @ModelAttribute("trainingType") TrainingType trainingType) {
        trainingTypeService.saveTrainingType(trainingType);
        return "redirect:/training-types";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainingType(@PathVariable("id") Long id) {
        trainingTypeService.deleteTrainingType(id);
        return "redirect:/training-types";
    }
}

