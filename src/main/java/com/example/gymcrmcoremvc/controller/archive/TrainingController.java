package com.example.gymcrmcoremvc.controller.archive;

import com.example.gymcrmcoremvc.entity.trainee.Trainee;
import com.example.gymcrmcoremvc.entity.trainer.Trainer;
import com.example.gymcrmcoremvc.entity.Training;
import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import com.example.gymcrmcoremvc.service.TrainingService;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainings")
@Slf4j
public class TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingController(TrainingService trainingService, TraineeService traineeService,
                              TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    public String getAllTrainings(Model model) {
        List<Training> trainings = trainingService.getAllTrainings();
        model.addAttribute("trainings", trainings);
        return "training/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Trainee> trainees = traineeService.getAllTrainees();
        List<Trainer> trainers = trainerService.getAllTrainers();
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();

        model.addAttribute("training", new Training());
        model.addAttribute("trainees", trainees);
        model.addAttribute("trainers", trainers);
        model.addAttribute("trainingTypes", trainingTypes);

        return "training/add";
    }

    @PostMapping("/add")
    public String addTraining(@ModelAttribute Training training) {
        trainingService.saveTraining(training);
        return "redirect:/trainings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Training> training = trainingService.getTrainingById(id);
        List<Trainee> trainees = traineeService.getAllTrainees();
        List<Trainer> trainers = trainerService.getAllTrainers();
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();

        model.addAttribute("training", training.orElse(new Training()));
        model.addAttribute("trainees", trainees);
        model.addAttribute("trainers", trainers);
        model.addAttribute("trainingTypes", trainingTypes);

        return "training/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTraining(@PathVariable Long id, @ModelAttribute Training training) {
        training.setId(id);
        trainingService.saveTraining(training);
        return "redirect:/trainings";
    }

    @GetMapping("/delete/{id}")
    public String deleteTraining(@PathVariable Long id) {
        trainingService.deleteTraining(id);
        return "redirect:/trainings";
    }
}

