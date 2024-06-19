package it.epicode.capstone.service;

import it.epicode.capstone.entity.DatePrenotate;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Prenotazione;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.PrenotazioneNotFoundException;
import it.epicode.capstone.repository.DatePrenotateRepository;
import it.epicode.capstone.repository.EsperienzaRepository;
import it.epicode.capstone.repository.PrenotazioneRepository;
import it.epicode.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private EsperienzaRepository esperienzaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatePrenotateRepository datePrenotateRepository;

    public List<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public Optional<Prenotazione> getPrenotazioneById(int id) {
        return prenotazioneRepository.findById(id);
    }


    public String deletePrenotazione(int id) {
        Optional<Prenotazione> prenotazioneOptional = prenotazioneRepository.findById(id);
        if (prenotazioneOptional.isPresent()) {
            Prenotazione prenotazione = prenotazioneOptional.get();

            Esperienza esperienza = prenotazione.getEsperienza();
            User user = prenotazione.getUser();

            esperienza.setPostiEsperienza(esperienza.getPostiEsperienza() + prenotazione.getPostiPrenotati());
            esperienzaRepository.save(esperienza);

            user.setPuntiGuadagnati(user.getPuntiGuadagnati()- esperienza.getPuntiEsperienza());
            userRepository.save(user);

            prenotazioneRepository.deleteById(id);

            Optional<DatePrenotate> datePrenotateOptional = datePrenotateRepository.findByEsperienzaAndDataPrenotata(esperienza, prenotazione.getDataPrenotazione());
            if (datePrenotateOptional.isPresent()) {
                DatePrenotate datePrenotate = datePrenotateOptional.get();
                datePrenotateRepository.delete(datePrenotate);
            }

            return "Prenotazione with id= " + id + " correctly deleted";
        }
        else {
            throw  new PrenotazioneNotFoundException("Prenotazione with id= " + id + " not found");
        }
    }


    public boolean hasPrenotazioneForUserAndEsperienza(int id, int esperienzaId) {
        return prenotazioneRepository.existsByUserIdAndEsperienzaId(id , esperienzaId);
    }
}
