package com.dsi.insibo.sice.Asistencia_personal.Aparato_Docente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import com.dsi.insibo.sice.entity.DocenteAparato;
import java.util.List;

public interface AparatoDocenteRepository extends CrudRepository<DocenteAparato, Integer> {
  DocenteAparato findByNumeroAparatoDocente(int numeroAparatoDocente);

  // Consulta de Identificador de Aparato Docente
  //@Query("SELECT m FROM Materia m WHERE m.tipoMateria = :tipo ORDER BY m.codMateria ASC")

  @Query("SELECT numeroAparatoDocente FROM DocenteAparato")
  List <Integer> obtenerIDAparatoDocente(); // Todos en formato listado
  
  Page<DocenteAparato> findAll(Pageable pageable); // Todos en formato paginado
}
