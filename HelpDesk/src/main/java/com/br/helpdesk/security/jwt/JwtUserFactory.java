package com.br.helpdesk.security.jwt;

import com.br.helpdesk.entity.ProfileEnum;
import com.br.helpdesk.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Usuario usuario) {
        return new JwtUser(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getPassword(),
                mapToGrantedAutorities(usuario.getProfile())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAutorities(ProfileEnum profile) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(profile.toString()));
        return authorities;
    }
}
