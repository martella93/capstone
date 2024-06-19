package it.epicode.capstone.repository;

import it.epicode.capstone.entity.DatePrenotate;
import it.epicode.capstone.entity.Esperienza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DatePrenotateRepository extends JpaRepository<DatePrenotate, Integer> {

    Optional<DatePrenotate> findByEsperienzaAndDataPrenotata(Esperienza esperienza, LocalDate dataPrenotata);


}
