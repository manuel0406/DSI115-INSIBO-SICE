package com.dsi.insibo.sice.Asistencia_personal.Asistencia_admnistrativa;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dsi.insibo.sice.entity.AsistenciaPersonal;

public interface AsistenciaAdmRepository extends JpaRepository<AsistenciaPersonal, Integer> {
        // Consulta de asistencia personal administrativo paginable
        @Query(value = "SELECT ad.id_asistencia_personal, ad.id_personal_aparato, ad.depart_personal, ad.turnop, ad.hora_entradap, "
                        + "ad.hora_salidap, da.numero_aparato_personal, d.nombre_personal, d.apellido_personal, d.correo_personal, "
                        + "d.telefono_personal FROM asistencia_personal ad " +
                        "JOIN personal_aparato da ON ad.id_personal_aparato = da.id_personal_aparato " +
                        "JOIN personal_administrativo d ON da.personal_dui_personal = d.dui_personal " +
                        "ORDER BY ad.id_asistencia_personal DESC", countQuery = "SELECT COUNT(*) FROM asistencia_personal ad JOIN personal_aparato da ON ad.id_asistencia_personal = da.id_asistencia_personal JOIN personal_administrativo d ON da.personal_dui_personal = d.dui_personal", nativeQuery = true)
        Page<Map<String, Object>> findAsistenciaAdministrativaP(Pageable pageable);

        // Lista de administrativos en asistencia (select)
        @Query(value = "SELECT d.nombre_personal, d.apellido_personal, da.id_personal_aparato FROM personal_administrativo AS d JOIN personal_aparato AS da ON d.dui_personal = da.personal_dui_personal ", nativeQuery = true)
        List<Map<String, Object>> findAdministrativosAparato();

        // Lista de departamentos en asistencia(select)
        @Query(value = "SELECT DISTINCT depart_personal FROM asistencia_personal", nativeQuery = true)
        List<Map<String, Object>> findAdministrativosDepartamento();

        // Listar por numero aparato personal (en vista por nombre)
        @Query(value = "SELECT ad.id_asistencia_personal, ad.id_personal_aparato, ad.depart_personal, ad.turnop, "
                        + " ad.hora_entradap, ad.hora_salidap, da.numero_aparato_personal, d.nombre_personal, "
                        + " d.apellido_personal, d.correo_personal, d.telefono_personal FROM asistencia_personal "
                        + "ad JOIN personal_aparato da ON ad.id_personal_aparato=da.id_personal_aparato "
                        + " JOIN personal_administrativo d ON da.personal_dui_personal=d.dui_personal "
                        + "WHERE da.id_personal_aparato= :numeroAparato ORDER BY ad.id_asistencia_personal DESC", nativeQuery = true)
        Page<Map<String, Object>> findAdministrativoPersonaAll(int numeroAparato, Pageable pageable);

        // Listar por departamento en asistencia
        @Query(value = "SELECT ad.id_asistencia_personal, ad.id_personal_aparato, ad.depart_personal, ad.turnop, ad.hora_entradap, "
                        + "ad.hora_salidap, da.numero_aparato_personal, d.nombre_personal, d.apellido_personal, d.correo_personal, "
                        + "d.telefono_personal FROM asistencia_personal ad " +
                        "JOIN personal_aparato da ON ad.id_personal_aparato = da.id_personal_aparato " +
                        "JOIN personal_administrativo d ON da.personal_dui_personal = d.dui_personal "
                        + "WHERE ad.depart_personal =:departamento"
                        + "ORDER BY ad.id_asistencia_personal DESC", nativeQuery = true)
        Page<Map<String, Object>> findAdministrativoDepartamentoAll(String departamento, Pageable pageable);

        // Listar por numeroAparto y departamento
        @Query(value = "SELECT ad.id_asistencia_personal, ad.id_personal_aparato, ad.depart_personal, ad.turnop, "
                        + "ad.hora_entradap, ad.hora_salidap, da.numero_aparato_personal, d.nombre_personal, "
                        + "d.apellido_personal, d.correo_personal, d.telefono_personal FROM "
                        + "asistencia_personal ad JOIN personal_aparato da ON ad.id_personal_aparato=da.id_personal_aparato "
                        + " JOIN personal_administrativo d ON da.personal_dui_personal=d.dui_personal "
                        + " WHERE da.id_personal_aparato=:numeroAparato AND ad.depart_personal=:departamento "
                        + "ORDER BY ad.id_asistencia_personal DESC", nativeQuery = true)
        Page<Map<String, Object>> findAdministrativoPersonaDepartamentoAll(int numeroAparato, String departamento,
                        Pageable pageable);

