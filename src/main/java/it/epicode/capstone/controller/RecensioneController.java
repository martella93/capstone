package it.epicode.capstone.controller;

import it.epicode.capstone.dto.RecensioneDto;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Recensione;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.service.EsperienzaService;
import it.epicode.capstone.service.RecensioneService;
import it.epicode.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/recensione")
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private EsperienzaService esperienzaService;

    @Autowired
    private UserService userService;


    @PostMapping("/{esperienzaId}")
    @PreAuthorize("hasAuthority('USER')")
    public String createRecensione(@PathVariable int esperienzaId, @RequestBody @Validated RecensioneDto recensioneDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", ((s, s2) -> s+s2)));
        }
        return recensioneService.createRecensione(esperienzaId, recensioneDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Recensione updateRecensione(@PathVariable int id, @RequestBody @Validated RecensioneDto recensioneDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RuntimeException(bindingResult.getAllErrors().
                    stream().
                    map(error -> error.getDefaultMessage()).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return recensioneService.updateRecensione(id, recensioneDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteRecensione(@PathVariable int id){
        return recensioneService.deleteRecensione(id);
    }

}
