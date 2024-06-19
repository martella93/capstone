package it.epicode.capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.capstone.enums.Categoria;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Esperienza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titolo;
    private String descrizione;
    private String programma;
    private String luogo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String ora;
    private String durata;
    private List<String> video = new ArrayList<>();
    private List<String> foto = new ArrayList<>();
    private int postiEsperienza;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private double prezzo;
    private int puntiEsperienza;

    @OneToOne
    @JoinColumn(name = "guida_id")
    @JsonIgnore
    private Guida guida;

    @OneToMany(mappedBy = "esperienza")
    @JsonIgnore
    private List<Recensione> recensione;

    @OneToMany(mappedBy = "esperienza")
    @JsonIgnore
    private List<Prenotazione> prenotazione;

    @OneToMany(mappedBy = "esperienza")
    @JsonIgnore
    private List<DatePrenotate> datePrenotate = new ArrayList<>();



}
