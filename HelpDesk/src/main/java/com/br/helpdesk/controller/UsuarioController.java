package com.br.helpdesk.controller;

import com.br.helpdesk.api.response.Response;
import com.br.helpdesk.entity.Usuario;
import com.br.helpdesk.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Usuario>> create(HttpServletRequest request,
                                                    @RequestBody Usuario usuario,
                                                    BindingResult result){
        Response<Usuario> usuarioResponse = new Response<>();
        try {
            validateCreateUser(usuario, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        usuarioResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(usuarioResponse);
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario usuarioPersist = usuarioService.createOrUpdate(usuario);
            usuarioResponse.setData(usuarioPersist);
        }catch (DuplicateKeyException de){
            usuarioResponse.getErrors().add("Email already registred");
            return ResponseEntity.badRequest().body(usuarioResponse);
        }catch (Exception e){
            usuarioResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(usuarioResponse);
        }

        return ResponseEntity.ok(usuarioResponse);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Usuario>> update(HttpServletRequest request,
                                                    @RequestBody Usuario usuario,
                                                    BindingResult result){
        Response<Usuario> usuarioResponse = new Response<>();
        try {
            validateUpdateUser(usuario, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(objectError ->
                        usuarioResponse.getErrors().add(objectError.getDefaultMessage()));
                return ResponseEntity.badRequest().body(usuarioResponse);
            }
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario usuarioPersist = usuarioService.createOrUpdate(usuario);
            usuarioResponse.setData(usuarioPersist);
        }catch (Exception e){
            usuarioResponse.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(usuarioResponse);
        }

        return ResponseEntity.ok(usuarioResponse);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Usuario>> findById(@PathVariable String id) {
        Response<Usuario> usuarioResponse = new Response<>();
        Optional<Usuario> usuarioFind = usuarioService.findById(id);
        if(usuarioFind == null || usuarioFind.get() == null){
            usuarioResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(usuarioResponse);
        }
        usuarioResponse.setData(usuarioFind.get());
        return ResponseEntity.ok(usuarioResponse);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> delete(@PathVariable String id) {
        Response<String> usuarioResponse = new Response<>();
        Optional<Usuario> usuarioFind = usuarioService.findById(id);
        if(usuarioFind == null || usuarioFind.get() == null){
            usuarioResponse.getErrors().add("Register not found id: "+id);
            return ResponseEntity.badRequest().body(usuarioResponse);
        }
        usuarioService.delete(usuarioFind.get().getId());
        return ResponseEntity.ok(new Response<String>());
    }

    @GetMapping(value = "/{page}/{count}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<Page<Usuario>>> findAll(@PathVariable int page,@PathVariable int count) {
        Response<Page<Usuario>> usuarioResponse = new Response<>();
        Page<Usuario> usuarios = usuarioService.findAll(page, count);
        usuarioResponse.setData(usuarios);
        return ResponseEntity.ok(usuarioResponse);
    }

    private void validateCreateUser(Usuario usuario, BindingResult result){
        if(usuario.getEmail() == null){
            result.addError(new ObjectError("User", "Email no Information"));
        }
    }

    private void validateUpdateUser(Usuario usuario, BindingResult result){
        if(usuario.getId() == null){
            result.addError(new ObjectError("User", "Id no Information"));
        }
        if(usuario.getEmail() == null){
            result.addError(new ObjectError("User", "Email no Information"));
        }
    }



}
