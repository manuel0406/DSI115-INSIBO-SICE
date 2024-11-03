package com.dsi.insibo.sice.Calificaciones;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Asignacion;
import com.dsi.insibo.sice.entity.NotaMateria;
import com.dsi.insibo.sice.entity.NotaPeriodo;

@Service
public class NotaMateriaService {

    @Autowired
    private NotaMateriaRepository notaMateriaRepository;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private NotaPeriodoService notaPeriodoService;

    public void guardarNotaMateria(NotaMateria notaMateria) {
        notaMateriaRepository.save(notaMateria);
    }

    public List<NotaMateria> listadoNotaMateria(){
        return notaMateriaRepository.findAll();
    }

    public NotaMateria notaMateriaId(int idNotaMateria){
        return notaMateriaRepository.findById(idNotaMateria).orElse(null);
    }

    public void eliminarNotaMateria(int idNotaMateria){
        notaMateriaRepository.deleteById(idNotaMateria);
    }

    public NotaMateria existeNotaMateria(int idAlumno, int idMateria, String duiDocente, int codigoBachillerato){
        return notaMateriaRepository.notaMateriaAlumno(idAlumno, idMateria, duiDocente, codigoBachillerato).orElse(null);
    }

    public void procesarNotaMateria(Asignacion asignacion, String dui, String pe, int codigoBachillerato){

        NotaMateria notaMateria =null;
        
		List<NotaPeriodo> notaPeriodos = notaPeriodoService.listadoNotasPeriodoMateria(asignacion.getMateria().getIdMateria(), codigoBachillerato,
				dui);

		// Obtener lista de alumnos y ordenar por apellido en orden ascendente
		List<Alumno> listaAlumnos = alumnoService.alumnosPorBachilerato(asignacion.getBachillerato().getCodigoBachillerato());
		listaAlumnos.sort(Comparator.comparing(Alumno::getApellidoAlumno));

		// Usar LinkedHashMap para preservar el orden en listaAlumnos
		Map<Alumno, Map<Integer, Float>> notasPorAlumnoYPeriodo = new LinkedHashMap<>();
		Map<Alumno, Float> notaGlobalPorAlumno = new HashMap<>();

		boolean esModulo = "Módulo".equals(asignacion.getMateria().getTipoMateria());

		// Organizar las notas por alumno y periodo en función de listaAlumnos
		for (Alumno alumno : listaAlumnos) {
			Map<Integer, Float> notasPeriodo = new HashMap<>();
			for (NotaPeriodo nota : notaPeriodos) {
				if (nota.getAlumno().equals(alumno)) {
					int periodo = nota.getPeriodo().getIdPeriodo();
					notasPeriodo.put(periodo, nota.getNotaPeriodo());
				}
			}
			notasPorAlumnoYPeriodo.put(alumno, notasPeriodo);
		}
		// Calcular la nota global para cada alumno
		for (Alumno alumno : listaAlumnos) {
			Map<Integer, Float> notasPeriodo = notasPorAlumnoYPeriodo.getOrDefault(alumno, new HashMap<>());

			float notaGlobal = 0;
			if (esModulo) {
				// Si es "Módulo", solo considerar la nota del periodo 1
				notaGlobal = notasPeriodo.getOrDefault(1, 0.0f);
			} else {
				// Si no es "Módulo", calcular la nota global considerando todos los periodos
				for (int i = 1; i <= 4; i++) {
					float nota = notasPeriodo.getOrDefault(i, 0.0f);
					notaGlobal += nota * 0.25;
				}
			}
			notaGlobalPorAlumno.put(alumno, notaGlobal);

			// notaPeriodo = notaPeriodoService.notaPeriodoAlumno(idAlumno, pe, idMateria,
			// codigoBachillerato, dui);
			notaMateria = existeNotaMateria(alumno.getIdAlumno(), asignacion.getMateria().getIdMateria(), dui,
					codigoBachillerato);
			if (notaMateria != null) {
				// System.out.println("si existe");
				// System.out.println("nota: "+ notaGlobal);
				notaMateria.setNotaMateria(notaGlobal);
				guardarNotaMateria(notaMateria);
			} else {
				notaMateria = new NotaMateria();
				notaMateria.setAlumno(alumno);
				notaMateria.setAsignacion(asignacion);
				notaMateria.setNotaMateria(notaGlobal);
				// System.out.println("nota: "+ notaGlobal);
				// System.out.println("neles");
				guardarNotaMateria(notaMateria);
			}
		}
    }

	public List<NotaMateria> listadoNotaMateriaAlumno(int idAlumno){
		return notaMateriaRepository.notaMateriaPorAlumno(idAlumno);
	}
    
}
