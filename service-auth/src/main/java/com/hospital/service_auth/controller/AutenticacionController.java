package com.hospital.service_auth.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import com.hospital.service_auth.dto.AuthRequest;
import com.hospital.service_auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autentication", description = "Endpoints para registro y login de usuario")
public class AutenticacionController
{
    @Autowired
    private AuthService authService;

    @Operation(
        summary = "Registrar un nuevo cliente",
        description = "Registro público. El usuario SIEMPRE queda con rol CLIENTE; " +
                       "cualquier rol enviado en el body es ignorado."
    )
    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody AuthRequest request)
    {
        try {
            return ResponseEntity.ok(authService.registrarCliente(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Registrar personal interno (FOTOGRAFO / ADMINISTRADOR)",
        description = "Solo puede ser usado por un usuario autenticado con rol ADMINISTRADOR."
    )
    @PostMapping("/registrar-staff")
    public ResponseEntity<String> registrarStaff(@Valid @RequestBody AuthRequest request)
    {
        try {
            return ResponseEntity.ok(authService.registrarStaff(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Iniciar sesion", description = "Retorna el Token JWT si las credenciales son válidas")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request)
    {
        try {
            String token = authService.login(request.getNombreUsuario(), request.getContrasena());
            return ResponseEntity.ok(token);
        }
        catch(RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}