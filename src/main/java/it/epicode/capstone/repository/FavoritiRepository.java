package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Favoriti;
import it.epicode.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritiRepository extends JpaRepository<Favoriti, Integer> {

    Optional<Favoriti> findByUserAndEsperienza(User user, Esperienza esperienza);

    List<Favoriti> findByUser(User user);
}
