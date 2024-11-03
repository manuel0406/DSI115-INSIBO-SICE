package com.dsi.insibo.sice.Calificaciones;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dsi.insibo.sice.Expediente_alumno.AlumnoService;
import com.dsi.insibo.sice.entity.Alumno;
import com.dsi.insibo.sice.entity.Materia;
import com.dsi.insibo.sice.entity.Nota;
import com.dsi.insibo.sice.entity.NotaPeriodo;
import com.dsi.insibo.sice.entity.Periodo;

@Service
public class NotaPeriodoService {

    @Autowired
    private NotaPeriodoRepository notaPeriodoRepository;
    @Autowired
    private NotaService notaService;
    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private PeriodoService periodoService;

    public void guardarNotaPeriodo(NotaPeriodo notaPeriodo) {
        notaPeriodoRepository.save(notaPeriodo);
    }

    public List<NotaPeriodo> listaNotasPeriodo() {
        return notaPeriodoRepository.findAll();
    }

    public void eliminarNotaPeriodo(int idNotaPeriodo) {
        notaPeriodoRepository.deleteById(idNotaPeriodo);
    }

    public NotaPeriodo notaPeriodoId(int idNotaPeriodo) {

        return notaPeriodoRepository.findById(idNotaPeriodo).orElse(null);
    }

    public NotaPeriodo notaPeriodoAlumno(int idAlumno, String periodo, int IdMateria, int codigoBachillerato,
            String duiDocente) {
        return notaPeriodoRepository.notaPeriodoAlumno(idAlumno, periodo, IdMateria, codigoBachillerato, duiDocente)
                .orElse(null);
    }

    public List<NotaPeriodo> notaPorPeriodoAlumno(int idAlumno, int idMateria, int codigoBachillerato) {
        return notaPeriodoRepository.notaPeriodoAlumnos(idAlumno, idMateria, codigoBachillerato);
    }

    public Optional<NotaPeriodo> obtenerNotaPeriodo(Alumno alumno, int numeroPeriodo, Materia materia) {
        return notaPeriodoRepository.findByAlumnoAndPeriodoNumeroPeriodoAndAsignacionMateria(alumno, numeroPeriodo,
                materia);

    }

    public List<NotaPeriodo> listadoNotasPeriodoMateria(int idMateria, int codigoBachillerato, String duiDocente) {
        return notaPeriodoRepository.notaPeriodoMateria(idMateria, codigoBachillerato, duiDocente);
    }

    public void procesarNotaPeriodo(Nota notaAlumno) {

        List<Nota> listaNotas = notaService.listaNotaActividadBachilleratoAlumno(
                notaAlumno.getActividad().getAsignacion().getDocente().getDuiDocente(),
                notaAlumno.getActividad().getAsignacion().getBachillerato().getCodigoBachillerato(),
                String.valueOf(notaAlumno.getActividad().getPeriodo().getNumeroPeriodo()),
                notaAlumno.getActividad().getAsignacion().getMateria().getIdMateria(),
                notaAlumno.getAlumno().getIdAlumno());

        // Crear un mapa para almacenar el total de notas por alumno
        Map<Integer, Double> totalNotasPorAlumno = new HashMap<>();

        // Calcular el total ponderado de notas por alumno
        for (Nota nota : listaNotas) {
            int idAlumno = nota.getAlumno().getIdAlumno();

            BigDecimal notaObtenida = BigDecimal.valueOf(nota.getNotaObtenida());
            BigDecimal ponderacion = BigDecimal.valueOf(nota.getActividad().getPonderacionActividad())
                    .divide(BigDecimal.valueOf(100));
            BigDecimal notaPonderada = notaObtenida.multiply(ponderacion).setScale(2, RoundingMode.HALF_UP);

            // Sumar al total de notas ponderadas por alumno
            totalNotasPorAlumno.merge(idAlumno, notaPonderada.doubleValue(), Double::sum);
        }

        // Actualizar o crear NotaPeriodo para cada alumno
        for (Map.Entry<Integer, Double> entry : totalNotasPorAlumno.entrySet()) {
            int idAlumno = entry.getKey();
            float notaFinalPeriodo = entry.getValue().floatValue();

            NotaPeriodo notaPeriodo = notaPeriodoAlumno(idAlumno,
                    String.valueOf(notaAlumno.getActividad().getPeriodo().getNumeroPeriodo()),
                    notaAlumno.getActividad().getAsignacion().getMateria().getIdMateria(),
                    notaAlumno.getActividad().getAsignacion().getBachillerato().getCodigoBachillerato(),
                    notaAlumno.getActividad().getAsignacion().getDocente().getDuiDocente());

            if (notaPeriodo != null) {
                notaPeriodo.setNotaPeriodo(notaFinalPeriodo);
            } else {
                notaPeriodo = new NotaPeriodo();
                Alumno alumno = alumnoService.buscarPorIdAlumno(idAlumno);
                Periodo periodo = periodoService
                        .periodoPorId(notaAlumno.getActividad().getPeriodo().getNumeroPeriodo());

                notaPeriodo.setAlumno(alumno);
                notaPeriodo.setAsignacion(notaAlumno.getActividad().getAsignacion());
                notaPeriodo.setPeriodo(periodo);
                notaPeriodo.setNotaPeriodo(notaFinalPeriodo);
            }
            // Guardar NotaPeriodo
            guardarNotaPeriodo(notaPeriodo);
        }
    }

}
