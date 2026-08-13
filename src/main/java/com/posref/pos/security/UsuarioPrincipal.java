package com.posref.pos.security;

import com.posref.pos.model.seguridad.Permiso;
import com.posref.pos.model.seguridad.Rol;
import com.posref.pos.model.seguridad.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UsuarioPrincipal implements UserDetails {

    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Rol rol : usuario.getRoles()) {

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + rol.getNombre()
                    )
            );

            for (Permiso permiso : rol.getPermisos()) {
                authorities.add(
                        new SimpleGrantedAuthority(
                                permiso.getNombre()
                        )
                );
            }
        }

        return authorities;
    }

    public Long getId() {
        return usuario.getId();
    }

    public String getNombre() {
        return usuario.getNombre();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(usuario.getActivo());
    }
}
