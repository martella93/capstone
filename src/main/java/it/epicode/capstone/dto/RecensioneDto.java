package it.epicode.capstone.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecensioneDto {

    private int id;

    @NotNull(message = "La valutazione non può essere null")
    @Min(value = 1, message = "La valutazione deve essere almeno 1")
    @Max(value = 5, message = "La valutazione non può essere superiore a 5")
    private int valutazione;
    @Size(max = 1000)
    private String commento;

    private int prenotazioneId;
    private int userId;
    private int esperienzaId;
}
