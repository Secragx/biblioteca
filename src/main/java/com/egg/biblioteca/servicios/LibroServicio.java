package com.egg.biblioteca.servicios;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;



@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional(readOnly = true)
    public Libro getOne(Long isbn) {
        return libroRepositorio.getReferenceById(isbn);
    }

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException{
        
        validar(isbn, titulo, ejemplares, titulo, idEditorial); 

        Autor autor = autorRepositorio.findById(idAutor).get();

        Editorial editorial = editorialRepositorio.findById(idEditorial).get();

        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        libro.setAlta(new Date());

        libro.setAutor(autor);
        libro.setEditorial(editorial);

        libroRepositorio.save(libro);
    }

    public List<Libro> listarLibros(){
        List<Libro> libros = new ArrayList<>();
        libros = libroRepositorio.findAll();
        return libros;
    }

    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException{
        
        validar(isbn, titulo, ejemplares, titulo, idEditorial);

        Optional <Libro> respuestaLibro = libroRepositorio.findById(isbn);
        Optional <Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional <Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);

        Autor autor = new Autor();

        Editorial editorial = new Editorial(); 

        if(respuestaAutor.isPresent()){
            autor = respuestaAutor.get();
        }

        if(respuestaEditorial.isPresent()){
            editorial = respuestaEditorial.get();
        }

        if(respuestaLibro.isPresent()){
        Libro libro = respuestaLibro.get();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setEjemplares(ejemplares);
        libroRepositorio.save(libro);
        }
    }

    private void validar(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException{
if(isbn==null){
            throw new MiException ("EL isbn NO PUEDE SER NULO");
        }

        if(titulo.isEmpty() || titulo==null){
            throw new MiException ("EL titulo NO PUEDE SER NULO");
        }

        if(ejemplares==null){
            throw new MiException ("Los ejemplares PUEDE SER NULO");
        } 

        if(idAutor==null){
            throw new MiException ("EL IdAutor NO PUEDE SER NULO");
        } 
        
        if(idAutor.isEmpty() || titulo==null){
            throw new MiException ("EL idEditorial NO PUEDE SER NULO");
        } 
    }

}

