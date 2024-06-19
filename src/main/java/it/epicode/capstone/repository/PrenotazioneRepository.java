package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

        boolean existsByUserIdAndEsperienzaId(int userId, int esperienzaId);

}
