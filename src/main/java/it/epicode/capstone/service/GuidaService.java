package it.epicode.capstone.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.capstone.dto.GuidaDto;
import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Guida;
import it.epicode.capstone.exception.EsperienzaNotFoundException;
import it.epicode.capstone.exception.GuidaNotFoundException;
import it.epicode.capstone.repository.GuidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GuidaService {

    @Autowired
    private GuidaRepository guidaRepository;

    @Autowired
    private Cloudinary cloudinary;

    public String createGuida(GuidaDto guidaDto,  Esperienza esperienza) {
        Guida guida = new Guida();

        guida.setNome(guidaDto.getNome());
        guida.setCognome(guidaDto.getCognome());
        guida.setDescrizione(guidaDto.getDescrizione());
        guida.setAnniEsperienza(guidaDto.getAnniEsperienza());
        guida.setLingue(guidaDto.getLingue());

        esperienza.setGuida(guida);

        guidaRepository.save(guida);
        return "Guida con id " + guida.getId() + " salvata correttamente";
    }

    public List<Guida> getAllGuida() {
        return guidaRepository.findAll();
    }

    public Optional<Guida> getGuidaById(int id) {
        return guidaRepository.findById(id);
    }

    public Guida updateGuida(int id, GuidaDto guidaDto) {
        Optional<Guida> guidaOptional = guidaRepository.findById(id);

        if (guidaOptional.isPresent()) {
            Guida guida = new Guida();

            guida.setNome(guidaDto.getNome());
            guida.setCognome(guidaDto.getCognome());
            guida.setDescrizione(guidaDto.getDescrizione());
            guida.setAnniEsperienza(guidaDto.getAnniEsperienza());
            guida.setLingue(guidaDto.getLingue());

            return guidaRepository.save(guida);
        }
        else {
            throw new GuidaNotFoundException("Guida con id " + id + " not found");
        }
    }

    public String deleteGuida(int id) {
        guidaRepository.deleteById(id);
        return "Guida con id " + id + " eliminata correttamente";
    }

    public String uploadImmagineGuida(int id, List<MultipartFile> foto) throws IOException {
        Optional<Guida> guidaOptional = getGuidaById(id);

        if (guidaOptional.isPresent()) {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : foto) {
                if (!file.isEmpty()) {
                    String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
                    urls.add(url);
                }
            }
            Guida guida = guidaOptional.get();
            guida.setFoto(List.of(urls.toArray(new String[0])));

            guidaRepository.save(guida);
            return "Immagini caricate con successo dalla guida"+ guida.getNome() + " " + guida.getCognome() + " con id=" + id;
        } else {
            throw new EsperienzaNotFoundException("Esperienza con id " + id + " non trovata");
        }
    }

    public String uploadVideoGuida(int id, List<MultipartFile> video) throws IOException {
        // Verifica se video è nullo
        if (video == null) {
            throw new IllegalArgumentException("La lista di video non può essere nulla");
        }

        // Recupera la guida dal repository
        Optional<Guida> guidaOptional = guidaRepository.findById(id);

        if (guidaOptional.isPresent()) {
            Guida guida = guidaOptional.get();
            List<String> urls = new ArrayList<>();

            for (MultipartFile file : video) {
                if (!file.isEmpty()) {
                    try {
                        // Carica il video su Cloudinary
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
                        String url = (String) uploadResult.get("url");
                        urls.add(url);
                    } catch (IOException e) {
                        throw new IOException("Errore durante il caricamento del video: " + file.getOriginalFilename(), e);
                    }
                }
            }

            // Aggiorna la lista di URL dei video nella guida
            guida.setVideo(List.of(urls.toArray(new String[0])));

            // Salva la guida aggiornata nel repository
            guidaRepository.save(guida);

            return "Video con URL=" + urls + " salvato correttamente per la guida con ID=" + id;
        } else {
            throw new GuidaNotFoundException("Guida con ID " + id + " non trovata");
        }
    }

    public Guida getGuidaByEsperienzaId(int esperienzaId) {
        Optional<Guida> guidaOptional = guidaRepository.findByEsperienzaId(esperienzaId);
        return guidaOptional.orElseThrow(() -> new GuidaNotFoundException("Guida for Esperienza with id " + esperienzaId + " not found"));
    }
}
