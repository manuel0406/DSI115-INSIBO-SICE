package com.dsi.insibo.sice.entity;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "roles")
public class UsuarioRoles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private UsuarioRoleEnum roleEnum;

    @ManyToAny
    @JoinTable(name="rol_permiso", joinColumns = @JoinColumn(name = "id_rol"), inverseJoinColumns = @JoinColumn(name = "id_permiso"))
    private Set<UsuarioPermiso> usuarioPermiso = new HashSet<>();

        // Getter para idRol
        public Long getIdRol() {
            return idRol;
        }
    
        // Setter para idRol
        public void setIdRol(Long idRol) {
            this.idRol = idRol;
        }
    
        // Getter para roleEnum
        public UsuarioRoleEnum getRoleEnum() {
            return roleEnum;
        }
    
        // Setter para roleEnum
        public void setRoleEnum(UsuarioRoleEnum roleEnum) {
            this.roleEnum = roleEnum;
        }
    
        // Getter para usuarioPermiso
        public Set<UsuarioPermiso> getUsuarioPermiso() {
            return usuarioPermiso;
        }
    
        // Setter para usuarioPermiso
        public void setUsuarioPermiso(Set<UsuarioPermiso> usuarioPermiso) {
            this.usuarioPermiso = usuarioPermiso;
        }

}
