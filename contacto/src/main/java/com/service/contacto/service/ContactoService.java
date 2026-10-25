package com.service.contacto.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.contacto.model.ContactoModelo;
import com.service.contacto.repository.ContactoRepository;
@Service
public class ContactoService 
{
    @Autowired
    private ContactoRepository repository;

    public List<ContactoModelo> listar(){
        return repository.findAll();
    }
    public ContactoModelo guardar(ContactoModelo contactoModelo) {
        return repository.save(contactoModelo);
    }

    public ContactoModelo buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    public ContactoModelo actualizar(Long id, ContactoModelo contactoNuevo) {
    ContactoModelo contactoExistente = repository.findById(id).orElse(null);

    if (contactoExistente == null) {
        return null;
    }

    contactoExistente.setNombre(contactoNuevo.getNombre());
    contactoExistente.setCorreo(contactoNuevo.getCorreo());
    contactoExistente.setTelefono(contactoNuevo.getTelefono());
    contactoExistente.setAsunto(contactoNuevo.getAsunto());
    contactoExistente.setMensaje(contactoNuevo.getMensaje());

    return repository.save(contactoExistente);
    }


}
