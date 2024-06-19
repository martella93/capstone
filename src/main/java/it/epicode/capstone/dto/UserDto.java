package it.epicode.capstone.dto;

import it.epicode.capstone.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    private int id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @Email(message = "Email is not valid")
    private String email;

    private int puntiGuadagnati;

    private Role role;
}
