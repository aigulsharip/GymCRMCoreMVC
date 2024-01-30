package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    @GetMapping
    public String getAllTrainees(Model model) {
        List<Trainee> trainees = traineeService.getAllTrainees();
        model.addAttribute("trainees", trainees);
        return "trainee/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trainee", new Trainee());
        return "trainee/add";
    }

    @PostMapping("/add")
    public String addTrainee(@ModelAttribute Trainee trainee) {
        traineeService.saveTrainee(trainee);
        return "redirect:/trainees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Trainee> trainee = traineeService.getTraineeById(id);
        model.addAttribute("trainee", trainee.orElse(new Trainee()));
        return "trainee/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTrainee(@PathVariable Long id, @ModelAttribute Trainee trainee) {
        trainee.setId(id);
        traineeService.saveTrainee(trainee);
        return "redirect:/trainees";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainee(@PathVariable Long id) {
        traineeService.deleteTrainee(id);
        return "redirect:/trainees";
    }
}

