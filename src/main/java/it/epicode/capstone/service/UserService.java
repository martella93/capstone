package it.epicode.capstone.service;

import it.epicode.capstone.dto.SignupDto;
import it.epicode.capstone.dto.UserDto;
import it.epicode.capstone.entity.Role;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.BadRequestException;
import it.epicode.capstone.exception.UserNotFoundException;
import it.epicode.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;

    public SignupDto saveUser(UserDto userDto) {
        if (getUserByUsername(userDto.getUsername()).isPresent()) {
            throw new BadRequestException("Lo username è già stato preso.");
        }

        if (getUserByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException("L'email è già in uso.");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);
        userRepository.save(user);
        sendMail(user.getEmail());

        SignupDto signupDto = new SignupDto();
        signupDto.setUser(user);

        return signupDto;
    }


    public Page<User> getUser(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public User updateUser(int id, UserDto userDto){
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()){
            User user = userOptional.get();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole(Role.USER);
            user.setEmail(userDto.getEmail());
            return userRepository.save(user);
        }
        else {
            throw  new UserNotFoundException("User with id= " + id + " not found");
        }
    }

    public String deleteUser(int id){
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()){
            userRepository.deleteById(id);
            return "User with id= " + id + " correctly deleted";
        }
        else {
            throw  new UserNotFoundException("User with id= " + id + " not found");
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void sendMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Signup");
        message.setText("Registrazione effettuata!");

        javaMailSender.send(message);
    }
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }
}
