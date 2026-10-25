package com.estudio.service_reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.estudio.service_reservas.model.Reserva;
import com.estudio.service_reservas.repository.ReservaRepository;

import com.estudio.service_reservas.repository.FotografoRepository;
import com.estudio.service_reservas.repository.TipoSesionRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservaService 
{
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private FotografoRepository fotografoRepository;

    @Autowired
    private TipoSesionRepository tipoSesionRepository;

    // WEBCLIENT
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    public List<Reserva> listarTodos(){
        return reservaRepository.findAll();
    }

    public Optional<Reserva> buscarPorId(Long id){
        return reservaRepository.findById(id);
    }

    @Transactional
    public Reserva guardar(Reserva reserva){

        // VALIDAR USUARIO
        try{
            Object usuario = webClientBuilder.build()
                .get()
                .uri("http://localhost:8093/auth/usuarios/" + reserva.getUsuarioId())
                .retrieve()
                .bodyToMono(Object.class)
                .block();
            System.out.println(usuario);
        } catch(Exception e){
            throw new RuntimeException("El usuario no existe");
        }

        // ESTADO POR DEFECTO
        if(reserva.getEstado() == null){
            reserva.setEstado("PENDIENTE");
        }

        if(!fotografoRepository.existsById(reserva.getFotografo().getId())){
            throw new RuntimeException("El fotografo no existe");
        }
        
        if(!tipoSesionRepository.existsById(reserva.getTipoSesion().getId())){
            throw new RuntimeException("El tipo de sesion no existe");
        }

        // VALIDAR HORARIO (choque interno en la propia tabla reservas)
        boolean existe = reservaRepository
            .existsByFechaAndHoraAndFotografo_Id(
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getFotografo().getId()
            );

        if(existe){
            throw new RuntimeException(
                "Ya existe reserva en ese horario"
            );
        }

        // ===== NUEVO: VALIDAR CONTRA EL MICROSERVICIO DE DISPONIBILIDAD =====
        Boolean disponible;
        try {
            disponible = webClientBuilder.build()
                .get()
                .uri("http://localhost:8090/api/v1/disponibilidades/verificar?fecha={fecha}&hora={hora}&fotografoId={fid}",
                    reserva.getFecha(), reserva.getHora(), reserva.getFotografo().getId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo verificar la disponibilidad del horario");
        }

        if (disponible == null || !disponible) {
            throw new RuntimeException("El horario no está disponible para este fotógrafo");
        }

        Reserva reservaGuardada = reservaRepository.save(reserva);

        // ===== NUEVO: MARCAR EL HORARIO COMO RESERVADO =====
        try {
            webClientBuilder.build()
                .put()
                .uri("http://localhost:8090/api/v1/disponibilidades/marcar-reservado?fecha={fecha}&hora={hora}&fotografoId={fid}",
                    reserva.getFecha(), reserva.getHora(), reserva.getFotografo().getId())
                .retrieve()
                .toBodilessEntity()
                .block();
        } catch (Exception e) {
            System.out.println("Advertencia: no se pudo actualizar disponibilidad: " + e.getMessage());
        }

        return reservaGuardada;
    }

    public void eliminar(Long id){
        reservaRepository.deleteById(id);
    }


    public List<Reserva> listarPorUsuario(Long usuarioId){
        return reservaRepository.findByUsuarioId(usuarioId);
    }
}