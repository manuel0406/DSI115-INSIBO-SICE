package com.dsi.insibo.sice.Expediente_docente.Administrativos.Anexos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dsi.insibo.sice.entity.AnexoPersonalAdministrativo;

@Repository
public interface AnexoAdministrativoRepository extends JpaRepository<AnexoPersonalAdministrativo, Integer> {

    @Query("SELECT a FROM AnexoPersonalAdministrativo a WHERE a.personalAdministrativo.duiPersonal = :duiPersonal")
    AnexoPersonalAdministrativo findByAdministrativoDui(@Param("duiPersonal") String duiPersonal);
    
    @Modifying
    @Query("DELETE FROM AnexoPersonalAdministrativo n WHERE n.personalAdministrativo.duiPersonal = :duiPersonal")
    void deleteByAdministrativoDui(@Param("duiPersonal") String duiPersonal);
}
