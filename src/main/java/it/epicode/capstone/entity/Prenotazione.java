package it.epicode.capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate data;
    private LocalTime ora;
    private int postiPrenotati;

    private LocalDate dataPrenotazione;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany(mappedBy = "prenotazione")
//    private List<Recensione> recensioni;

    @ManyToOne
    @JoinColumn(name = "esperienza_id")
    private Esperienza esperienza;

}
