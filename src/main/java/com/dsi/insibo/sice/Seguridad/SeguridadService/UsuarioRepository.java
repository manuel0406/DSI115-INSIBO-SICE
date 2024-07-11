package com.dsi.insibo.sice.Seguridad.SeguridadService;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.dsi.insibo.sice.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>  {
    Optional <Usuario> findByCorreoUsuarioAndContrasenaUsuario(String correoUsuario, String contrasenaUsuario);
    Optional <Usuario> findByCorreoUsuario(String correoUsuario);
    Usuario findByIdUsuario(int idUsuario);
    List<Usuario> findAll();

    // LISTA DE USUARIOS
    List<Usuario> findByEnabledAndAccountLocked(boolean enabled, boolean accountLocked ,Pageable pageable);
    List<Usuario> findByEnabledAndAccountLocked(boolean enabled, boolean accountLocked);

    // BUSCAR EN ACTIVO
    @Query("SELECT u FROM Usuario u WHERE u.correoUsuario = :correoUsuario")
    public Optional <Usuario> findActivoByCorreo(String correoUsuario);

    //Estado: Activo | Desactivado | Bloqueado |
    List<Usuario> findByAccountLocked(boolean accountLocked, Pageable pageable);
    List<Usuario> findByAccountLocked(boolean accountLocked);

    // Eliminación de usuario-docente 
    @Transactional
    void deleteByDocente_DuiDocente(String duiDocente);


    @Transactional
    void deleteByPersonalAdministrativo_DuiPersonal(String duiPersonal);
    // Nuevo método para encontrar usuario por el Docente
    Usuario findByDocente_DuiDocente(String duiDocente);
    // Encontrar a personal docente por el duiPersonal
    Usuario findByPersonalAdministrativo_DuiPersonal(String duiPersonal);
}
