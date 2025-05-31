package com.tuempresa.aplicacion.service;

import com.tuempresa.aplicacion.entity.Categoria;
import com.tuempresa.aplicacion.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejar la lógica de negocio relacionada con Categoría.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;

    public CategoriaService(CategoriaRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepo.findByNombre(nombre);
    }

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepo.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(Long id, Categoria datosActualizados) {
        Categoria existente = categoriaRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id " + id));
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        return categoriaRepo.save(existente);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepo.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con id " + id);
        }
        categoriaRepo.deleteById(id);
    }
}
