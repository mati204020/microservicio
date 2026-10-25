package com.akatellme.service.repository;
import java.util.List;


import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.akatellme.service.model.Usuario;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long>

{
   Usuario findByRun(String run);

    @Query("""
            SELECT u.tipoDeusuario.nombre AS tipoUsuario,
            COUNT(u) AS cantidad
            FROM Usuario u
            GROUP BY u.tipoDeusuario.nombre
            """)
    List<Object[]> conteoPorUsuario();

    @Query("""
            SELECT u FROM Usuario u
            JOIN FETCH u.tipoSesion ts
            WHERE ts IS NOT NULL
            """)
    List<Usuario> findUsuariofichacompleta();

    

}
