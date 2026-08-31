package com.msvc.huesped.service.impl;

import com.msvc.huesped.entity.Calificacion;
import com.msvc.huesped.entity.Hotel;
import com.msvc.huesped.entity.Huesped;
import com.msvc.huesped.exception.ResourceNotFoundException;
import com.msvc.huesped.external.service.CalificacionService;
import com.msvc.huesped.external.service.HotelService;
import com.msvc.huesped.repository.HuespedRepository;
import com.msvc.huesped.service.HuespedService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class HuespedServiceImpl implements HuespedService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private CalificacionService calificacionService;

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

    private int cantidadReintentos = 1;

    // @CircuitBreaker(name = "huespedServiceBreaker", fallbackMethod = "fallbackHuesped")
    @Retry(name = "huespedServiceRetry", fallbackMethod = "fallbackHuesped")
    public Huesped getHuesped(String huespedId) {
        log.info("Listar un solo huesped: HuespedServiceImpl");
        log.info("Cantidad de intentos: {}", cantidadReintentos);
        cantidadReintentos++;

        // 1. Obtenemos el usuario
        Huesped huesped = huespedRepository.findById(huespedId)
                .orElseThrow(() -> new ResourceNotFoundException("Huesped no encontrado con ID : " + huespedId));

        // 2. Obtenemos el listado de calificaciones del huesped
//        Calificacion[] calificacionesDelHuesped = restTemplate
//                // .getForObject("http://localhost:8083/calificaciones/huespedes/" + huesped.getHuespedId(), Calificacion[].class);
//                .getForObject("http://CALIFICACION-SERVICE/calificaciones/huespedes/" + huesped.getHuespedId(), Calificacion[].class);
//        // 3. Convertimos array de calificaciones a un ArrayList
//        List<Calificacion> calificaciones = Arrays.asList(calificacionesDelHuesped);
        List<Calificacion> calificaciones = calificacionService.calificacionesHuesped(huesped.getHuespedId());

        // 4. Mostramos el listado de calificaciones en consola
        log.info("Calificaciones del huesped: {}", calificaciones);

        // A la lista de calificaciones añadimos los datos de los hoteles
        List<Calificacion> listaCalificaciones = calificaciones.stream().map(calificacion -> {
            System.out.println("Hotel ID: " + calificacion.getHotelId());

            // Obtener datos del hotel por el ID
//            ResponseEntity<Hotel> forEntity = restTemplate
//                    // .getForEntity("http://localhost:8082/hoteles/" + calificacion.getHotelId(), Hotel.class);
//                    .getForEntity("http://HOTEL-SERVICE/hoteles/" + calificacion.getHotelId(), Hotel.class);
//            Hotel hotel = forEntity.getBody();
//            log.info("Respuesta con codigo de estado: {}", forEntity.getStatusCode());
            Hotel hotel = hotelService.getHotel(calificacion.getHotelId());

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

    public Huesped fallbackHuesped(String huespedId, Exception exception) {
        Huesped huesped = huespedRepository.findById(huespedId).orElseThrow();
        log.info("Ejecutar fallbackHuesped: {}", huesped);
        huesped.setInformacionAdicional("Algunos servicios no estan disponibles");
        huesped.setCalificaciones(new ArrayList<>());
        return huesped;
    }
}
