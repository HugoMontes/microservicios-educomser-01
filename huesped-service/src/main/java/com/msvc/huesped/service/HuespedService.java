package com.msvc.huesped.service;

import com.msvc.huesped.entity.Huesped;

import java.util.List;

public interface HuespedService {
    Huesped saveHuesped(Huesped huesped);

    List<Huesped> getAllHuespedes();

    Huesped getHuesped(String huespedId);
}
