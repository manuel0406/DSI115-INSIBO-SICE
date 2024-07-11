package com.dsi.insibo.sice.Seguridad.ClasesDeSeguridad;

import com.dsi.insibo.sice.entity.Usuario;

public class UsuarioConNombre {
    private Usuario usuario;
    private String nombre;

    public UsuarioConNombre(Usuario usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
