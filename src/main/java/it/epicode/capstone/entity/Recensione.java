package it.epicode.capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int valutazione;
    private String commento;

//    @ManyToOne
//    @JoinColumn(name = "prenotazione_id")
//    private Prenotazione prenotazione;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "esperienza_id")
    private Esperienza esperienza;
}
