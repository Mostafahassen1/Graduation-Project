package com.codemeet.utils.dto.user;

import com.codemeet.entity.Gender;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
    @NotNull
    Integer userId,
    
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
    Gender gender,

    String bio
) {
}
