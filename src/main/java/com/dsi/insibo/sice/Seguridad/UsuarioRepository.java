package com.dsi.insibo.sice.Seguridad;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.dsi.insibo.sice.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>  {
    Usuario findByCorreoUsuarioAndContrasenaUsuario(String correoUsuario, String contrasenaUsuario);
    Usuario findByCorreoUsuario(String correoUsuario);
    Usuario findByIdUsuario(int idUsuario);
    List<Usuario> findAll();
    //Estado: Activo | Desactivado | Bloqueado |
    List<Usuario> findByEstadoUsuario(String estadoUsuario, Pageable pageable);
    List<Usuario> findByEstadoUsuario(String estadoUsuario);
    @Transactional
    void deleteByDocente_DuiDocente(String duiDocente);
    @Transactional
    void deleteByPersonalAdministrativo_DuiPersonal(String duiPersonal);
    // Nuevo método para encontrar usuario por el Docente
    Usuario findByDocente_DuiDocente(String duiDocente);
}
