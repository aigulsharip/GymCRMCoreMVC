package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.security.User;
import com.example.gymcrmcoremvc.security.UserValidator;
import com.example.gymcrmcoremvc.security.RegistrationService;
import com.example.gymcrmcoremvc.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final TraineeService traineeService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator, TraineeService traineeService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.traineeService = traineeService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(user);

        return "redirect:/auth/login";
    }

    @GetMapping("/register-trainee")
    public String showAddForm(Model model) {
        model.addAttribute("trainee", new Trainee());
        return "trainee/add";
    }

    @PostMapping("/register-trainee")
    public String addTrainee(@Valid @ModelAttribute Trainee trainee, BindingResult bindingResult, Model model) {
        log.info("Received request to register trainee: {}", trainee);
        if (registrationService.isTrainer(trainee.getFirstName(), trainee.getLastName())) {
            log.warn("User {} is already registered as a trainer", trainee);
            model.addAttribute("errorMessage", "User is already registered as a trainer");

            return "trainee/add";
        }

        if (registrationService.isTrainee(trainee.getFirstName(), trainee.getLastName())) {
            log.warn("User {} is already registered as a trainee", trainee);
            model.addAttribute("errorMessage", "User is already registered as a trainee");

            return "trainee/add";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("trainee", trainee);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "trainee/add"; // Assuming trainee_form is your form view
        }
        registrationService.saveTrainee(trainee);
        User user = new User();
        user.setPassword(trainee.getPassword());
        user.setId(trainee.getId());
        user.setUsername(trainee.getUsername());
        user.setRole("ROLE_TRAINEE"); // Assuming you have a role for trainees

        registrationService.registerUserAsTrainee(user);

        return "redirect:/trainees";

    }


}
