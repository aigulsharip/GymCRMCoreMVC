package com.example.gymcrmcoremvc.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String transactionId = UUID.randomUUID().toString();
        request.setAttribute("transactionId", transactionId);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Endpoint: " + request.getRequestURI());
        System.out.println("Request: " + request.getMethod());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Response Status: " + response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String transactionId = (String) request.getAttribute("transactionId");
        if (ex != null) {
            System.out.println("Error Response: " + ex.getMessage());
        }
        System.out.println("Request Completed for Transaction ID: " + transactionId);
    }
}

