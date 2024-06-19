package it.epicode.capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DatePrenotate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "esperienza_id")
    private Esperienza esperienza;

    private LocalDate dataPrenotata;

    private int postiPrenotati;
}
