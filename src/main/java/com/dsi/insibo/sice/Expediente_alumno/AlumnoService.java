package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dsi.insibo.sice.entity.Alumno;

/**
 * Servicio que proporciona métodos para interactuar con la entidad Alumno.
 * Utiliza el repositorio AlumnoRepository para acceder a los datos.
 */
@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    /**
     * Devuelve una lista de alumnos según los parámetros proporcionados.
     * Si carrera, grado o sección son no nulos, filtra los alumnos según esos
     * criterios.
     * Si alguno de los parámetros es nulo, filtra solo por los parámetros no nulos.
     * Si todos los parámetros son nulos, devuelve todos los alumnos.
     *
     * carrera Nombre de la carrera.
     * grado Grado del bachillerato.
     * seccion Sección del bachillerato.
     * return Lista de alumnos que cumplen con los criterios de búsqueda.
     */
    public List<Alumno> listarAlumnos(String carrera, String grado, String seccion) {

        if (carrera != null && grado != null && seccion != null) {
            return (List<Alumno>) alumnoRepository.findAll(carrera, grado, seccion);

        } else if (carrera != null && grado == null && seccion == null) {
            return (List<Alumno>) alumnoRepository.findAllPorCarrera(carrera);

        } else if (carrera == null && grado != null && seccion == null) {
            return (List<Alumno>) alumnoRepository.findAllPorGrado(grado);

        } else if (carrera == null && grado == null && seccion != null) {
            return (List<Alumno>) alumnoRepository.findAllPorSeccion(seccion);

        } else if (carrera != null && grado != null && seccion == null) {
            return (List<Alumno>) alumnoRepository.findAllCarreraGrado(carrera, grado);

        } else if (carrera != null && grado == null && seccion != null) {
            return (List<Alumno>) alumnoRepository.findAllCarreraSeccion(carrera, seccion);

        } else if (carrera == null && grado != null && seccion != null) {
            return (List<Alumno>) alumnoRepository.findAllGradoSeccion(grado, seccion);
        }
        // Si todos son nulos, devuelve todos los alumnos
        return alumnoRepository.findAll();
    }

    /**
     * Guarda un nuevo alumno en la base de datos.
     *
     * alumno Alumno a guardar.
     */
    public void guardarAlumno(Alumno alumno) {
        alumnoRepository.save(alumno);
    }

    /**
     * Busca un alumno por su ID.
     *
     * id ID del alumno a buscar.
     * return Alumno encontrado, o null si no existe.
     */
    public Alumno buscarPorIdAlumno(int id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    /**
     * Elimina un alumno por su número de identificación estudiantil (NIE).
     *
     * @param nie Número de identificación estudiantil del alumno a eliminar.
     */
    public void eliminar(int nie) {
        alumnoRepository.deleteById(nie);
    }

    /**
     * Busca todos los alumnos que pertenecen a un bachillerato específico por su
     * código.
     *
     * @param codigoBachillerato Código del bachillerato.
     * @return Lista de alumnos que pertenecen al bachillerato especificado.
     */
    public List<Alumno> findAlumnosByBachilleratoCodigoBachillerato(String codigoBachillerato) {
        return alumnoRepository.findByBachilleratoCodigoBachillerato(codigoBachillerato);
    }

}
