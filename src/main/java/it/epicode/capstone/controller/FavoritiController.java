package it.epicode.capstone.controller;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.service.FavoritiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoriti")
public class FavoritiController {

    @Autowired
    private FavoritiService favoritiService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<Esperienza> getFavorites() {
        return favoritiService.getFavorites();
    }

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


}
