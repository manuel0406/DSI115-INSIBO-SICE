package com.dsi.insibo.sice.Expediente_docente.Docentes;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dsi.insibo.sice.entity.Docente;
import com.dsi.insibo.sice.entity.UsuarioRoleEnum;

public interface DocenteRepository extends CrudRepository<Docente, String> {
   
    @Query("SELECT d FROM Docente d  "
         + "JOIN Usuario u ON d.duiDocente = u.docente.duiDocente " 
         + "JOIN u.rolesUsuario r " +
           "WHERE r.roleEnum = :roleEnum AND u.docente.activoDocente = true")
    List<Docente> obtenerDocentesPorRol(@Param("roleEnum") UsuarioRoleEnum roleEnum);


    List<Docente> findByNombreDocenteContainingIgnoreCase(String nombre);
    
    boolean existsBynit(String nit);
    boolean existsBynup(String nup);
    boolean existsBynip(String nip);
    boolean existsBycorreoDocente(String correoDocente);
}
