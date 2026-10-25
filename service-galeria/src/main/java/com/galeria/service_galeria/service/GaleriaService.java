package com.galeria.service_galeria.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.galeria.service_galeria.model.Galeria;
import com.galeria.service_galeria.repository.GaleriaRepository;

@Service

public class GaleriaService {

    @Autowired
    private GaleriaRepository galeriaRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Galeria> listarTodos(){
        return galeriaRepository.findAll();
    }

    public Optional<Galeria> buscarPorId(Long id){
        return galeriaRepository.findById(id);
    }

    public Galeria guardar(Galeria galeria){
        try{
            Object reserva = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/api/v1/reservas/" + galeria.getReservaId())
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            System.out.println(reserva);


        } catch (Exception e){
            throw new RuntimeException("La reserva no existe");
        }
        if(galeria.getEstado()== null){
            galeria.setEstado("PENDIENTE");
        }

        if(galeria.getFechaCreacion()==null){
            galeria.setFechaCreacion(LocalDate.now());
        }
        return galeriaRepository.save(galeria);
    }

    public void eliminar(Long id){
        galeriaRepository.deleteById(id);
    }



}
