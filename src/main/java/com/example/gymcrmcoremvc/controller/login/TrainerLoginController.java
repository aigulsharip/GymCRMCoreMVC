package com.example.gymcrmcoremvc.controller.login;

import com.example.gymcrmcoremvc.entity.Trainer;
import com.example.gymcrmcoremvc.service.login.TrainerLoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/trainer-login")
public class TrainerLoginController {

    @Autowired
    private TrainerLoginService trainerLoginService;

    @GetMapping()
    public String showLoginForm(Model model) {
        model.addAttribute("trainer", new Trainer());
        return "trainer/login";
    }

    @PostMapping("/authenticate")
    public String authenticateTrainer(Trainer trainer, Model model, HttpSession session) {
        String username = trainer.getUsername();
        String password = trainer.getPassword();

        // Perform login.html authentication
        Optional<Trainer> authenticatedTrainer = trainerLoginService.login(username, password);

        if (authenticatedTrainer.isPresent()) {
            // Successful login.html
            Trainer loggedInTrainer = authenticatedTrainer.get();

            // Store the Trainer ID in the session
            session.setAttribute("loggedInTrainerId", loggedInTrainer.getId());

            // Redirect to the Trainer's edit page
            return "redirect:/trainers/edit/" + loggedInTrainer.getId();
        } else {
            // Failed login.html
            model.addAttribute("error", "Invalid username or password");
            return "trainer/login.html";
        }
    }

}
