package it.epicode.capstone.service;

import it.epicode.capstone.dto.AuthDataDto;
import it.epicode.capstone.dto.UserLoginDto;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.NotFoundException;
import it.epicode.capstone.exception.UnauthorizedException;
import it.epicode.capstone.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthDataDto authenticateUserAndGenerateToken(UserLoginDto userLoginDto) {
        Optional<User> userOptional = userService.getUserByUsername(userLoginDto.getUsername());

        if (userOptional.isPresent()) {
           User user = userOptional.get();
            if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                AuthDataDto authDataDto = new AuthDataDto();
                authDataDto.setAccessToken(jwtTool.createToken(user));
                authDataDto.setUser(user);
                return authDataDto;
            } else {
                throw new UnauthorizedException("Errore in fase di login, ritentare.");
            }

        } else {
            throw new NotFoundException("Utente con username: " + userLoginDto.getUsername() + "non trovato.");
        }
    }


}
