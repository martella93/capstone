package it.epicode.capstone.dto;

import it.epicode.capstone.enums.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class EsperienzaDto {

    private int id;

    @NotBlank(message = "Titolo obbligatorio")
    private String titolo;

    @NotNull
    private String descrizione;

    @NotBlank(message = "Luogo obbligatorio")
    private String luogo;

    @NotNull(message = "Data inizio obbligatoria")
    private LocalDate dataInizio;
    @NotNull(message = "Data fine obbligatoria")
    private LocalDate dataFine;

    @NotNull(message = "Fasce oraria obbligatoria")
    private String ora;

    @NotNull(message = "Durata obbligatoria")
    private String durata;

    @NotBlank(message = "programma obbligatorio")
    private String programma;

    private int guidaId;


    @NotNull(message = "Prezzo obbligatorio")
    private double prezzo;

    private List<String> video = new ArrayList<>();

    private List<String> foto = new ArrayList<>();

    @NotNull(message = "Punti obbligatori")
    private int puntiEsperienza;

    @NotNull(message = "Posti obbligatori")
    private int postiEsperienza;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Categoria obbligatoria")
    private Categoria categoria;


}
