package com.dsi.insibo.sice.Expediente_docente.Docentes.Anexos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dsi.insibo.sice.entity.AnexoDocente;

@Repository
public interface AnexoDocenteRepository extends JpaRepository<AnexoDocente, Integer> {

    @Query("SELECT a FROM AnexoDocente a WHERE a.docente.duiDocente = :duiDocente")
    AnexoDocente findByDocenteDui(@Param("duiDocente") String duiDocente);
    
    @Modifying
    @Query("DELETE FROM AnexoDocente n WHERE n.docente.duiDocente = :duiDocente")
    void deleteByDocenteDui(@Param("duiDocente") String duiDocente);
}
