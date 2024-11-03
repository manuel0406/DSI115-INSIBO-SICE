package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dsi.insibo.sice.entity.Alumno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE  b.nombreCarrera = :carrera AND b.grado = :grado AND b.seccion = :seccion AND an.activoAnio = true")
    public List<Alumno> findAllCarreraGradoSeccion(String carrera, String grado, String seccion);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an WHERE b.nombreCarrera = :carrera AND b.grado = :grado AND b.seccion = :seccion AND a.sexoAlumno = :genero AND an.activoAnio = true")
    public List<Alumno> findAll(String carrera, String grado, String seccion, String genero);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an WHERE an.activoAnio = true")
    public List<Alumno> findAll();

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera y grado.
     *
     * carrera Nombre de la carrera.
     * grado Grado del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.nombreCarrera = :carrera AND b.grado = :grado AND an.activoAnio = true")
    public List<Alumno> findAllCarreraGrado(String carrera, String grado);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an   WHERE  b.nombreCarrera = :carrera AND b.grado = :grado  AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllCarreraGradoGenero(String carrera, String grado, String genero);

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera y sección.
     *
     * carrera Nombre de la carrera.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.nombreCarrera = :carrera AND b.seccion = :seccion AND an.activoAnio = true")
    public List<Alumno> findAllCarreraSeccion(String carrera, String seccion);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE  b.nombreCarrera = :carrera AND  b.seccion = :seccion AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllCarreraSeccionGenero(String carrera, String seccion, String genero);

    /**
     * Busca todos los alumnos que pertenecen a un determinado grado y sección.
     *
     * grado Grado del bachillerato.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.grado = :grado AND b.seccion = :seccion AND an.activoAnio = true")
    public List<Alumno> findAllGradoSeccion(String grado, String seccion);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE  b.grado = :grado AND b.seccion = :seccion AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllGradoSeccionGenero(String grado, String seccion, String genero);

    /**
     * Busca todos los alumnos que pertenecen a una determinada carrera.
     *
     * carrera Nombre de la carrera.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.nombreCarrera = :carrera AND an.activoAnio = true")
    public List<Alumno> findAllPorCarrera(String carrera);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE  b.nombreCarrera = :carrera AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllCarreraGenero(String carrera, String genero);

    /**
     * Busca todos los alumnos que pertenecen a un determinado grado.
     *
     * grado Grado del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.grado = :grado AND an.activoAnio = true")
    public List<Alumno> findAllPorGrado(String grado);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE  b.grado = :grado AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllGradoGenero(String grado, String genero);

    /**
     * Busca todos los alumnos que pertenecen a una determinada sección.
     *
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.seccion = :seccion AND an.activoAnio = true")
    public List<Alumno> findAllPorSeccion(String seccion);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE b.seccion = :seccion AND a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllSeccionGenero(String seccion, String genero);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an  WHERE a.sexoAlumno=:genero AND an.activoAnio = true")
    public List<Alumno> findAllGenero(String genero);

    /**
     * Busca todos los alumnos que pertenecen a un bachillerato específico por su
     * código.
     *
     * param codigoBachillerato Código del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    @Query("SELECT a FROM Alumno a WHERE a.bachillerato.codigoBachillerato=:codigoBachillerato ORDER BY a.apellidoAlumno ASC")
    List<Alumno> findByBachilleratoCodigoBachillerato(int codigoBachillerato);

    // Métodos de consulta paginados
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado AND b.seccion = :seccion")
    public Page<Alumno> findAll(String carrera, String grado, String seccion, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado")
    public Page<Alumno> findAllCarreraGrado(String carrera, String grado, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.seccion = :seccion")
    public Page<Alumno> findAllCarreraSeccion(String carrera, String seccion, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado AND b.seccion = :seccion")
    public Page<Alumno> findAllGradoSeccion(String grado, String seccion, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera")
    public Page<Alumno> findAllPorCarrera(String carrera, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado")
    public Page<Alumno> findAllPorGrado(String grado, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.seccion = :seccion")
    public Page<Alumno> findAllPorSeccion(String seccion, Pageable pageable);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an WHERE (CAST(a.nie AS string) LIKE %:nie% AND a.nombreAlumno ILIKE %:nombre% AND a.apellidoAlumno ILIKE %:apellido%) AND an.anio = :anio")
    List<Alumno> listadoMatricula(@Param("nie") String nie, @Param("nombre") String nombre,
            @Param("apellido") String apellido, @Param("anio") int anio);

    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b JOIN b.anioAcademico an WHERE  an.activoMatricula=true")
    List<Alumno> yaMatriculado();

    List<Alumno> findByNombreAlumnoContainingIgnoreCase(String nombre);

}
