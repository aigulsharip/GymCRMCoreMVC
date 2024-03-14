package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.entity.Trainee;
import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.entity.TrainingType;
import com.example.gymcrmcoremvc.security.LoginAttemptService;
import com.example.gymcrmcoremvc.security.User;
import com.example.gymcrmcoremvc.service.RegistrationService;
import com.example.gymcrmcoremvc.service.TrainingTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
@Slf4j
public class RegistrationLoginController {

    private final RegistrationService registrationService;
    private final TrainingTypeService trainingTypeService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    public RegistrationLoginController(RegistrationService registrationService, TrainingTypeService trainingTypeService) {
        this.registrationService = registrationService;
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(user);

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        String ip = getClientIP();
        if (error != null) {
            if (loginAttemptService.isBlocked(ip)) {
                model.addAttribute("errorMessage", "You have been blocked due to too many failed login attempts. Please try again after some time.");
            }
        }
        return "auth/login";
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return "redirect:/";
        } catch (LockedException e) {
            model.addAttribute("loginError", "You are blocked");
            return "auth/login";
        } catch (AuthenticationException e) {
            model.addAttribute("loginErrorgg", "Invalid username or passwordfdd");
            return "auth/login";
        }
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

    @GetMapping("/register-trainer")
    public String showAddTrainerForm(Model model) {
        List<TrainingType> trainingTypes = trainingTypeService.getAllTrainingTypes();
        model.addAttribute("trainer", new Trainer());
        model.addAttribute("trainingTypes", trainingTypes);
        return "trainer/add";
    }

    @PostMapping("/register-trainer")
    public String addTrainer(@Valid @ModelAttribute Trainer trainer, BindingResult bindingResult, Model model) {
        log.info("Received request to register trainer: {}", trainer);
        if (registrationService.isTrainer(trainer.getFirstName(), trainer.getLastName())) {
            log.warn("User {} is already registered as a trainer", trainer);
            model.addAttribute("errorMessage", "User is already registered as a trainer");
            return "trainer/add";
        }

        if (registrationService.isTrainee(trainer.getFirstName(), trainer.getLastName())) {
            log.warn("User {} is already registered as a trainee", trainer);
            model.addAttribute("errorMessage", "User is already registered as a trainee");
            return "trainer/add";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("trainer", trainer);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "trainer/add";
        }

        registrationService.saveTrainer(trainer);
        User user = new User();
        user.setPassword(trainer.getPassword());
        user.setId(trainer.getId());
        user.setUsername(trainer.getUsername());
        user.setRole("ROLE_TRAINER");

        registrationService.registerUserAsTrainer(user);
        return "redirect:/trainers";
    }


}
