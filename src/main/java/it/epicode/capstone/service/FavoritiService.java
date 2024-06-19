package it.epicode.capstone.service;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Favoriti;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
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

        Esperienza esperienza = esperienzaRepository.findById(esperienzaId).orElseThrow(() -> new EsperienzaNotFoundException("Esperienza con id " + esperienzaId + " not found"));
        Favoriti favorito = new Favoriti();
        favorito.setUser(user);
        favorito.setEsperienza(esperienza);
        favoritiRepository.save(favorito);
        return "Esperienza con id "+esperienzaId+" aggiunta ai favoriti";
    }

    public String removeFavorito(int esperienzaId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Esperienza esperienza = esperienzaRepository.findById(esperienzaId).orElseThrow(() -> new EsperienzaNotFoundException("Esperienza con id " + esperienzaId + " not found"));
        Favoriti favorito = favoritiRepository.findByUserAndEsperienza(user, esperienza).orElseThrow(() -> new EsperienzaNotFoundException("Esperienza con id " + esperienzaId + " not found"));
        favoritiRepository.delete(favorito);
        return "Favorito rimosso";
    }

    public List<Esperienza> getFavorites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Favoriti> favorites = favoritiRepository.findByUser(user);
        return favorites.stream().map(Favoriti:: getEsperienza).collect(Collectors.toList());
    }
}
