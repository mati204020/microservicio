package com.estudio.service_reservas.repository;

import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.estudio.service_reservas.model.Reserva;

@Repository

public interface ReservaRepository extends JpaRepository<Reserva,Long>
{
    List <Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByFecha(LocalDate fecha);

    boolean existsByFechaAndHoraAndFotografo_Id(LocalDate fecha, LocalTime hora, Long fotografoId);

    @Query("""
           SELECT f.nombre, COUNT(r)
           FROM Reserva r
           JOIN r.fotografo f
           GROUP BY f.nombre
           """)
    List<Object[]> reservarPorFotografo();

    @Query("""
           SELECT r FROM Reserva r
           JOIN FETCH r.tipoSesion ts
           JOIN FETCH r.fotografo f
           """)
    List<Reserva> findReservasCompletas();

}
