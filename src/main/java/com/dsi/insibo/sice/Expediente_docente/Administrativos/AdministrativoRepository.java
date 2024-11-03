package com.dsi.insibo.sice.Expediente_docente.Administrativos;

import org.springframework.data.repository.CrudRepository;

import com.dsi.insibo.sice.entity.PersonalAdministrativo;

public interface AdministrativoRepository extends CrudRepository<PersonalAdministrativo, String> {
    boolean existsByCorreoPersonal(String correoPersonal);
    boolean existsBynupPersonal(String nupPersonal);
    boolean existsBynitPersonal(String nitPersonal);
}
