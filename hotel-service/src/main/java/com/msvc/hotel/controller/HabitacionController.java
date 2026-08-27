package com.msvc.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    @GetMapping
    public ResponseEntity<List<String>> listarHabitaciones() {
        List<String> habitaciones = Arrays.asList("Habitacion 100", "Habitacion 101", "Habitacion 102");
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }
}
