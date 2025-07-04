package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;

@Service

public class UsuarioServicio implements UserDetailsService{
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional 
    public void registrar(String nombre, String email, String password, String password2) throws MiException{
        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));


        usuario.setRol(Rol.USER);

        usuarioRepositorio.save(usuario);
          
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException{
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacio");
        }

        if (password.isEmpty() || password == null || password.length() <=5) {
            throw new MiException("La contraseña no puede estar vacia y debe tener mas de 5 caracteres");
        }

        if (!password.equals(password2)) {
            throw new MiException("las contraseñas deben ser iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

       if (usuario !=null){

        List <GrantedAuthority> permisos = new ArrayList();

        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());
        permisos.add(p);

         
        return new User(usuario.getEmail(), usuario.getPassword(),permisos);
       } else {
        return null; 
       }
       
    }

}
    