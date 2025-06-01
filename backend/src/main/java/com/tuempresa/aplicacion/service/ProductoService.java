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

/**
 * Servicio para manejar la lógica de negocio de Productos.
 * <p>
 * Contiene métodos para:
 * - Obtener todos los productos
 * - Obtener un producto por ID
 * - Crear, actualizar y eliminar productos
 * - Buscar productos aplicando filtros por nombre, rango de precio y categoría
 * </p>
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaService categoriaService;

    public ProductoService(final ProductoRepository productoRepo,
                           final CategoriaService categoriaService) {
        this.productoRepo = productoRepo;
        this.categoriaService = categoriaService;
    }

    /**
     * Recupera todos los productos de la base de datos.
     *
     * @return Lista de objetos Producto
     */
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepo.findAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id Identificador único del producto
     * @return El objeto Producto si existe
     * @throws EntityNotFoundException si no se encuentra el producto
     */
    @Transactional(readOnly = true)
    public Producto obtenerPorId(final Long id) {
        return productoRepo.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado con id " + id)
            );
    }

    /**
     * Busca productos cuyo nombre contenga la cadena dada, ignorando mayúsculas/minúsculas.
     *
     * @param nombre Cadena parcial a buscar en el campo 'nombre'
     * @return Lista de productos que coinciden
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(final String nombre) {
        return productoRepo.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca productos cuyo precio esté entre min y max (ambos inclusive).
     *
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de productos dentro del rango de precios
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(final BigDecimal min, final BigDecimal max) {
        return productoRepo.findByPrecioBetween(min, max);
    }

    /**
     * Busca productos que pertenecen a una categoría específica.
     *
     * @param categoriaId ID de la categoría
     * @return Lista de productos en la categoría indicada
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(final Long categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId);
    }

    /**
     * Busca productos aplicando filtros opcionales de nombre, rango de precio y categoría.
     * <p>
     * Si todos los parámetros son null, devuelve todos los productos.
     * Si solo nombre está presente, filtra por nombre.
     * Si solo rango de precio está presente, filtra por precioBetween.
     * Si solo categoría está presente, filtra por categoríaId.
     * Si hay múltiples filtros, primero obtiene todos y luego aplica las condiciones manualmente.
     * </p>
     *
     * @param nombre      Cadena parcial a buscar en el nombre (puede ser null)
     * @param minPrecio   Precio mínimo (puede ser null)
     * @param maxPrecio   Precio máximo (puede ser null)
     * @param categoriaId ID de la categoría (puede ser null)
     * @return Lista de productos que cumplen los filtros
     */
    @Transactional(readOnly = true)
    public List<Producto> buscarConFiltros(final String nombre,
                                           final BigDecimal minPrecio,
                                           final BigDecimal maxPrecio,
                                           final Long categoriaId) {
        if (nombre == null && minPrecio == null && maxPrecio == null && categoriaId == null) {
            return productoRepo.findAll();
        }
        if (nombre != null && minPrecio == null && maxPrecio == null && categoriaId == null) {
            return productoRepo.findByNombreContainingIgnoreCase(nombre);
        }
        if (nombre == null && minPrecio != null && maxPrecio != null && categoriaId == null) {
            return productoRepo.findByPrecioBetween(minPrecio, maxPrecio);
        }
        if (nombre == null && minPrecio == null && maxPrecio == null && categoriaId != null) {
            return productoRepo.findByCategoriaId(categoriaId);
        }

        final List<Producto> listaBase = productoRepo.findAll();
        final List<Producto> filtrados = new ArrayList<>();

        for (final Producto p : listaBase) {
            boolean cumple = true;

            if (nombre != null &&
                !p.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                cumple = false;
            }

            if (cumple && minPrecio != null && maxPrecio != null) {
                final BigDecimal precio = p.getPrecio();
                if (precio.compareTo(minPrecio) < 0 || precio.compareTo(maxPrecio) > 0) {
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

    /**
     * Crea un nuevo producto y lo guarda en la base de datos.
     *
     * @param producto Objeto Producto con todos los datos (nombre, precio, stock, categoría)
     * @return El producto creado, incluyendo su ID generado
     * @throws EntityNotFoundException si la categoría asociada no existe
     */
    @Transactional
    public Producto crearProducto(final Producto producto) {
        final Long categoriaId = producto.getCategoria().getId();
        final Categoria categoria = categoriaService.buscarPorId(categoriaId)
            .orElseThrow(() ->
                new EntityNotFoundException("Categoría no encontrada con id " + categoriaId)
            );
        producto.setCategoria(categoria);
        return productoRepo.save(producto);
    }

    /**
     * Actualiza un producto existente según su ID.
     *
     * @param id                ID del producto a modificar
     * @param datosActualizados Nuevo objeto Producto con los datos actualizados
     * @return El producto actualizado
     * @throws EntityNotFoundException si no se encuentra el producto o la categoría nueva
     */
    @Transactional
    public Producto actualizarProducto(final Long id, final Producto datosActualizados) {
        final Producto existente = productoRepo.findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException("Producto no encontrado con id " + id)
            );

        existente.setNombre(datosActualizados.getNombre());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());

        final Long nuevaCategoriaId = datosActualizados.getCategoria().getId();
        if (!existente.getCategoria().getId().equals(nuevaCategoriaId)) {
            final Categoria nuevaCategoria = categoriaService.buscarPorId(nuevaCategoriaId)
                .orElseThrow(() ->
                    new EntityNotFoundException("Categoría no encontrada con id " + nuevaCategoriaId)
                );
            existente.setCategoria(nuevaCategoria);
        }

        return productoRepo.save(existente);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     * @throws EntityNotFoundException si no se encuentra el producto
     */
    @Transactional
    public void eliminarProducto(final Long id) {
        if (!productoRepo.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id " + id);
        }
        productoRepo.deleteById(id);
    }
}
