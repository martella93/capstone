package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Guida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuidaRepository extends JpaRepository<Guida, Integer> {

    Optional<Guida> findByEsperienzaId(Integer esperienzaId);

}
