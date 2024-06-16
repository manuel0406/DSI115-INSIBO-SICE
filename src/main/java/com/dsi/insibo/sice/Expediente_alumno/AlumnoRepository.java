package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dsi.insibo.sice.entity.Alumno;

/**
 * Repositorio que define las operaciones para acceder y manipular la entidad
 * Alumno en la base de datos.
 * Utiliza Spring Data JPA y extiende JpaRepository para obtener métodos CRUD
 * básicos.
 */
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera, grado y
     * sección.
     *
     * carrera Nombre de la carrera.
     * grado Grado del bachillerato.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado AND b.seccion = :seccion")
    public List<Alumno> findAll(String carrera, String grado, String seccion);

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera y grado.
     *
     * carrera Nombre de la carrera.
     * grado Grado del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado")
    public List<Alumno> findAllCarreraGrado(String carrera, String grado);

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera y sección.
     *
     * carrera Nombre de la carrera.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.seccion = :seccion")
    public List<Alumno> findAllCarreraSeccion(String carrera, String seccion);

    /**
     * Busca todos los alumnos que pertenecen a un determinado grado y sección.
     *
     * grado Grado del bachillerato.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado AND b.seccion = :seccion")
    public List<Alumno> findAllGradoSeccion(String grado, String seccion);

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera.
     *
     * carrera Nombre de la carrera.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera")
    public List<Alumno> findAllPorCarrera(String carrera);

    /**
     * Busca todos los alumnos que pertenecen a un determinado grado.
     *
     * grado Grado del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado")
    public List<Alumno> findAllPorGrado(String grado);

    /**
     * Busca todos los alumnos que pertenecen a una determinada sección.
     *
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.seccion = :seccion")
    public List<Alumno> findAllPorSeccion(String seccion);

    /**
     * Busca todos los alumnos que pertenecen a un bachillerato específico por su
     * código.
     *
     * param codigoBachillerato Código del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    List<Alumno> findByBachilleratoCodigoBachillerato(String codigoBachillerato);

}
