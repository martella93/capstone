package it.epicode.capstone.controller;

import it.epicode.capstone.entity.Prenotazione;
import it.epicode.capstone.exception.PrenotazioneNotFoundException;
import it.epicode.capstone.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioneService.getAllPrenotazioni();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Prenotazione getPrenotazioneById(@PathVariable int id) {
        Optional<Prenotazione> prenotazioneOptional = prenotazioneService.getPrenotazioneById(id);

        if (prenotazioneOptional.isPresent()) {
            return prenotazioneOptional.get();
        }
        else {
            throw new PrenotazioneNotFoundException("Prenotazione with id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String cancellaPrenotazione(@PathVariable int id) {
         return prenotazioneService.deletePrenotazione(id);
    }
}
