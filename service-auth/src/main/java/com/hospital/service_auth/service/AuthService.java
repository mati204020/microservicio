package com.hospital.service_auth.service;

import com.hospital.service_auth.dto.AuthRequest;
import com.hospital.service_auth.model.Rol;
import com.hospital.service_auth.model.Usuario;
import com.hospital.service_auth.repository.RolRepository;
import com.hospital.service_auth.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    // Roles que existen en el sistema. Cualquier rol fuera de este set es rechazado.
    private static final Set<String> ROLES_VALIDOS = Set.of("CLIENTE", "FOTOGRAFO", "ADMINISTRADOR");

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
    private String secreto;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registro público (formulario de la web). SIEMPRE crea el usuario como CLIENTE,
     * sin importar lo que venga en el campo "roles" de la petición: ese campo se ignora
     * a propósito para que nadie pueda autoasignarse ADMINISTRADOR o FOTOGRAFO.
     */
    public String registrarCliente(AuthRequest request) {
        validarUsuarioNoExiste(request.getNombreUsuario());

        Usuario nuevoUsuario = construirUsuarioBase(request);

        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: El rol CLIENTE no existe en la DB."));
        nuevoUsuario.agregarRol(rolCliente);

        usuarioRepository.save(nuevoUsuario);
        return "Usuario registrado correctamente como CLIENTE.";
    }

    /**
     * Alta de personal interno (FOTOGRAFO / ADMINISTRADOR). Este método solo debe ser
     * invocado desde un endpoint protegido, accesible únicamente para usuarios con rol
     * ADMINISTRADOR (ver AutenticacionController + SecurityConfig).
     */
    public String registrarStaff(AuthRequest request) {
        validarUsuarioNoExiste(request.getNombreUsuario());

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            throw new RuntimeException("Debe indicar al menos un rol (FOTOGRAFO o ADMINISTRADOR).");
        }

        Usuario nuevoUsuario = construirUsuarioBase(request);

        for (String nombreRol : request.getRoles()) {
            String rolNormalizado = nombreRol.trim().toUpperCase();
            if (!ROLES_VALIDOS.contains(rolNormalizado)) {
                throw new RuntimeException("Rol no reconocido: " + nombreRol);
            }
            Rol rolEncontrado = rolRepository.findByNombreRol(rolNormalizado)
                    .orElseThrow(() -> new RuntimeException("Error: El rol " + nombreRol + " no existe en la DB."));
            nuevoUsuario.agregarRol(rolEncontrado);
        }

        usuarioRepository.save(nuevoUsuario);
        return "Usuario de personal registrado correctamente.";
    }

    private Usuario construirUsuarioBase(AuthRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        return usuario;
    }

    private void validarUsuarioNoExiste(String nombreUsuario) {
        if (usuarioRepository.findByNombreUsuario(nombreUsuario).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
    }

    @Transactional(readOnly = true)
    public String login(String nombreUsuario, String contrasena) {

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        List<String> rolesList = usuario.getRoles().stream()
                .map(Rol::getNombreRol)
                .collect(Collectors.toList());

        java.util.Date ahora = new java.util.Date();
        java.util.Date expiracion = new java.util.Date(ahora.getTime() + 86400000);

        return Jwts.builder()
                .setSubject(usuario.getNombreUsuario())
                .claim("userId", usuario.getId())
                .claim("roles", rolesList)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}