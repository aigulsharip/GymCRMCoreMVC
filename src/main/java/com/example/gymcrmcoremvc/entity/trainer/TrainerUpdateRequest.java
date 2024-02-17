package com.example.gymcrmcoremvc.entity.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateRequest {
    private String firstName;
    private String lastName;
    private boolean isActive;
}
