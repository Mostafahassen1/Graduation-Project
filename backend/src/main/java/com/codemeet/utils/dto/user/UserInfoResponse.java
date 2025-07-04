package com.codemeet.utils.dto.user;

import com.codemeet.entity.Gender;
import com.codemeet.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserInfoResponse(
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
    @Pattern(
        regexp = "",
        flags = {}
    )
    String username,
    
    String bio,
    
    @NotNull
    @NotBlank
    @Length(max = 100) // Minimum length is determined by the pattern
    @Pattern(
        regexp = "",
        flags = {}
    )
    String email,
    
    @NotNull
    @NotBlank
    @Length(max = 25)
    @Pattern(
        regexp = "",
        flags = {}
    )
    String phoneNumber,
    
    @NotNull
    Gender gender,
    
    String profilePictureUrl
) {

    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getUsername(),
            user.getBio(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getGender(),
            user.getProfilePictureUrl()
        );
    }
}
