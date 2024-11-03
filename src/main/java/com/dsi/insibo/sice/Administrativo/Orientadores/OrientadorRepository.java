package com.dsi.insibo.sice.Administrativo.Orientadores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.Orientador;

public interface OrientadorRepository extends JpaRepository<Orientador, Integer> {

    @Query("SELECT b FROM Bachillerato b JOIN Orientador o on b.codigoBachillerato=o.bachillerato.codigoBachillerato where o.docente.duiDocente=:dui AND b.anioAcademico.activoAnio=true")
    List<Bachillerato> listaSecciones(String dui);

    @Query("SELECT a FROM Alumno a where a.bachillerato.codigoBachillerato=:codigoBachillerato ")
    List<Alumno> listaAlumnos(int codigoBachillerato);

    @Query("SELECT o FROM Orientador o WHERE  o.bachillerato.codigoBachillerato=:codigoBachillerato")
    Orientador existeAsingnacion(int codigoBachillerato);

    @Query("SELECT o FROM Orientador o WHERE o.bachillerato.anioAcademico.activoAnio=true")
    List<Orientador> findAll();

}
