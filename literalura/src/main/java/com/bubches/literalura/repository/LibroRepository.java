package com.bubches.literalura.Repository;

import com.bubches.literalura.Model.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTituloIgnoreCase(String titulo);
    Optional<Libro> findByTituloIgnoreCase(String titulo);
    List<Libro> findByIdiomaIgnoreCase(String idioma);

    // Sobrescribimos findAll para incluir autores
    @Override
    @EntityGraph(attributePaths = "autores")
    List<Libro> findAll();
    @Query("SELECT DISTINCT l.idioma FROM Libro l")
    List<String> findDistinctIdiomas();
    @EntityGraph(attributePaths = "autores")
    List<Libro> findAllByTituloIgnoreCase(String titulo);

}
