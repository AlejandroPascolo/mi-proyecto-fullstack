package com.tuempresa.aplicacion.service;

import com.tuempresa.aplicacion.entity.Categoria;
import com.tuempresa.aplicacion.entity.Producto;
import com.tuempresa.aplicacion.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio para la entidad Producto.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepo, CategoriaService categoriaService) {
        this.productoRepo = productoRepo;
        this.categoriaService = categoriaService;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepo.findByNombreContenidoIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return productoRepo.findByPrecioBetween(min, max);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarConFiltros(String nombre, BigDecimal minPrecio, BigDecimal maxPrecio, Long categoriaId) {
        if (nombre == null && minPrecio == null && maxPrecio == null && categoriaId == null) {
            return productoRepo.findAll();
        }

        if (nombre != null && minPrecio == null && maxPrecio == null && categoriaId == null) {
            return productoRepo.findByNombreContenidoIgnoreCase(nombre);
        }

        if (nombre == null && minPrecio != null && maxPrecio != null && categoriaId == null) {
            return productoRepo.findByPrecioBetween(minPrecio, maxPrecio);
        }

        if (nombre == null && minPrecio == null && maxPrecio == null && categoriaId != null) {
            return productoRepo.findByCategoriaId(categoriaId);
        }

        List<Producto> listaBase = productoRepo.findAll();
        List<Producto> filtrados = new ArrayList<>();

        for (Producto p : listaBase) {
            boolean cumple = true;

            if (nombre != null && !p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                cumple = false;
            }

            if (cumple && minPrecio != null && maxPrecio != null) {
                if (p.getPrecio().compareTo(minPrecio) < 0 || p.getPrecio().compareTo(maxPrecio) > 0) {
                    cumple = false;
                }
            }

            if (cumple && categoriaId != null) {
                if (!p.getCategoria().getId().equals(categoriaId)) {
                    cumple = false;
                }
            }

            if (cumple) {
                filtrados.add(p);
            }
        }

        return filtrados;
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        Long categoriaId = producto.getCategoria().getId();
        Categoria categoria = categoriaService.buscarPorId(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id " + categoriaId));
        producto.setCategoria(categoria);
        return productoRepo.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto datosActualizados) {
        Producto existente = productoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + id));

        existente.setNombre(datosActualizados.getNombre());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());

        Long nuevaCategoriaId = datosActualizados.getCategoria().getId();
        if (!existente.getCategoria().getId().equals(nuevaCategoriaId)) {
            Categoria nuevaCategoria = categoriaService.buscarPorId(nuevaCategoriaId)
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id " + nuevaCategoriaId));
            existente.setCategoria(nuevaCategoria);
        }

        return productoRepo.save(existente);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepo.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id " + id);
        }
        productoRepo.deleteById(id);
    }
}
