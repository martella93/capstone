package it.epicode.capstone.controller;

import it.epicode.capstone.dto.GuidaDto;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Guida;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
import it.epicode.capstone.exception.GuidaNotFoundException;
import it.epicode.capstone.repository.EsperienzaRepository;
import it.epicode.capstone.service.GuidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guida")
public class GuidaController {

    @Autowired
    private GuidaService guidaService;

    @Autowired
    private EsperienzaRepository esperienzaRepository;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Guida> getAllGuida() {
        return guidaService.getAllGuida();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Guida getGuidaById(@PathVariable int id) {
        Optional<Guida> guidaOptional = guidaService.getGuidaById(id);

        if (guidaOptional.isPresent()) {
            return guidaOptional.get();
        }
        else {
            throw new GuidaNotFoundException("Guida with id " + id + " not found");
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createGuida(@RequestBody @Validated GuidaDto guidaDto,  @RequestParam int esperienzaId , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().
                    stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", ((s, s2) -> s+s2)));
        }
        Esperienza esperienza = esperienzaRepository.findById(esperienzaId).orElseThrow(() -> new EsperienzaNotFoundException("Esperienza with id=" + esperienzaId + " not found"));
        return guidaService.createGuida(guidaDto,esperienza);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Guida updateGuida(@RequestBody @Validated GuidaDto guidaDto, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().
                    stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", ((s, s2) -> s+s2)));
        }
        return guidaService.updateGuida(id, guidaDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteGuida(@PathVariable int id) {
        return guidaService.deleteGuida(id);
    }

    @PatchMapping("/{id}/upload-foto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadFotoGuida(@PathVariable int id, @RequestBody List<MultipartFile> foto) throws IOException {
        return guidaService.uploadImmagineGuida(id, foto);
    }

    @PatchMapping("/{id}/upload-video")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadVideoGuida(@PathVariable int id, @RequestBody List<MultipartFile> video) throws IOException {
        return guidaService.uploadVideoGuida(id, video);
    }



}
