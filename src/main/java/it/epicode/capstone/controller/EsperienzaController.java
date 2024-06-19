package it.epicode.capstone.controller;

import it.epicode.capstone.dto.EsperienzaDto;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Prenotazione;
import it.epicode.capstone.exception.BadRequestException;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
import it.epicode.capstone.service.EsperienzaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/esperienze")
public class EsperienzaController {

    @Autowired
    private EsperienzaService esperienzaService;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String saveEsperienza(@Valid @RequestBody @Validated EsperienzaDto esperienzaDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().
                    stream().
                    map(error -> error.getDefaultMessage()).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return esperienzaService.saveEsperienza(esperienzaDto);
    }

    @GetMapping
    public List<Esperienza> getAllEsperienze() {
       return esperienzaService.getAllEsperienze();
    }

    @GetMapping("/{id}")
    public Esperienza getEsperienzaById(@PathVariable int id) {
        Optional<Esperienza> esperienzaOptional = esperienzaService.getEsperienzaById(id);

        if(esperienzaOptional.isPresent()){
            return esperienzaOptional.get();
        }
        else {
            throw new EsperienzaNotFoundException("Esperienza with id " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Esperienza updateEsperienza(@RequestBody @Validated EsperienzaDto esperienzaDto, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().
                    stream().
                    map(error -> error.getDefaultMessage()).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return esperienzaService.updateEsperienza(esperienzaDto, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteEsperienza(@PathVariable int id) {
        return esperienzaService.deleteEsperienza(id);
    }

    @PatchMapping("/{id}/upload-foto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadFotoEsperienza(@PathVariable int id, @RequestBody List<MultipartFile> foto) throws IOException {
        return esperienzaService.uploadImmagineEsperienza(id, foto);
    }

    @PatchMapping("/{id}/upload-video")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadVideoEsperienza(@PathVariable int id, @RequestBody List<MultipartFile> video) throws IOException {
        return esperienzaService.uploadVideoEsperienza(id,video);
    }

    @PostMapping("/{esperienzaId}/prenotazioni")
    @PreAuthorize("hasAnyAuthority('USER')")
    public Prenotazione prenotaEsperienza(@PathVariable int esperienzaId, @RequestBody Prenotazione prenotazione) {
        return esperienzaService.prenotaEsperienza(esperienzaId, prenotazione);
    }

    @GetMapping("/cercaPerLuogo")
    public List<Esperienza> cercaPerLuogo(@RequestParam String luogo) {
        return esperienzaService.cercaPerLuogo(luogo);
    }









}
