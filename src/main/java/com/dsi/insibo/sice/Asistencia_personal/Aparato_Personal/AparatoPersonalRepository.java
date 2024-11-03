package com.dsi.insibo.sice.Asistencia_personal.Aparato_Personal;
import org.springframework.data.repository.CrudRepository;
import com.dsi.insibo.sice.entity.PersonalAparato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AparatoPersonalRepository extends CrudRepository<PersonalAparato, Integer>{
    PersonalAparato findByNumeroAparatoPersonal(int numeroAparatoPersonal);

    //Listado de identificadores de aparato del personal
    @Query("SELECT numeroAparatoPersonal FROM PersonalAparato")
    List<Integer> obtenerIDAparatoPersonal();

    Page<PersonalAparato> findAll(Pageable pageable);

}
