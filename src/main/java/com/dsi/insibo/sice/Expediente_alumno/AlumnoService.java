package com.dsi.insibo.sice.Expediente_alumno;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.dsi.insibo.sice.Seguridad.gestionarCredencialesController;
import com.dsi.insibo.sice.entity.Alumno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public List<Alumno> listarAlumnos(String carrera, String grado, String seccion, String genero) {

        if (carrera != null && grado != null && seccion != null && genero != null) {// 8
            return (List<Alumno>) alumnoRepository.findAll(carrera, grado, seccion, genero);

        } else if (carrera != null && grado != null && seccion != null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllCarreraGradoSeccion(carrera, grado, seccion);

        } else if (carrera != null && grado == null && seccion == null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllPorCarrera(carrera);

        } else if (carrera != null && grado == null && seccion == null && genero != null) {// 1
            return (List<Alumno>) alumnoRepository.findAllCarreraGenero(carrera, genero);

        } else if (carrera == null && grado != null && seccion == null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllPorGrado(grado);

        } else if (carrera == null && grado != null && seccion == null && genero != null) {// 2
            return (List<Alumno>) alumnoRepository.findAllGradoGenero(grado, genero);

        } else if (carrera == null && grado == null && seccion != null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllPorSeccion(seccion);

        } else if (carrera == null && grado == null && seccion != null && genero != null) {// 3
            return (List<Alumno>) alumnoRepository.findAllSeccionGenero(seccion, genero);

        } else if (carrera != null && grado != null && seccion == null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllCarreraGrado(carrera, grado);

        } else if (carrera != null && grado != null && seccion == null && genero != null) {// 4
            return (List<Alumno>) alumnoRepository.findAllCarreraGradoGenero(carrera, grado, genero);

        } else if (carrera != null && grado == null && seccion != null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllCarreraSeccion(carrera, seccion);

        } else if (carrera != null && grado == null && seccion != null && genero != null) {// 5
            return (List<Alumno>) alumnoRepository.findAllCarreraSeccionGenero(carrera, seccion, genero);

        } else if (carrera == null && grado != null && seccion != null && genero == null) {
            return (List<Alumno>) alumnoRepository.findAllGradoSeccion(grado, seccion);
        } else if (carrera == null && grado != null && seccion != null && genero != null) {// 6
            return (List<Alumno>) alumnoRepository.findAllGradoSeccionGenero(grado, seccion, genero);

        } else if (carrera == null && grado == null && seccion == null && genero != null) {// 7
            return (List<Alumno>) alumnoRepository.findAllGenero(genero);
        }
        // Si todos son nulos, devuelve todos los alumnos
        return alumnoRepository.findAll();
    }

    /**
     * Método que devuelve una página de alumnos filtrados y paginados según los
     * parámetros proporcionados.
     * 
     * Filtra la lista de alumnos en base a los parámetros opcionales de carrera,
     * grado y sección,
     * utilizando el repositorio de alumnos correspondiente. Si alguno de los
     * parámetros es nulo,
     * se omite ese filtro específico.
     * 
     * @param carrera  El parámetro opcional de carrera del alumno.
     *                 Si es nulo, se omite el filtro por carrera.
     * @param grado    El parámetro opcional de grado del alumno.
     *                 Si es nulo, se omite el filtro por grado.
     * @param seccion  El parámetro opcional de sección del alumno.
     *                 Si es nulo, se omite el filtro por sección.
     * @param pageable La información de paginación que incluye el número de página
     *                 y tamaño de página.
     * @return Una página paginada de objetos Alumno que cumplen con los criterios
     *         de filtro especificados.
     *         Si todos los parámetros son nulos, devuelve todos los alumnos
     *         paginados.
     */
    public Page<Alumno> listarAlumnosPaginados(String carrera, String grado, String seccion, Pageable pageable) {
        if (carrera != null && grado != null && seccion != null) {
            return alumnoRepository.findAll(carrera, grado, seccion, pageable);
        } else if (carrera != null && grado == null && seccion == null) {
            return alumnoRepository.findAllPorCarrera(carrera, pageable);
        } else if (carrera == null && grado != null && seccion == null) {
            return alumnoRepository.findAllPorGrado(grado, pageable);
        } else if (carrera == null && grado == null && seccion != null) {
            return alumnoRepository.findAllPorSeccion(seccion, pageable);
        } else if (carrera != null && grado != null && seccion == null) {
            return alumnoRepository.findAllCarreraGrado(carrera, grado, pageable);
        } else if (carrera != null && grado == null && seccion != null) {
            return alumnoRepository.findAllCarreraSeccion(carrera, seccion, pageable);
        } else if (carrera == null && grado != null && seccion != null) {
            return alumnoRepository.findAllGradoSeccion(grado, seccion, pageable);
        }
        // Si todos son nulos, devuelve todos los alumnos paginados
        return alumnoRepository.findAll(pageable);
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
    public List<Alumno> alumnosPorBachilerato(int codigoBachillerato) {
        return alumnoRepository.findByBachilleratoCodigoBachillerato(codigoBachillerato);
    }

    public List<Alumno> matricula(String nie, String nombre, String apellido, int anio){
        return alumnoRepository.listadoMatricula(nie, nombre, apellido, anio);
    }

    public List<Alumno> yaMatriculado(){
        return alumnoRepository.yaMatriculado();
    }

    public List<Alumno> buscarPorNombre(String nombre) {
        return alumnoRepository.findByNombreAlumnoContainingIgnoreCase(nombre);
    }
}
