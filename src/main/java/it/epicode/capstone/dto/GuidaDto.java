package it.epicode.capstone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GuidaDto {

    private int id;
    @NotBlank(message = "Name is mandatory")
    private String nome;
    @NotBlank(message = "Surname is mandatory")
    private String cognome;
    @NotBlank(message = "Description is mandatory")
    private String descrizione;
    @NotBlank(message = "Languages is mandatory")
    private String lingue;
    @NotNull(message = "Experience is mandatory")
    private int anniEsperienza;

    private List<String> video = new ArrayList<>();
    private List<String> foto = new ArrayList<>();
}
