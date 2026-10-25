package com.hospital.service_auth.model;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
@Entity
@Table(name = "usuarios")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@ToString(exclude = "roles") 
@EqualsAndHashCode(onlyExplicitlyIncluded = true) 
public class Usuario 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include 
    private Long id;
    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;
    @Column(name = "contrasena", nullable = false)
    private String contrasena;
    @Column(unique = true, nullable = false)
    private String correo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
    public void agregarRol(Rol rol) 
    {
    if (this.roles == null) 
    {
        this.roles = new HashSet<>();
    }
    this.roles.add(rol);
    }
}