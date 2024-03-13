package com.example.gymcrmcoremvc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{

//public class CustomAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        setDefaultFailureUrl("/login?error=true");

        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = "Invalid username or password";

        if(exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            errorMessage = "Invalid username or password";
        }else if(exception.getClass().isAssignableFrom(LockedException.class)) {
            errorMessage = "Account is blocked";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
    }


}