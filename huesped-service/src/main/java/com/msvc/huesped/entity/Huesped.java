package com.msvc.huesped.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "huespedes")
public class Huesped {
    @Id
    @Column(name = "id")
    private String huespedId;

    @Column(name = "nombre", length = 20)
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "informacion")
    private String informacion;

    @Transient
    private List<Calificacion> calificaciones = new ArrayList<>();

    @Transient
    private String informacionAdicional;
}
