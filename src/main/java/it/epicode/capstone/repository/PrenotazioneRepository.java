package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

        boolean existsByUserIdAndEsperienzaId(int userId, int esperienzaId);

        @Query("SELECT p FROM Prenotazione p WHERE p.user.id = :userId")
        List<Prenotazione> findPrenotazioniByUserId(@Param("userId") int userId);

        int countByEsperienzaIdAndDataPrenotazione(int esperienzaId, LocalDate dataPrenotazione);

}
