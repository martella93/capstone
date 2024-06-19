package it.epicode.capstone.service;

import it.epicode.capstone.dto.RecensioneDto;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Prenotazione;
import it.epicode.capstone.entity.Recensione;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.RecensioneNotFoundException;
import it.epicode.capstone.repository.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private EsperienzaService esperienzaService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private UserService userService;

    public String createRecensione(int esperienzaId, RecensioneDto recensioneDto) {
        // Ottenere l'utente corrente autenticato
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Verificare se l'utente ha una prenotazione per l'esperienza specificata
        if (!prenotazioneService.hasPrenotazioneForUserAndEsperienza(user.getId(), esperienzaId)) {
            return "Impossibile creare una recensione. Devi prima prenotare questa esperienza.";
        }

        // Ottenere l'esperienza associata all'ID specificato
        Optional<Esperienza> esperienzaOptional = esperienzaService.getEsperienzaById(esperienzaId);
        if (!esperienzaOptional.isPresent()) {
            return "Impossibile trovare l'esperienza associata alla recensione.";
        }

        // Creare e salvare la nuova recensione
        Recensione recensione = new Recensione();
        recensione.setValutazione(recensioneDto.getValutazione());
        recensione.setCommento(recensioneDto.getCommento());
        recensione.setEsperienza(esperienzaOptional.get());
        recensione.setUser(user);

        recensioneRepository.save(recensione);

        return "Recensione creata con successo";
    }


    public Recensione updateRecensione(int id, RecensioneDto recensioneDto){
        Optional<Recensione> recensioneOptional = recensioneRepository.findById(id);

        if (recensioneOptional.isPresent()) {
            Recensione recensione = new Recensione();
            recensione.setValutazione(recensioneDto.getValutazione());
            recensione.setCommento(recensioneDto.getCommento());
            return recensioneRepository.save(recensione);
        }
        else {
            throw new RecensioneNotFoundException("Recensione con id " + id + " not found");
        }
    }

    public String deleteRecensione(int id){
        Optional<Recensione> recensioneOptional = recensioneRepository.findById(id);

        if (recensioneOptional.isPresent()){
            recensioneRepository.deleteById(id);
            return "Recensione with id= " + id + " correctly deleted";
        }
        else {
            throw new RecensioneNotFoundException("Recensione con id " + id + " not found");
        }
    }
}
