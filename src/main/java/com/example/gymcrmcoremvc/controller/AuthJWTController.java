package com.example.gymcrmcoremvc.controller;

import com.example.gymcrmcoremvc.jwt.AuthenticationDTO;
import com.example.gymcrmcoremvc.jwt.JWTUtil;
import com.example.gymcrmcoremvc.jwt.UserDTO;
import com.example.gymcrmcoremvc.security.LoginAttemptService;
import com.example.gymcrmcoremvc.security.User;
import com.example.gymcrmcoremvc.security.UserDetailsX;
import com.example.gymcrmcoremvc.service.RegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthJWTController {

    @Autowired
    private RegistrationService registrationService;
    private LoginAttemptService loginAttemptService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/check")
    public String check() {
        String token = jwtUtil.generateToken("Aigul");
        System.out.println(token);
        //return Map.of("jwt-token", token);
        return "working";
    }


    @PostMapping("/register-jwt") // Specifies that this endpoint produces JSON responses
    public Map<String, String> performRegistrationJWT(@RequestBody @Valid UserDTO userDTO,
                                                      BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        String token = jwtUtil.generateToken(user.getUsername());
        registrationService.register(user);

        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        return Map.of("jwt-token", token);
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/showUserInfo")
    @ResponseBody
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsX userDetails = (UserDetailsX) authentication.getPrincipal();
        //return "hello";
        return userDetails.getUsername();
    }



    public User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }

}
