package com.example.gymcrmcoremvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        Object usr = e.getAuthentication().getPrincipal();
        if(usr instanceof User){
            WebAuthenticationDetails auth = (WebAuthenticationDetails) e.getAuthentication().getDetails();
            loginAttemptService.loginFailed(auth.getRemoteAddress());
        }
    }

}