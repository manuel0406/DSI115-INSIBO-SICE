package com.dsi.insibo.sice.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permiso")
public class UsuarioPermiso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPermiso;
    @Column(nullable = false)
    private String tipo;
}
