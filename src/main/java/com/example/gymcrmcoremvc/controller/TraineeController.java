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

//    @GetMapping
//    public String getAllTrainees(Model model) {
//        List<Trainee> trainees = traineeService.getAllTrainees();
//        model.addAttribute("trainees", trainees);
//        return "trainee/list";
//    }

    @GetMapping
    public String getAllTrainees(Model model, @RequestParam(name = "search", required = false) String search) {
        List<Trainee> trainees;

        if (search != null && !search.isEmpty()) {
            // Perform a search based on the provided username
            Optional<Trainee> trainee = traineeService.getTraineeByUsername(search);

            if (trainee.isPresent()) {
                // If a trainee is found, display its profile
                model.addAttribute("trainee", trainee.get());
                return "trainee/profile";
            } else {
                // If no trainee is found, display the list with a message
                trainees = traineeService.getAllTrainees();
                model.addAttribute("trainees", trainees);
                model.addAttribute("search", search);
                model.addAttribute("searchMessage", "No trainee found for username: " + search);
                return "trainee/list";
            }
        } else {
            // If no search criteria provided, display the list of all trainees
            trainees = traineeService.getAllTrainees();
            model.addAttribute("trainees", trainees);
            return "trainee/list";
        }
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

    @GetMapping("/profile/{username}")
    public String viewTraineeProfile(@PathVariable String username, Model model) {
        Optional<Trainee> trainee = traineeService.getTraineeByUsername(username);

        if (trainee.isPresent()) {
            model.addAttribute("trainee", trainee.get());
            return "trainee/profile";
        } else {
            // Handle the case where the trainee with the given username is not found
            return "redirect:/trainees"; // Redirect to the trainee list or another appropriate page
        }
    }

    @PostMapping("/profile/{username}/change-password")
    public String changePassword(@PathVariable String username, @RequestParam String newPassword, Model model) {
        try {
            traineeService.updateTraineePassword(username, newPassword);
            return "redirect:/trainees/profile/" + username.toLowerCase() + "?successMessage=Password changed successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to change password. Please try again.");
            return "redirect:/trainees/profile/" + username.toLowerCase() + "?errorMessage=Failed to change password. Please try again.";
        }
    }





}

