package com.dsi.insibo.sice.Calificaciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.Nota;
import jakarta.transaction.Transactional;

@Service
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Transactional
    public void deleteNotasByAlumnoNie(int nie) {
        notaRepository.deleteByAlumnoNie(nie);
    }

    @Transactional
    public void deleteNotaActividad(int idActividad) {
        notaRepository.deleteByActividad(idActividad);

    }

    public void guardarNota(Nota nota) {
        notaRepository.save(nota);
    }

    public List<Nota> notasPorBachilleratoActivdad(int idActividad) {
        return notaRepository.notasActividad(idActividad);
    }

    public List<Nota> listaNotaActividadBachillerato(String dui, int codigoBachillerato) {
        return notaRepository.findAll(dui, codigoBachillerato);
    }

    public List<Nota> listaNotaActividadBachillerato(String dui, int codigoBachillerato, String periodo,int idMateria) {
        return notaRepository.notasPeriodo(dui, codigoBachillerato, periodo, idMateria);
    }

    public List<Nota> notasPorAlumno(Alumno alumno) {
        return notaRepository.findNotasByAlumnoGroupedByMateriaAndPeriodo(alumno);
    }

    public Map<Materia, Map<Integer, Map<String, List<Nota>>>> notasAgrupadas(Alumno alumno) {
        List<Nota> listaNotas = notaRepository.findNotasByAlumnoGroupedByMateriaAndPeriodo(alumno);

        // Agrupación: Materia -> Periodo -> TipoActividad -> Lista de Notas
        return listaNotas.stream().collect(Collectors.groupingBy(
                nota -> nota.getActividad().getAsignacion().getMateria(), // Agrupa por materia
                Collectors.groupingBy(
                        nota -> nota.getActividad().getPeriodo().getNumeroPeriodo(), // Luego por periodo
                        Collectors.groupingBy(
                                nota -> nota.getActividad().getTipoActividad() // Luego por tipo de actividad
                        ))));
    }

    public Map<Materia, Map<Integer, Double>> calcularPromediosGlobales(Alumno alumno) {
        List<Nota> listaNotas = notaRepository.findNotasByAlumnoGroupedByMateriaAndPeriodo(alumno);

        // Agrupar por Materia y luego por Periodo
        Map<Materia, Map<Integer, List<Nota>>> notasAgrupadas = listaNotas.stream().collect(Collectors.groupingBy(
                nota -> nota.getActividad().getAsignacion().getMateria(),
                Collectors.groupingBy(
                        nota -> nota.getActividad().getPeriodo().getNumeroPeriodo())));

        // Calcular promedios globales
        Map<Materia, Map<Integer, Double>> promediosGlobales = new HashMap<>();

        for (Map.Entry<Materia, Map<Integer, List<Nota>>> materiaEntry : notasAgrupadas.entrySet()) {
            Map<Integer, List<Nota>> periodos = materiaEntry.getValue();

            for (Map.Entry<Integer, List<Nota>> periodoEntry : periodos.entrySet()) {
                int numeroPeriodo = periodoEntry.getKey();
                List<Nota> notasDelPeriodo = periodoEntry.getValue();

                // Calcular el promedio ponderado
                double sumaPonderada = notasDelPeriodo.stream()
                        .mapToDouble(nota -> nota.getNotaObtenida()
                                * (nota.getActividad().getPonderacionActividad() / 100.0))
                        .sum();

                double sumaPonderaciones = notasDelPeriodo.stream()
                        .mapToDouble(nota -> nota.getActividad().getPonderacionActividad() / 100.0)
                        .sum();

                // Calcular el promedio global ponderado, evitando división por cero
                double promedioGlobal = sumaPonderaciones > 0 ? sumaPonderada / sumaPonderaciones : 0.0;

                // Guardar el promedio global
                promediosGlobales.computeIfAbsent(materiaEntry.getKey(), k -> new HashMap<>())
                        .put(numeroPeriodo, promedioGlobal);
            }
        }

        return promediosGlobales;
    }

    public List<Nota> listaNotaActividadBachilleratoAlumno(String dui, int codigoBachillerato, String periodo,int idMateria, int idAlumno) {
        return notaRepository.notasPeriodoAlumno(dui, codigoBachillerato, periodo, idMateria, idAlumno);
    }

}
