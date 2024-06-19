package it.epicode.capstone.controller;

import it.epicode.capstone.dto.AuthDataDto;
import it.epicode.capstone.dto.SignupDto;
import it.epicode.capstone.dto.UserDto;
import it.epicode.capstone.dto.UserLoginDto;
import it.epicode.capstone.service.AuthService;
import it.epicode.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/signup")
    public SignupDto register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().
                    stream().
                    map(ObjectError::getDefaultMessage).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return userService.saveUser(userDto);
    }

    @PostMapping("/auth/login")
    public AuthDataDto login(@RequestBody @Validated UserLoginDto userLoginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().
                    stream().
                    map(ObjectError::getDefaultMessage).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return authService.authenticateUserAndGenerateToken(userLoginDto);
    }
}
