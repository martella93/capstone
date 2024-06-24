package it.epicode.capstone.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.capstone.dto.EsperienzaDto;
import it.epicode.capstone.entity.DatePrenotate;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Prenotazione;
import it.epicode.capstone.entity.User;
import it.epicode.capstone.exception.DataNonDisponibileException;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
import it.epicode.capstone.exception.NotFoundException;
import it.epicode.capstone.exception.PostiEsauritiException;
import it.epicode.capstone.repository.DatePrenotateRepository;
import it.epicode.capstone.repository.EsperienzaRepository;
import it.epicode.capstone.repository.PrenotazioneRepository;
import it.epicode.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class EsperienzaService {

    @Autowired
    private EsperienzaRepository esperienzaRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private DatePrenotateRepository datePrenotateRepository;

    public String saveEsperienza(EsperienzaDto esperienzaDto) {
        Esperienza esperienza = new Esperienza();
        esperienza.setTitolo(esperienzaDto.getTitolo());
        esperienza.setDescrizione(esperienzaDto.getDescrizione());
        esperienza.setProgramma(esperienzaDto.getProgramma());
        esperienza.setPostiEsperienza(esperienzaDto.getPostiEsperienza());
        esperienza.setLuogo(esperienzaDto.getLuogo());
        esperienza.setCategoria(esperienzaDto.getCategoria());
        esperienza.setDataInizio(esperienzaDto.getDataInizio());
        esperienza.setDataFine(esperienzaDto.getDataFine());
        esperienza.setOra(esperienzaDto.getOra());
        esperienza.setDurata(esperienzaDto.getDurata());
        esperienza.setPrezzo(esperienzaDto.getPrezzo());
        esperienza.setPuntiEsperienza(esperienzaDto.getPuntiEsperienza());

        esperienzaRepository.save(esperienza);
       return "Esperienza con ID " + esperienza.getId() + " creata";

    }

    public List<Esperienza> getAllEsperienze() {
        return esperienzaRepository.findAll();
    }

    public Optional<Esperienza> getEsperienzaById(int id) {
        return esperienzaRepository.findById(id);
    }

    public Esperienza updateEsperienza(EsperienzaDto esperienzaDto, int id) {
        Optional<Esperienza> esperienzaOptional = getEsperienzaById(id);

        if (esperienzaOptional.isPresent()) {
            Esperienza esperienza = esperienzaOptional.get();
            esperienza.setTitolo(esperienzaDto.getTitolo());
            esperienza.setDescrizione(esperienzaDto.getDescrizione());
            esperienza.setProgramma(esperienzaDto.getProgramma());
            esperienza.setPostiEsperienza(esperienzaDto.getPostiEsperienza());
            esperienza.setLuogo(esperienzaDto.getLuogo());
            esperienza.setCategoria(esperienzaDto.getCategoria());
            esperienza.setDataInizio(esperienzaDto.getDataInizio());
            esperienza.setDataFine(esperienzaDto.getDataFine());
            esperienza.setOra(esperienzaDto.getOra());
            esperienza.setDurata(esperienzaDto.getDurata());
            esperienza.setPrezzo(esperienzaDto.getPrezzo());
            return esperienzaRepository.save(esperienza);
        }

        else {
            throw new NotFoundException("Esperienza with id= " + id + " not found");
        }
    }

    public String deleteEsperienza(int id){
        Optional<Esperienza> esperienzaOptional = getEsperienzaById(id);

        if (esperienzaOptional.isPresent()){
            esperienzaRepository.deleteById(id);
            return "Esperienza con id= " + id + " correctly deleted";
        }
        else {
            throw  new NotFoundException("Esperienza with id= " + id + " not found");
        }
    }

public String uploadImmagineEsperienza(int id, List<MultipartFile> foto) throws IOException {
    Optional<Esperienza> esperienzaOptional = getEsperienzaById(id);

    if (esperienzaOptional.isPresent()) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : foto) {
            if (!file.isEmpty()) {
                String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
                urls.add(url);
            }
        }
        Esperienza esperienza = esperienzaOptional.get();
        esperienza.setFoto(List.of(urls.toArray(new String[0])));

        esperienzaRepository.save(esperienza);
        return "Immagini caricate con successo su esperienza con id=" + id;
    } else {
        throw new EsperienzaNotFoundException("Esperienza con id " + id + " non trovata");
    }
}


    public String uploadVideoEsperienza(int id, List<MultipartFile> video) throws IOException {

        if (video == null) {
            throw new IllegalArgumentException("La lista di video non può essere nulla");
        }
        Optional<Esperienza> esperienzaOptional = getEsperienzaById(id);


        if (esperienzaOptional.isPresent()) {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : video) {
                if (!file.isEmpty()) {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
                        String url = (String) uploadResult.get("url");
                        urls.add(url);
                    } catch (IOException e) {
                        throw new IOException("Errore durante il caricamento del video: " + file.getOriginalFilename(), e);
                    }
                }
            }

            Esperienza esperienza = esperienzaOptional.get();
            esperienza.setVideo(List.of(urls.toArray(new String[0])));

            esperienzaRepository.save(esperienza);
            return "Video con url=" + urls + " salvato correttamente su esperienza con id=" + id;
        } else {
            throw new EsperienzaNotFoundException("Esperienza con id " + id + " non trovata");
        }
    }

    public Prenotazione prenotaEsperienza(int esperienzaId, Prenotazione prenotazione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Esperienza esperienza = esperienzaRepository.findById(esperienzaId)
                .orElseThrow(() -> new EsperienzaNotFoundException("Esperienza with id " + esperienzaId + " not found"));

        LocalDate dataPrenotazione = prenotazione.getDataPrenotazione();

        // Verifica che la data di prenotazione non sia null
        if (dataPrenotazione == null) {
            throw new IllegalArgumentException("La data di prenotazione non può essere null");
        }

        // Verifica se la data di prenotazione rientra nell'intervallo di date dell'esperienza
        if (dataPrenotazione.isBefore(esperienza.getDataInizio()) || dataPrenotazione.isAfter(esperienza.getDataFine())) {
            throw new DataNonDisponibileException("La data selezionata non rientra nell'intervallo disponibile per questa esperienza");
        }

        // Verifica se ci sono abbastanza posti disponibili per la data specifica della prenotazione
        Optional<DatePrenotate> datePrenotateOpt = datePrenotateRepository.findByEsperienzaAndDataPrenotata(esperienza, dataPrenotazione);

        int postiDisponibili = esperienza.getPostiEsperienza();
        if (datePrenotateOpt.isPresent()) {
            postiDisponibili -= datePrenotateOpt.get().getPostiPrenotati();
        }

        if (prenotazione.getPostiPrenotati() > postiDisponibili) {
            throw new PostiEsauritiException("No more available seats for this date in the experience");
        }

        // Imposta l'utente e l'esperienza nella prenotazione
        prenotazione.setEsperienza(esperienza);
        prenotazione.setUser(user);

        // Aggiorna o crea un nuovo record per le date prenotate
        DatePrenotate datePrenotate;
        if (datePrenotateOpt.isPresent()) {
            datePrenotate = datePrenotateOpt.get();
            datePrenotate.setPostiPrenotati(datePrenotate.getPostiPrenotati() + prenotazione.getPostiPrenotati());
        } else {
            datePrenotate = new DatePrenotate();
            datePrenotate.setEsperienza(esperienza);
            datePrenotate.setPostiPrenotati(prenotazione.getPostiPrenotati());
            datePrenotate.setDataPrenotata(dataPrenotazione);
        }
        datePrenotateRepository.save(datePrenotate);

        user.setPuntiGuadagnati(user.getPuntiGuadagnati() + esperienza.getPuntiEsperienza());
        userRepository.save(user);

        sendMail(user.getEmail());

        return prenotazioneRepository.save(prenotazione);
    }

