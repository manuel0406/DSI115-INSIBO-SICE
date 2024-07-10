package com.dsi.insibo.sice.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;
    @Column(unique = true)
    private String correoUsuario;
    private String contrasenaUsuario;
    private boolean primerIngreso;
    private boolean enabled;                // Si esta activo
    private boolean accountNoExpired;       // Si ha expirado
    private boolean accountLocked;          // Si esta bloqueado
    private boolean credentialNoExpired;    // Si credenciales no han expirado

    @OneToOne
    private Docente docente;
    @OneToOne
    private PersonalAdministrativo personalAdministrativo;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="usuario_rol", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<UsuarioRoles> rolesUsuario = new HashSet<>();
    
    public Docente getDocente() {
        return docente;
    }
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getCorreoUsuario() {
        return correoUsuario;
    }
    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }
    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }
    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }
    public boolean isPrimerIngreso() {
        return primerIngreso;
    }
    public void setPrimerIngreso(boolean primerIngreso) {
        this.primerIngreso = primerIngreso;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public PersonalAdministrativo getPersonalAdministrativo() {
        return personalAdministrativo;
    }
    public void setPersonalAdministrativo(PersonalAdministrativo personalAdministrativo) {
        this.personalAdministrativo = personalAdministrativo;
    }
    // Getter for accountNoExpired
    public boolean isAccountNoExpired() {
        return accountNoExpired;
    }
    // Setter for accountNoExpired
    public void setAccountNoExpired(boolean accountNoExpired) {
        this.accountNoExpired = accountNoExpired;
    }
    // Getter for accountLocked
    public boolean isAccountLocked() {
        return accountLocked;
    }
    // Setter for accountLocked
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }
    // Getter for credentialNoExpired
    public boolean isCredentialNoExpired() {
        return credentialNoExpired;
    }
    // Setter for credentialNoExpired
    public void setCredentialNoExpired(boolean credentialNoExpired) {
        this.credentialNoExpired = credentialNoExpired;
    }
    // Setter para rolesUsuario
    public void setRolesUsuario(Set<UsuarioRoles> rolesUsuario) {
        this.rolesUsuario = rolesUsuario;
    }
    // Método para obtener todos los nombres de los roles como una cadena
    public String getRolesUsuarioNombres() {
        StringBuilder roles = new StringBuilder();
        for (UsuarioRoles rol : rolesUsuario) {
            roles.append(rol.getRoleEnum().name()).append(", ");
        }
        // Eliminar la última coma y espacio si hay roles
        if (roles.length() > 0) {
            roles.setLength(roles.length() - 2);
        }
        return roles.toString();
    }
    
}
