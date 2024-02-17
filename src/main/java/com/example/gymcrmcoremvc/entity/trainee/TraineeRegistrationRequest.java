package com.example.gymcrmcoremvc.entity.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TraineeRegistrationRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
