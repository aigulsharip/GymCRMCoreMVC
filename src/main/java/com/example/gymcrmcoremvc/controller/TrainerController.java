package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.service.TrainerService;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeService trainingTypeService;

    @GetMapping
    public String getAllTrainers(Model model, @RequestParam(name = "search", required = false) String search) {
        List<Trainer> trainers;

        if (search != null && !search.isEmpty()) {
            // Perform a search based on the provided username
            Optional<Trainer> trainer = trainerService.getTrainerByUsername(search);

            if (trainer.isPresent()) {
                // If a trainee is found, display its profile
                model.addAttribute("trainer", trainer.get());
                return "trainer/profile";
            } else {
                // If no trainee is found, display the list with a message
                trainers = trainerService.getAllTrainers();
                model.addAttribute("trainers", trainers);
                model.addAttribute("search", search);
                model.addAttribute("searchMessage", "No trainer found for username: " + search);
                return "trainer/list";
            }
        } else {
            // If no search criteria provided, display the list of all trainees
            trainers = trainerService.getAllTrainers();
            model.addAttribute("trainers", trainers);
            return "trainer/list";
        }
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        model.addAttribute("trainer", new Trainer());
        model.addAttribute("trainingTypes", trainingTypes);
        return "trainer/add";
    }

    @PostMapping("/add")
    public String addTrainer(@ModelAttribute Trainer trainer) {
        trainerService.saveTrainer(trainer);
        return "redirect:/trainers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Trainer> trainer = trainerService.getTrainerById(id);
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        model.addAttribute("trainer", trainer.orElse(new Trainer()));
        model.addAttribute("trainingTypes", trainingTypes);
        return "trainer/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTrainer(@PathVariable Long id, @ModelAttribute Trainer trainer) {
        trainer.setId(id);
        trainerService.updateTrainer(id, trainer);
        return "redirect:/trainers";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return "redirect:/trainers";
    }

    @GetMapping("/profile/{username}")
    public String viewTrainerProfile(@PathVariable String username, Model model) {
        Optional<Trainer> trainer = trainerService.getTrainerByUsername(username);

        if (trainer.isPresent()) {
            model.addAttribute("trainer", trainer.get());
            return "trainer/profile";
        } else {
            // Handle the case where the trainee with the given username is not found
            return "redirect:/trainers"; // Redirect to the trainee list or another appropriate page
        }
    }

    @PostMapping("/profile/{username}/change-password")
    public String changePassword(@PathVariable String username, @RequestParam String newPassword, Model model) {
        try {
            trainerService.updateTrainerPassword(username, newPassword);
            model.addAttribute("successMessage", "Password changed successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to change password. Please try again.");
        }

        return "redirect:/trainers/profile/" + username + "?successMessage=Password changed successfully!";
    }

    @GetMapping("/activate/{id}")
    public String activateTrainer(@PathVariable Long id) {
        trainerService.updateTrainerStatus(id, true);
        return "redirect:/trainers";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateTrainer(@PathVariable Long id) {
        trainerService.updateTrainerStatus(id, false);
        return "redirect:/trainers";
    }
}