//    public Prenotazione prenotaEsperienza(int esperienzaId, Prenotazione prenotazione) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        Esperienza esperienza = esperienzaRepository.findById(esperienzaId)
//                .orElseThrow(() -> new EsperienzaNotFoundException("Esperienza with id " + esperienzaId + " not found"));
//
//         // Verifica se la data di prenotazione è disponibile per questa esperienza
////    if (!isDataDisponibile(esperienza, prenotazione.getDataPrenotazione())) {
////        throw new DataNonDisponibileException("La data selezionata non è disponibile per questa esperienza");
////    }
//
//        // Verifica se ci sono abbastanza posti disponibili sull'esperienza
//        if (prenotazione.getPostiPrenotati() > esperienza.getPostiEsperienza()) {
//            throw new PostiEsauritiException("No more available seats for this experience");
//        }
//
//        // Imposta l'utente e l'esperienza nella prenotazione
//        prenotazione.setEsperienza(esperienza);
//        prenotazione.setUser(user);
//
//        prenotazione.setDataPrenotazione(LocalDate.now());
//
//        // Aggiorna il numero di posti prenotati sull'esperienza
//        esperienza.setPostiEsperienza(esperienza.getPostiEsperienza() - prenotazione.getPostiPrenotati());
//        user.setPuntiGuadagnati(esperienza.getPuntiEsperienza());
//        esperienzaRepository.save(esperienza);
//
//        DatePrenotate datePrenotate = new DatePrenotate();
//        datePrenotate.setEsperienza(esperienza);
//        datePrenotate.setPostiPrenotati(prenotazione.getPostiPrenotati());
//        datePrenotate.setDataPrenotata(prenotazione.getData());
//        datePrenotateRepository.save(datePrenotate);
//
//
//
//        esperienzaRepository.save(esperienza);
//
//        sendMail(user.getEmail());
//
//        return prenotazioneRepository.save(prenotazione);
//
//    }

//    private boolean isDataDisponibile(Esperienza esperienza, LocalDate dataPrenotazione) {
//        // Verifica se esperienza o dataPrenotazione sono null
//        if (esperienza == null || dataPrenotazione == null) {
//            return false; // o solleva un'eccezione, a seconda di come vuoi gestire il caso null
//        }
//
//        // Verifica se la data è all'interno dell'intervallo di date disponibili
////        if (dataPrenotazione.isBefore(esperienza.getDataInizio()) || dataPrenotazione.isAfter(esperienza.getDataFine())) {
////            return false;
////        }
//
//
//        // Verifica se la data è già prenotata
//        List<DatePrenotate> datePrenotate = datePrenotateRepository.findByEsperienzaAndDataPrenotata(esperienza, dataPrenotazione);
//        return datePrenotate.isEmpty();
//    }


    private void sendMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("PRENOTAZIONE");
        message.setText("Prenotazione effettuata!");

        javaMailSender.send(message);
    }

    public List<Esperienza> cercaPerLuogo(String luogo) {
        return esperienzaRepository.findByLuogoContainingIgnoreCase(luogo);
    }


}
