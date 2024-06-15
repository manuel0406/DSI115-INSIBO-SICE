package com.dsi.insibo.sice.Calificaciones;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dsi.insibo.sice.entity.Bachillerato;
@Repository
public interface GradoRepository extends JpaRepository<Bachillerato, String> {

}
