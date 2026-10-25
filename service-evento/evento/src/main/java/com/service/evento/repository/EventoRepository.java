package com.service.evento.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.service.evento.model.Eventoi;


public interface EventoRepository extends JpaRepository<Eventoi, Long>
{
    

}
