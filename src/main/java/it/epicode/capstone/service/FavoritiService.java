package it.epicode.capstone.service;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Favoriti;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
import it.epicode.capstone.exception.NotFoundException;
import it.epicode.capstone.repository.EsperienzaRepository;
import it.epicode.capstone.repository.FavoritiRepository;
import it.epicode.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritiService {

    @Autowired
    private FavoritiRepository favoritiRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EsperienzaRepository esperienzaRepository;

    public String addFavorito(int esperienzaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Favoriti existingFavorito = favoritiRepository.findByUserAndEsperienza_Id(user, esperienzaId);
        if (existingFavorito != null) {
            throw new IllegalStateException("L'esperienza con id " + esperienzaId + " è già nei preferiti dell'utente");
        }

        Esperienza esperienza = esperienzaRepository.findById(esperienzaId).orElseThrow(() -> new EsperienzaNotFoundException("Esperienza con id " + esperienzaId + " not found"));
        Favoriti favorito = new Favoriti();
        favorito.setUser(user);
        favorito.setEsperienza(esperienza);
        favoritiRepository.save(favorito);
        return "Esperienza con id "+esperienzaId+" aggiunta ai favoriti";
    }

    public String removeFavorito(int esperienzaId) {
        // Ottieni l'utente autenticato dal contesto di sicurezza
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("Utente non autenticato o tipo di utente non valido");
        }

        User user = (User) authentication.getPrincipal();

        // Trova l'esperienza dal repository
        Esperienza esperienza = esperienzaRepository.findById(esperienzaId)
                .orElseThrow(() -> new EsperienzaNotFoundException("Esperienza con id " + esperienzaId + " non trovata"));

        // Trova il favorito corrispondente dal repository
        List<Favoriti> favoritiList = favoritiRepository.findByUserAndEsperienza(user, esperienza);
        if (favoritiList.isEmpty()) {
            throw new NotFoundException("Favorito con esperienza id " + esperienzaId + " non trovato");
        }

        // Rimuovi il favorito dal repository (considerando che potrebbero essercene più di uno)
        for (Favoriti favorito : favoritiList) {
            favoritiRepository.delete(favorito);
        }

        return "Favorito rimosso";
    }


    public List<Favoriti> getFavoritiByUser(User user) {
        return favoritiRepository.findByUser(user);
    }
}
