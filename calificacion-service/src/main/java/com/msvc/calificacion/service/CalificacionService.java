package com.msvc.calificacion.service;

import com.msvc.calificacion.entity.Calificacion;

import java.util.List;

public interface CalificacionService {

    Calificacion create(Calificacion calificacion);

    List<Calificacion> getCalificaciones();
    
    List<Calificacion> getCalificacionesByHuespedId(String huespedId);

    List<Calificacion> getCalificacionesByHotelId(String hotelId);
}
