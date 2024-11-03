package com.dsi.insibo.sice.Paquetes_escolar.Zapatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Zapatos;

@Repository
public interface ZapatosRepository extends JpaRepository<Zapatos, Integer> {
        public List<Zapatos> findByAlumno(Alumno alumno);

        @Query(value = "SELECT DISTINCT z.fecha_entega_zapatos FROM public.zapatos z JOIN public.alumno a ON z.alumno_id_alumno = a.id_alumno WHERE a.bachillerato_codigo_bachillerato = :idBachillerato", nativeQuery = true)
        List<String> findDistinctFechasByBachillerato(@Param("idBachillerato") int idBachillerato);

        @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, z.fecha_entega_zapatos, z.zapatalo_entregado, z.id_zapatos, z.talla_zapato "
                        + "FROM alumno a JOIN zapatos z ON a.id_alumno = z.alumno_id_alumno "
                        + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
                        + "AND z.fecha_entega_zapatos = CAST(:fecha AS DATE) "
                        + "AND z.zapatalo_entregado = :estado ORDER BY a.apellido_alumno ASC", nativeQuery = true)
        List<Object[]> filtrarPorZapatos(@Param("idBachillerato") int idBachillerato,
                        @Param("fecha") String fecha,
                        @Param("estado") Boolean estado);

        @Query(value = "SELECT a.id_alumno, a.apellido_alumno, a.nie, a.nombre_alumno, z.fecha_entega_zapatos, z.zapatalo_entregado, z.talla_zapato, z.id_zapatos "
                        + "FROM alumno a JOIN zapatos z ON a.id_alumno = z.alumno_id_alumno "
                        + "WHERE a.bachillerato_codigo_bachillerato = :idBachillerato "
                        + "AND z.fecha_entega_zapatos = CAST(:fecha AS DATE) ORDER BY a.apellido_alumno ASC", nativeQuery = true)
        List<Object[]> filtrarPorZapatosSinEstado(@Param("idBachillerato") int idBachillerato,
                        @Param("fecha") String fecha);

}
