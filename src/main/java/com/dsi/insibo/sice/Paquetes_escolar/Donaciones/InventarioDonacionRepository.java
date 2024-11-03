package com.dsi.insibo.sice.Paquetes_escolar.Donaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Bachillerato;
import com.dsi.insibo.sice.entity.InventarioDonacion;

@Repository
public interface InventarioDonacionRepository extends JpaRepository<InventarioDonacion, Integer> {
    // Metodo para buscar una donación por tipo de prenda y talla
    Optional<InventarioDonacion> findByTipoPrendaAndTallaPrenda(String tipoPrenda, String tallaPrenda);

    @Query("SELECT i.cantidadPrenda FROM InventarioDonacion i WHERE i.idInventarioDonacion = :donacionId")
    int findCantidadById(@Param("donacionId") int donacionId);

    // Consulta para obtener los detalles del bachillerato basados en su codigo
    @Query("SELECT b FROM Bachillerato b WHERE b.codigoBachillerato = :codigo")
    Bachillerato findBachilleratoByCodigo(@Param("codigo") Integer codigoBachillerato);

    @Query(value = "SELECT nie, apellido_alumno, nombre_alumno, sexo_alumno, bachillerato_codigo_bachillerato, id_alumno "
            +
            "FROM public.alumno WHERE bachillerato_codigo_bachillerato = :codigoBachillerato", nativeQuery = true)
    List<Object[]> findAlumnosByCodigoBachillerato(@Param("codigoBachillerato") Integer codigoBachillerato);

    // Obtener año academico activo
    @Query("SELECT a.idAnioAcademico FROM AnioAcademico a WHERE a.activoAnio = true")
    Integer findActiveAnioAcademicoId();

    @Query(value = "SELECT codigo_bachillerato, grado, nombre_carrera, seccion FROM public.bachillerato WHERE anio_academico_id_anio_academico = :idAnioAcademico", nativeQuery = true)
    List<Object[]> findBachilleratoByAnioAcademicoNoDTO(@Param("idAnioAcademico") Integer idAnioAcademico);

}