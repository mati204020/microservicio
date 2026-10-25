package com.service.contacto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.contacto.model.ContactoModelo;
@Repository
public interface ContactoRepository extends JpaRepository<ContactoModelo, Long>
{


}
