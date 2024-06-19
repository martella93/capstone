package it.epicode.capstone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PrenotazioneDto {

    private int id;
    @NotNull(message = "Data prenotazione obbligatoria")
    private LocalDate data;
    @NotNull(message = "Ora prenotazione obbligatoria")
    private LocalTime ora;
    @NotNull(message = "Posti prenotati obbligatori")
    @Min(value = 1, message = "Deve essere prenotato almeno un posto")
    private int postiPrenotati;

    private LocalDate dataprenotazione;


}
