package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Esperienza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EsperienzaRepository extends JpaRepository<Esperienza, Integer> {

    List<Esperienza> findByLuogoContainingIgnoreCase(String luogo);

}
