package com.msvc.huesped.external.service;

import com.msvc.huesped.entity.Calificacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CALIFICACION-SERVICE")
public interface CalificacionService {

    @GetMapping("/calificaciones/huespedes/{huespedId}")
    List<Calificacion> calificacionesHuesped(@PathVariable String huespedId);
}
