package com.tuempresa.aplicacion.repository;

import com.tuempresa.aplicacion.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Categoria.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
}
