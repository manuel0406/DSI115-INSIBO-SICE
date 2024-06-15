package com.dsi.insibo.sice.Calificaciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.dsi.insibo.sice.entity.MateriaBachillerato;
import com.dsi.insibo.sice.entity.MateriaBachilleratoKey;

@Repository
public interface MateriaBachilleratoRepository extends JpaRepository<MateriaBachillerato, MateriaBachilleratoKey> {
    List<MateriaBachillerato> findByBachilleratoCodigoBachillerato(String codigoBachillerato);
}
