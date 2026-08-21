package com.msvc.calificacion.controller;

import com.msvc.calificacion.entity.Calificacion;
import com.msvc.calificacion.service.CalificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @PostMapping
    public ResponseEntity<Calificacion> guardarCalificacion(@RequestBody Calificacion calificacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionService.create(calificacion));
    }

    @GetMapping
    public ResponseEntity<List<Calificacion>> listarCalificaciones() {
        return ResponseEntity.ok(calificacionService.getCalificaciones());
    }

    @GetMapping("/huespedes/{huespedId}")
    public ResponseEntity<List<Calificacion>> listarCalificacionesPorUsuarioId(@PathVariable String huespedId) {
        return ResponseEntity.ok(calificacionService.getCalificacionesByHuespedId(huespedId));

    }

    @GetMapping("/hoteles/{hotelId}")
    public ResponseEntity<List<Calificacion>> listarCalificacionesPorHotelId(@PathVariable String hotelId) {
        return ResponseEntity.ok(calificacionService.getCalificacionesByHotelId(hotelId));
    }
}