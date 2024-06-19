package it.epicode.capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Guida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String cognome;
    private String descrizione;
    private int anniEsperienza;
    private String lingue;
    private List<String> video = new ArrayList<>();
    private List<String> foto = new ArrayList<>();

    @OneToOne(mappedBy = "guida")
    private Esperienza esperienza;


}
