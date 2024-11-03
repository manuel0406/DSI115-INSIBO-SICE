package com.dsi.insibo.sice.Asistencia_personal.Asistencia_docente;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dsi.insibo.sice.entity.AsistenciaDocente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AsistenciaDocenteRepository extends JpaRepository<AsistenciaDocente, Integer> {

        // @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato,
        // ad.depar_docente, ad.turno, ad.hora_entrada, "
        // + " ad.hora_salida, da.numero_aparato_docente, d.nombre_docente,
        // d.apellido_docente, d.correo_docente, d.telefono_docente "
        // + "FROM asistencia_docente ad JOIN docente_aparato "
        // + " da ON ad.id_docente_aparato = da.id_docente_aparato "
        // + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
        // + "ORDER BY ad.id_asistencia DESC", nativeQuery = true)
        // List<Map<String, Object>> findAsistenciaDocente();

        // Obtener todas las asistencia paginable
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "ORDER BY ad.id_asistencia DESC", countQuery = "SELECT COUNT(*) FROM asistencia_docente ad "
                                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente", nativeQuery = true)
        Page<Map<String, Object>> findAsistenciaDocenteP(Pageable pageable);

        // lista de docentes en asistencia(con aparato en select)
        @Query(value = "SELECT d.nombre_docente, d.apellido_docente, da.id_docente_aparato " +
                        "FROM public.docente AS d JOIN public.docente_aparato " +
                        "AS da ON d.dui_docente = da.docente_dui_docente", nativeQuery = true)
        List<Map<String, Object>> findDocentesAparato();

        // lista de departamentos en asistencias docentes
        @Query(value = "SELECT DISTINCT depar_docente FROM public.asistencia_docente", nativeQuery = true)
        List<Map<String, Object>> findDocenteDepartamento();

        // Obtener todas las asistencia por numero aparato (en vista por nombre)
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ad.id_docente_aparato = :numeroAparato "
                        + "ORDER BY ad.id_asistencia DESC", nativeQuery = true)
        Page<Map<String, Object>> findDocentePersonaAll(int numeroAparato, Pageable pageable);

        /// Obtener todas las asistencia por departamento en asistencia
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ad.depar_docente = :departamento "
                        + "ORDER BY ad.id_asistencia DESC", nativeQuery = true)
        Page<Map<String, Object>> findDocenteDepartamentoAll(String departamento, Pageable pageable);

        // Obtener todas las asistencia por numero aparato (en vista por nombre) y
        // departamento
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE da.id_docente_aparato = :numeroAparato AND ad.depar_docente = :departamento "
                        + "ORDER BY ad.id_asistencia DESC", nativeQuery = true)
        Page<Map<String, Object>> findDocentePersonaDepartamentoAll(int numeroAparato, String departamento,
                        Pageable pageable);

        // Obtener todas las asistencia tardias paginable
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) "
                        + "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 13)) "
                        + "ORDER BY ad.id_asistencia DESC", countQuery = "SELECT COUNT(*) "
                                        + "FROM asistencia_docente ad "
                                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                                        + "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) "
                                        + "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) "
                                        + "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) "
                                        + "OR (extract(hour from ad.hora_entrada) > 13))", nativeQuery = true)
        Page<Map<String, Object>> findAsistenciaDocenteByHorario(Pageable pageable);

        // Obtener id aparato, nombre en asistencia tardias del docente
        @Query(value = "SELECT d.nombre_docente, d.apellido_docente, da.id_docente_aparato "
                        + "FROM public.docente AS d "
                        + "JOIN public.docente_aparato AS da ON d.dui_docente = da.docente_dui_docente "
                        + "WHERE da.id_docente_aparato IN (SELECT ad.id_docente_aparato "
                        + "FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) "
                        + "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 13)))", nativeQuery = true)
        List<Map<String, Object>> findDocentesTardia();

        // lista de mes y año disponibles en asistencia tardia
        @Query(value = "SELECT DISTINCT EXTRACT(MONTH FROM hora_entrada) AS mes, EXTRACT(YEAR FROM hora_entrada) AS año "
                        + "FROM public.asistencia_docente ORDER BY año DESC, mes DESC", nativeQuery = true)
        List<Map<String, Object>> findMesAnio();

        // Lista por numero aparato en tardia
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, "
                        + "ad.turno, ad.hora_entrada, ad.hora_salida, "
                        + "da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, "
                        + "d.correo_docente, d.telefono_docente "
                        + "FROM asistencia_docente ad JOIN docente_aparato da "
                        + "ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) "
                        + "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 13)) "
                        + "AND da.id_docente_aparato = :numeroAparato ", nativeQuery = true)
        Page<Map<String, Object>> findDocentePersonaTardiaAll(int numeroAparato, Pageable pageable);

        // Lista por mes y por año en tardia
        @Query(value = "SELECT ad.id_asistencia, ad.id_docente_aparato, ad.depar_docente, ad.turno, ad.hora_entrada, "
                        + "ad.hora_salida, da.numero_aparato_docente, d.nombre_docente, d.apellido_docente, "
                        + "d.correo_docente, d.telefono_docente FROM asistencia_docente ad "
                        + "JOIN docente_aparato da ON ad.id_docente_aparato = da.id_docente_aparato "
                        + "JOIN docente d ON da.docente_dui_docente = d.dui_docente "
                        + "WHERE ((extract(hour from ad.hora_entrada) = 7 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 7 AND extract(hour from ad.hora_entrada) < 13) "
                        + "OR (extract(hour from ad.hora_entrada) = 13 AND extract(minute from ad.hora_entrada) >= 30) "
                        + "OR (extract(hour from ad.hora_entrada) > 13)) AND extract(month from ad.hora_entrada) = :mes "
                        + "AND extract(year from ad.hora_entrada) = :anio "
                        + "ORDER BY ad.id_asistencia DESC", nativeQuery = true)
        Page<Map<String, Object>> findDocenteMesTardiaAll(int mes, int anio, Pageable pageable);
}
