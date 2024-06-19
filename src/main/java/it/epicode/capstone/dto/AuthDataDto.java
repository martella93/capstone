package it.epicode.capstone.dto;


import it.epicode.capstone.entity.User;
import lombok.Data;

@Data
public class AuthDataDto {

    private String accessToken;
    private User user;
}
