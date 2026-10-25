package com.estudio.service_pagos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import com.estudio.service_pagos.model.Pago;
import com.estudio.service_pagos.repository.PagoRepository;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Pago> listarTodos(){
        return pagoRepository.findAll();
    }

    public Optional <Pago> buscarPorId(Long id){
        return pagoRepository.findById(id);
    }

    public Pago guardar(Pago pago, String roles, Long userId){
        ReservaInfo reserva;
        try{
            reserva = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/v1/reservas/" + pago.getReservaId())
                .retrieve()
                .bodyToMono(ReservaInfo.class)
                .block();
        } catch (Exception e){
            throw new RuntimeException("La reserva no existe");
        }

        if (reserva == null) {
            throw new RuntimeException("La reserva no existe");
        }

        // ===== NUEVO: VALIDAR QUE LA RESERVA SEA DEL USUARIO LOGUEADO =====
        // ADMINISTRADOR puede pagar cualquier reserva (ej. pago presencial que registra el estudio).
        // Un CLIENTE solo puede pagar SU PROPIA reserva.
        boolean esAdmin = roles != null && roles.contains("ADMINISTRADOR");
        if (!esAdmin) {
            if (userId == null || reserva.getUsuarioId() == null || !reserva.getUsuarioId().equals(userId)) {
                throw new RuntimeException("No puedes pagar la reserva de otro usuario");
            }
        }

        if(pago.getEstado()== null){
            pago.setEstado("PAGADO");
        }

        if (pago.getFechaPago()==null){
                pago.setFechaPago(LocalDate.now());
        }
        return pagoRepository.save(pago);
    }


        
        public void eliminar(Long id){
            pagoRepository.deleteById(id);
        }

}