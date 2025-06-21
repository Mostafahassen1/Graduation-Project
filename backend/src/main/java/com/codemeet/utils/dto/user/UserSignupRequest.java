package com.codemeet.utils.dto.user;

import com.codemeet.entity.Gender;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSignupRequest(
    @NotNull
    @NotBlank
    @Length(max = 25)
    String firstName,
    
    @NotNull
    @NotBlank
    @Length(max = 25)
    String lastName,
    
    @NotNull
    @NotBlank
    @Length(max = 20)
    String username,
    
    @NotNull
    @NotBlank
    @Length(max = 100) // Minimum length is determined by the pattern
    String email,
    
    @NotNull
    @NotBlank
    @Length(
        min = 8,
        max = 100
    )
    String password,
    
    @NotNull
    @NotBlank
    @Length(
        min = 11,
        max = 11
    )
    String phoneNumber,
    
    @NotNull
    Gender gender
) {
}
