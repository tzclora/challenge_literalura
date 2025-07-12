package com.bubches.literalura.Repository;

import com.bubches.literalura.Model.Autor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreIgnoreCase(String nombre);
}
