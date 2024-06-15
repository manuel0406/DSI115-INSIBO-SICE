package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.dsi.insibo.sice.entity.Alumno;


public interface  AlumnoRepository extends JpaRepository<Alumno, Integer> {
    
    @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado AND b.seccion = :seccion")
   public List<Alumno> findAll(String carrera, String grado, String seccion );

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.grado = :grado")
   public List<Alumno> findAllCarreraGrado(String carrera, String grado);

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera AND b.seccion = :seccion")
   public List<Alumno> findAllCarreraSeccion(String carrera, String seccion);

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado AND b.seccion = :seccion")
   public List<Alumno> findAllGradoSeccion(String grado, String seccion);

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.nombreCarrera = :carrera")
   public List<Alumno> findAllPorCarrera(String carrera);   

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.grado = :grado")
   public List<Alumno> findAllPorGrado(String grado);

   @Query("SELECT a FROM Alumno a JOIN a.bachillerato b WHERE b.seccion = :seccion")
   public List<Alumno> findAllPorSeccion(String seccion);

   List<Alumno> findByBachilleratoCodigoBachillerato(String codigoBachillerato);

}
