package com.serice.resenas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.serice.resenas.model.Modelo;
import com.serice.resenas.repository.ResenaRepository;

@Service
public class ResenasService 
{
    @Autowired
    private ResenaRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<Modelo> listar(){
        return repository.findAll();
    }

    public Modelo guardar(Modelo resena, String roles, Long userId) {
        EventoInfo evento;
        try {
            evento = webClientBuilder.build()
                .get()
                .uri("http://localhost:8085/eventos/" + resena.getEventoid())
                .retrieve()
                .bodyToMono(EventoInfo.class)
                .block();
        } catch (Exception e) {
            throw new RuntimeException("El evento no existe");
        }

        if (evento == null) {
            throw new RuntimeException("El evento no existe");
        }

        // ADMINISTRADOR/FOTOGRAFO pueden crear reseñas para cualquier evento
        // (ej. cargar reseñas antiguas). Un CLIENTE solo puede reseñar SU PROPIO evento.
        boolean esSoloCliente = roles != null && roles.contains("CLIENTE")
                && !roles.contains("ADMINISTRADOR") && !roles.contains("FOTOGRAFO");

        if (esSoloCliente) {
            if (userId == null || evento.getUsuarioId() == null || !evento.getUsuarioId().equals(userId)) {
                throw new RuntimeException("No puedes reseñar un evento que no es tuyo");
            }
        }

        return repository.save(resena);
    }

    public Modelo buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Modelo actualizar(Long id, Modelo nuevo) {
        Modelo existente = repository.findById(id).orElse(null);

        if (existente == null) {
            return null;
        }

        existente.setClasificacion(nuevo.getClasificacion());
        existente.setClienteid(nuevo.getClienteid());
        existente.setComentario(nuevo.getComentario());
        existente.setEventoid(nuevo.getEventoid());
        existente.setFecha(nuevo.getFecha());

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Modelo> listarPorCliente(Long clienteId){
        return repository.findByClienteid(clienteId);
    }
}