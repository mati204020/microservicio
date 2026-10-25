package com.serice.resenas.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.serice.resenas.model.Modelo;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Modelo,Long>
{
    List<Modelo> findByClienteid(Long clienteid);
}