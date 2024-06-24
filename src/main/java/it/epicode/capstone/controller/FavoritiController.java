package it.epicode.capstone.controller;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Favoriti;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.service.FavoritiService;
import it.epicode.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequestMapping("/api/favoriti")
public class FavoritiController {

    @Autowired
    private FavoritiService favoritiService;

    @Autowired
    private UserService userService;



    @PostMapping("/add/{esperienzaId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String addFavorite(@PathVariable int esperienzaId) {
        return favoritiService.addFavorito(esperienzaId);
    }

    @DeleteMapping("/remove/{esperienzaId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String removeFavorite(@PathVariable int esperienzaId) {
       return favoritiService.removeFavorito(esperienzaId);
    }


    @GetMapping("/loggedUser")
    public List<Favoriti> getFavoritiByLoggedUser() {
        User loggedUser = getLoggedUser();
        if (loggedUser != null) {
            return favoritiService.getFavoritiByUser(loggedUser);
        } else {
            throw new RuntimeException("User not logged in");
        }
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

}
