package it.epicode.capstone.repository;

import it.epicode.capstone.entity.Esperienza;
import it.epicode.capstone.entity.Favoriti;
import it.epicode.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface FavoritiRepository extends JpaRepository<Favoriti, Integer> {

    @Query("SELECT f FROM Favoriti f JOIN FETCH f.esperienza e WHERE f.user = :user AND e = :esperienza")
    List<Favoriti> findByUserAndEsperienza(@Param("user") User user, @Param("esperienza") Esperienza esperienza);

    List<Favoriti> findByUser(User user);

    Favoriti findByUserAndEsperienza_Id(User user, int esperienzaId);
}
