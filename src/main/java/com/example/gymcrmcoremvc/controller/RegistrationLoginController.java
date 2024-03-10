package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.service.RegistrationService;
import com.example.gymcrmcoremvc.security.User;
import com.example.gymcrmcoremvc.security.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller

public class RegistrationLoginController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public RegistrationLoginController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @PostMapping("/registration/trainee")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(user);

        return "redirect:/auth/login";
    }
}
