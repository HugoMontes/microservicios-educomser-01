package com.msvc.huesped.service.impl;

import com.msvc.huesped.entity.Calificacion;
import com.msvc.huesped.entity.Hotel;
import com.msvc.huesped.entity.Huesped;
import com.msvc.huesped.exception.ResourceNotFoundException;
import com.msvc.huesped.repository.HuespedRepository;
import com.msvc.huesped.service.HuespedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class HuespedServiceImpl implements HuespedService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HuespedRepository huespedRepository;

    @Override
    public Huesped saveHuesped(Huesped huesped) {
        String randomHuespedId = UUID.randomUUID().toString();
        huesped.setHuespedId(randomHuespedId);
        return huespedRepository.save(huesped);
    }

    @Override
    public List<Huesped> getAllHuespedes() {
        return huespedRepository.findAll();
    }

    @Override
    public Huesped getHuesped(String huespedId) {
        // 1. Obtenemos el usuario
        Huesped huesped = huespedRepository.findById(huespedId)
                .orElseThrow(() -> new ResourceNotFoundException("Huesped no encontrado con ID : " + huespedId));
        // 2. Obtenemos el listado de calificaciones del huesped
        Calificacion[] calificacionesDelHuesped = restTemplate
                .getForObject("http://localhost:8083/calificaciones/huespedes/" + huesped.getHuespedId(), Calificacion[].class);
        // 3. Convertimos array de calificaciones a un ArrayList
        List<Calificacion> calificaciones = Arrays.asList(calificacionesDelHuesped);
        // 4. Mostramos el listado de calificaciones en consola
        log.info("Calificaciones del huesped: {}", calificaciones);

        // A la lista de calificaciones añadimos los datos de los hoteles
        List<Calificacion> listaCalificaciones = calificaciones.stream().map(calificacion -> {
            System.out.println("Hotel ID: " + calificacion.getHotelId());
            // Obtener datos del hotel por el ID
            ResponseEntity<Hotel> forEntity = restTemplate
                    .getForEntity("http://localhost:8082/hoteles/" + calificacion.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody();
            log.info("Respuesta con codigo de estado: {}", forEntity.getStatusCode());
            // Añadimos los datos del hotel a la calificacion
            calificacion.setHotel(hotel);
            // Retornar la calificacion para luego añadirla al List
            return calificacion;
        }).toList();

        // 5. Seteamos el ArrayList al huesped
        // huesped.setCalificaciones(Arrays.stream(calificacionesDelHuesped).toList());
        huesped.setCalificaciones(listaCalificaciones);

        // 6. Retornar el huesped con las calificaciones
        return huesped;
    }
}
