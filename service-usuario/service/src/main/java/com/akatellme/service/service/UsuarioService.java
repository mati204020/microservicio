package com.akatellme.service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akatellme.service.model.TipoDeusuario;
import com.akatellme.service.model.TipoSesion;
import com.akatellme.service.model.Usuario;

import com.akatellme.service.repository.TipoSesionRepository;
import com.akatellme.service.repository.TipoUsuarioRepository;
import com.akatellme.service.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService 
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private TipoSesionRepository tipoSesionRepository;

    public List<Usuario> listartodo()
    {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> BuscarporID(Long id)
    {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario guardar(Usuario usuario)
    {
        TipoDeusuario tipoUsuario = tipoUsuarioRepository
                .findById(usuario.getTipoDeusuario().getId())
                .orElseThrow();

        TipoSesion tipoSesion = tipoSesionRepository
                .findById(usuario.getTipoSesion().getId())
                .orElseThrow();

        usuario.setTipoDeusuario(tipoUsuario);
        usuario.setTipoSesion(tipoSesion);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Long id, Usuario usuario)
    {
        Usuario usuarioExistente = usuarioRepository
                .findById(id)
                .orElseThrow();

        TipoDeusuario tipoUsuario = tipoUsuarioRepository
                .findById(usuario.getTipoDeusuario().getId())
                .orElseThrow();

        TipoSesion tipoSesion = tipoSesionRepository
                .findById(usuario.getTipoSesion().getId())
                .orElseThrow();

        usuarioExistente.setRun(usuario.getRun());
        usuarioExistente.setNombres(usuario.getNombres());
        usuarioExistente.setApellidos(usuario.getApellidos());
        usuarioExistente.setCorreo(usuario.getCorreo());

        usuarioExistente.setTipoDeusuario(tipoUsuario);
        usuarioExistente.setTipoSesion(tipoSesion);

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void eliminar(Long id)
    {
        usuarioRepository.deleteById(id);
    }
}