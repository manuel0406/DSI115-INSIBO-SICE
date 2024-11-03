package com.dsi.insibo.sice.Administrativo.Bachilleratos.Repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.AnioAcademico;
import com.dsi.insibo.sice.entity.Bachillerato;

@Repository
public interface AnioRepository extends JpaRepository<AnioAcademico, Integer> {
    
    @Query("SELECT a FROM AnioAcademico a WHERE a.anio = :anio")
    AnioAcademico buscarAnio(@Param("anio") int anio);

    @Query("SELECT b FROM Bachillerato b JOIN  b.anioAcademico a WHERE a.idAnioAcademico = :idAnioAcademico")
    List<Bachillerato> listaBuscarAnio(@Param("idAnioAcademico") int idAnioAcademico);

    //RETORNA EL AÑO QUE TIENE LA MATRICULA ACTIVA
    @Query("SELECT a FROM AnioAcademico a WHERE a.activoMatricula = true")
    AnioAcademico matriculaActiva();

    //RETORNA EL AÑO QUE TIENE LA MATRICULA ACTIVA
    @Query("SELECT a FROM AnioAcademico a WHERE a.activoAnio = true")
    AnioAcademico anioActivo();
    
    
}
