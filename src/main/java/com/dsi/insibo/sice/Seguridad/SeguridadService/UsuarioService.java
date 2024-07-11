package com.dsi.insibo.sice.Seguridad.SeguridadService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import com.dsi.insibo.sice.entity.Usuario;
import com.dsi.insibo.sice.entity.UsuarioRoles;

import org.springframework.transaction.annotation.Transactional;

@Service
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolesRepository usuarioRolesRepository;

    /* BUSQUEDA DE CORREO - Permitido para todos
     Usos:  
        1. Para buscar correos de inicio de sesión.
        2. Para validar el envio de correos de restauración de contraseña.
    */ 
    @PreAuthorize("permitAll()")
    public Usuario buscarPorCorreo(String correoUsuario) {
        return usuarioRepository.findByCorreoUsuario(correoUsuario).orElse(null);
    }

    /* GUARDAR USUARIO - Permitido para todos
     Usos:  
        1. Para restaurar la contraseña.
        2. Para guardar un nuevo usuario.
    */
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void asignarRol(Usuario usuario, Long idRol) {
        UsuarioRoles rol = usuarioRolesRepository.findById(idRol).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.getRolesUsuario().add(rol);
        usuarioRepository.save(usuario);
    }

    //USUARIOS ACTIVOS
    public List<Usuario> listaUsuariosActivosIntervalos(int offset){
        Pageable pageable = PageRequest.of(offset, 7);
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(true, true, pageable);
    }

    public List<Usuario> listaUsuariosActivos(){
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(true,true);
    }

    // USUARIOS BLOQUEADOS - DENEGADOS
    public List<Usuario> listaUsuariosBloqueados(){
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(true, false);
    }

    public List<Usuario> listaUsuariosBloqueadosIntervalos(int offset){
        Pageable pageable = PageRequest.of(offset, 7);
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(true, false,pageable);
    }

    // USUARIOS INACTIVOS - SIN CREDENCIALES

    public List<Usuario> listaUsuariosDesactivados(){
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(false, true);
    }

    public List<Usuario> listaUsuariosDesactivadosIntervalos(int offset){
        Pageable pageable = PageRequest.of(offset, 7);
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(false,true, pageable);
    }

    // USUARIOS RECHAZADOS - SIN PERMISOS
    public List<Usuario> listaUsuariosRechazados(){
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(false, false);
    }

    public List<Usuario> listaUsuariosRechazadosIntervalos(int offset){
        Pageable pageable = PageRequest.of(offset, 7);
        return (List<Usuario>) usuarioRepository.findByEnabledAndAccountLocked(false, false, pageable);
    }





    public Usuario buscarPorCorreoYContrasena(String correoUsuario, String contrasenaUsuario) {
        return usuarioRepository.findByCorreoUsuarioAndContrasenaUsuario(correoUsuario, contrasenaUsuario).orElse(null);
    }


    public Usuario buscarPorCorreoActivo(String correoUsuario) {
        return usuarioRepository.findActivoByCorreo(correoUsuario).orElse(null);
    }

    public Usuario buscarPorIdUsuario(int idUsuario) {
        return usuarioRepository.findByIdUsuario(idUsuario);
    }

    @Transactional
    public void eliminarUsuarioPorDocenteId(String idDocente) {
        // Encuentra el usuario basado en el idDocente
        Usuario usuario = usuarioRepository.findByDocente_DuiDocente(idDocente);
        // Limpia las relaciones ManyToMany
        usuario.getRolesUsuario().clear();
        usuarioRepository.save(usuario);
        // Elimina el usuario
        usuarioRepository.deleteByDocente_DuiDocente(idDocente);
    }
    
    @Transactional
    public void eliminarUsuarioPorPersonalId(String idPersonal) {
         // Encuentra el usuario basado en el idDocente
         Usuario usuario = usuarioRepository.findByDocente_DuiDocente(idPersonal);
         // Limpia las relaciones ManyToMany
         usuario.getRolesUsuario().clear();
         usuarioRepository.save(usuario);
         // Elimina el usuario
        usuarioRepository.deleteByPersonalAdministrativo_DuiPersonal(idPersonal);
    }

    public Usuario buscarPorIdDocente(String duiDocente) {
        return usuarioRepository.findByDocente_DuiDocente(duiDocente);
    }

    public Usuario buscarPorIdPersonal(String duiPersonal) {
        return usuarioRepository.findByPersonalAdministrativo_DuiPersonal(duiPersonal);
    }

}
