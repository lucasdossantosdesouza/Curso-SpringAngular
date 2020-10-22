package com.br.helpdesk;

import com.br.helpdesk.entity.ProfileEnum;
import com.br.helpdesk.entity.Usuario;
import com.br.helpdesk.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HelpdeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApplication.class, args);
    }
    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        return args -> {
            initUsers(usuarioRepository, passwordEncoder);
        };
    }
    public void initUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        Usuario usuario = new Usuario();
        usuario.setEmail("lucasdossantosdesouza@gmail.com");
        usuario.setPassword(passwordEncoder.encode("123456"));
        usuario.setProfile(ProfileEnum.ROLE_ADMIN);

        Usuario usuario1 = usuarioRepository.findByEmail(usuario.getEmail());
        if(usuario1 == null){
            usuarioRepository.save(usuario);
        }
    }

}