        // TARDIA
        // Asistencia tardia paginable
        @Query(value = "SELECT ap.id_asistencia_personal, ap.depart_personal, ap.hora_entradap, ap.hora_salidap, ap.turnop, "
                        + "pa.numero_aparato_personal, p.nombre_personal, p.apellido_personal, p.correo_personal, p.telefono_personal "
                        + "FROM asistencia_personal ap JOIN personal_aparato pa ON "
                        + "ap.id_personal_aparato = pa.id_personal_aparato JOIN "
                        + " personal_administrativo p ON pa.personal_dui_personal = p.dui_personal "
                        + "WHERE ((extract(hour from ap.hora_entradap) = 7 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 7 AND extract(hour from ap.hora_entradap) < 13) "
                        + "OR (extract(hour from ap.hora_entradap) = 13 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 13)) "
                        + "ORDER BY ap.id_asistencia_personal DESC", countQuery = "SELECT COUNT(*) FROM "
                                        + "asistencia_personal ap JOIN personal_aparato pa ON ap.id_personal_aparato = pa.id_personal_aparato "
                                        + "JOIN personal_administrativo p ON pa.personal_dui_personal = p.dui_personal "
                                        + "WHERE ((extract(hour from ap.hora_entradap) = 7 AND extract(minute from ap.hora_entradap) >= 30) "
                                        + "OR (extract(hour from ap.hora_entradap) > 7 AND extract(hour from ap.hora_entradap) < 13) "
                                        + "OR (extract(hour from ap.hora_entradap) = 13 AND extract(minute from ap.hora_entradap) >= 30) "
                                        + "OR (extract(hour from ap.hora_entradap) > 13))", nativeQuery = true)
        Page<Map<String, Object>> findAsistenciaAdministrativaByHorario(Pageable pageable);

        // Listado de id aparato en asistencia tardia administrativos
        @Query(value = "SELECT p.nombre_personal, p.apellido_personal, pa.id_personal_aparato "
                        + "FROM personal_administrativo AS p "
                        + "JOIN personal_aparato AS pa ON p.dui_personal = pa.personal_dui_personal "
                        + "WHERE pa.id_personal_aparato IN (SELECT ap.id_personal_aparato "
                        + "FROM asistencia_personal ap "
                        + "JOIN personal_aparato pa ON ap.id_personal_aparato = pa.id_personal_aparato "
                        + "JOIN personal_administrativo p ON pa.personal_dui_personal = p.dui_personal "
                        + "WHERE ((extract(hour from ap.hora_entradap) = 7 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 7 AND extract(hour from ap.hora_entradap) < 13) "
                        + "OR (extract(hour from ap.hora_entradap) = 13 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 13)) "
                        + "ORDER BY ap.id_asistencia_personal)", nativeQuery = true)
        List<Map<String, Object>> findAdministrativosTardia();

        // Listado de fechas disponible en tardias
        @Query(value = "SELECT DISTINCT EXTRACT (MONTH FROM hora_entradap) AS mes, EXTRACT(YEAR FROM hora_entradap) "
                        + " AS año FROM asistencia_personal ORDER BY año DESC, mes DESC", nativeQuery = true)
        List<Map<String, Object>> findMesAnioAdministrativo();

        // Listado tardias por numero aparato(en vista por nombre administrativo)
        @Query(value = "SELECT ap.id_asistencia_personal, ap.id_personal_aparato, ap.depart_personal, ap.turnop, ap.hora_entradap, "
                        + "ap.hora_salidap, pa.numero_aparato_personal, p.nombre_personal, p.apellido_personal, "
                        + "p.correo_personal, p.telefono_personal FROM asistencia_personal ap "
                        + "JOIN personal_aparato pa ON ap.id_personal_aparato = pa.id_personal_aparato "
                        + "JOIN personal_administrativo p ON pa.personal_dui_personal = p.dui_personal "
                        + "WHERE ((extract(hour from ap.hora_entradap) = 7 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 7 AND extract(hour from ap.hora_entradap) < 13) "
                        + "OR (extract(hour from ap.hora_entradap) = 13 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 13)) AND pa.id_personal_aparato = :numeroAparato ORDER BY ap.id_asistencia_personal DESC", nativeQuery = true)
        Page<Map<String, Object>> findAdministrativoTadiaAll(int numeroAparato, Pageable pageable);

        // Listar por mes y año
        @Query(value = "SELECT ap.id_asistencia_personal, ap.id_personal_aparato, ap.depart_personal, ap.turnop, ap.hora_entradap, "
                        + "ap.hora_salidap, pa.numero_aparato_personal, p.nombre_personal, p.apellido_personal, "
                        + "p.correo_personal, p.telefono_personal FROM asistencia_personal ap "
                        + "JOIN personal_aparato pa ON ap.id_personal_aparato = pa.id_personal_aparato "
                        + "JOIN personal_administrativo p ON pa.personal_dui_personal = p.dui_personal "
                        + "WHERE ((extract(hour from ap.hora_entradap) = 7 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 7 AND extract(hour from ap.hora_entradap) < 13) "
                        + "OR (extract(hour from ap.hora_entradap) = 13 AND extract(minute from ap.hora_entradap) >= 30) "
                        + "OR (extract(hour from ap.hora_entradap) > 13)) "
                        + "AND extract(month from ap.hora_entradap) = :mes "
                        + "AND extract(year from ap.hora_entradap) = :anio "
                        + "ORDER BY ap.id_asistencia_personal DESC", nativeQuery = true)
        Page<Map<String, Object>> findAdministrativoMesTardia(int mes, int anio, Pageable pageable);

}