package com.example.gymcrmcoremvc.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private String username;

    private String password;

    private LocalDate dateOfBirth;

}
