package com.example.gymcrmcoremvc.entity.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLoginRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
}
