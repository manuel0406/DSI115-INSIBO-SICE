package com.dsi.insibo.sice.Seguridad.Configuraciones;

import com.dsi.insibo.sice.entity.Usuario;

import jakarta.transaction.Transactional;

import com.dsi.insibo.sice.Seguridad.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Petición a BD
        Usuario usuario = usuarioRepository.findByCorreoUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));
                
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        usuario.getRolesUsuario()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        usuario.getRolesUsuario().stream()
                .flatMap(role -> role.getUsuarioPermiso().stream())
                .forEach(permiso -> authorityList.add(new SimpleGrantedAuthority(permiso.getTipo())));

        return new User(usuario.getCorreoUsuario(),
                        usuario.getContrasenaUsuario(),
                        usuario.isEnabled(), // enabled
                        usuario.isAccountNoExpired(), // accountNonExpired
                        usuario.isCredentialNoExpired(), // credentialsNonExpired
                        usuario.isAccountLocked(), // accountNonLocked
                        authorityList);
    }
}

