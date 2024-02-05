package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.Training;
import com.example.gymcrmcoremvc.service.TraineeService;
import com.example.gymcrmcoremvc.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainees")
public class TraineeController {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

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
                return "trainee/edit";
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
            return "trainee/edit";
        } else {
            // Handle the case where the trainee with the given username is not found
            return "redirect:/trainees"; // Redirect to the trainee list or another appropriate page
        }
    }

    @PostMapping("/profile/{username}/change-password")
    public String changePassword(@PathVariable String username, @RequestParam String newPassword, Model model) {
        Trainee trainee = traineeService.getTraineeByUsername(username).orElse(null);
        try {
            traineeService.updateTraineePassword(username, newPassword);
            return "redirect:/trainees/edit/" + trainee.getId() + "?successMessage=Password changed successfully";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to change password. Please try again.");
            return "redirect:/trainees/edit/" + trainee.getId() + "?errorMessage=Failed to change password. Please try again.";
        }
    }

    @GetMapping("/activate/{id}")
    public String activateTrainee(@PathVariable Long id) {
        traineeService.updateTraineeStatus(id, true);
        return "redirect:/trainees";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateTrainee(@PathVariable Long id) {
        traineeService.updateTraineeStatus(id, false);
        return "redirect:/trainees";
    }

    @GetMapping("/delete-by-username/{username}")
    public String deleteTraineeByUsername(@PathVariable String username) {
        traineeService.deleteTraineeByUsername(username);
        return "redirect:/trainees";
    }

//    @GetMapping("/training-list")
//    public String getTraineeTrainingList(
//            @RequestParam(required = false) String username,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
//            @RequestParam(required = false) String trainerName,
//            @RequestParam(required = false) String trainingTypeName,
//            Model model
//    ) {
//        List<Training> trainingList = traineeService.getTraineeTrainingList(username, fromDate, toDate, trainerName, trainingTypeName);
//        model.addAttribute("trainingList", trainingList);
//        List<Trainee> trainees = traineeService.getAllTrainees();
//        model.addAttribute("trainees", trainees);
//        model.addAttribute("trainee", new Trainee());  // Add an empty trainee object for binding
//        return "trainee/training-list";
//    }

    @GetMapping("/training-list")
    public String getTraineeTrainingList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingTypeName,
            Model model
    ) {
        List<Training> trainingList = traineeService.getTraineeTrainingList(username, fromDate, toDate, trainerName, trainingTypeName);
        model.addAttribute("trainingList", trainingList);

        // Get all trainees for the dropdown
        List<Trainee> trainees = traineeService.getAllTrainees();
        model.addAttribute("trainees", trainees);
        model.addAttribute("trainee", new Trainee());  // Add an empty trainee object for binding
        model.addAttribute("trainer", new Trainer());

        Optional<Trainee> trainee = traineeService.getTraineeByUsername(username);


        // Get the list of trainers not assigned to the trainee
        List<Trainer> availableTrainers = trainerService.getAvailableTrainersByTrainee(trainee  );

        // Add the list of available trainers to the model
        model.addAttribute("availableTrainers", availableTrainers);

        return "trainee/training-list";
    }








}

