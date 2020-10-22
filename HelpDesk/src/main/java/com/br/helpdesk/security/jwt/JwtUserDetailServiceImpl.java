package com.br.helpdesk.security.jwt;

import com.br.helpdesk.entity.Usuario;
import com.br.helpdesk.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService {
   private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByEmail(email);
        if(usuario == null){
            throw new UsernameNotFoundException(String.format("No user found "));
        }else{
            return JwtUserFactory.create(usuario);
        }
    }
}
