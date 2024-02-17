package com.example.gymcrmcoremvc.entity.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerRegistrationResponse {
    private String username;
    private String password;

}
