package it.epicode.capstone.controller;

import it.epicode.capstone.dto.UserDto;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.BadRequestException;
import it.epicode.capstone.exception.UserNotFoundException;
import it.epicode.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public Page<User> getUsers(@RequestParam(defaultValue = "0")  int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy){
        return userService.getUser(page,size,sortBy);
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable int id){
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            throw new UserNotFoundException("User con id " + id + " not found");
        }
    }

    @PutMapping("/api/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().
                    stream().
                    map(ObjectError::getDefaultMessage).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/api/user/{id}")
    public String deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }

}
