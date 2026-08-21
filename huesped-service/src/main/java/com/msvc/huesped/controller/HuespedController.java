package com.msvc.huesped.controller;

import com.msvc.huesped.entity.Huesped;
import com.msvc.huesped.service.HuespedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/huespedes")
public class HuespedController {

    @Autowired
    private HuespedService huespedService;

    @PostMapping
    public ResponseEntity<Huesped> guardarHuesped(@RequestBody Huesped huespedRequest) {
        Huesped huesped = huespedService.saveHuesped(huespedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(huesped);
    }

    @GetMapping("/{huespedId}")
    public ResponseEntity<Huesped> obtenerHuesped(@PathVariable String huespedId) {
        Huesped huesped = huespedService.getHuesped(huespedId);
        return ResponseEntity.ok(huesped);
    }

    @GetMapping
    public ResponseEntity<List<Huesped>> listarHuespeds() {
        List<Huesped> huespeds = huespedService.getAllHuespedes();
        return ResponseEntity.ok(huespeds);
    }
}
