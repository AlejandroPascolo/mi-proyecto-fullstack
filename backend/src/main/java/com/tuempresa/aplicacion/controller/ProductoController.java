package com.tuempresa.aplicacion.controller;

import com.tuempresa.aplicacion.entity.Producto;
import com.tuempresa.aplicacion.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para manejar las operaciones CRUD de Productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(final ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        final List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable final Long id) {
        final Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody final Producto producto) {
        final Producto creado = productoService.crearProducto(producto);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable final Long id,
                                               @RequestBody final Producto producto) {
        final Producto actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable final Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtros")
    public ResponseEntity<List<Producto>> buscarConFiltros(
            @RequestParam(required = false) final String nombre,
            @RequestParam(required = false) final BigDecimal minPrecio,
            @RequestParam(required = false) final BigDecimal maxPrecio,
            @RequestParam(required = false) final Long categoriaId) {
        final List<Producto> resultados = productoService.buscarConFiltros(
                nombre, minPrecio, maxPrecio, categoriaId
        );
        return ResponseEntity.ok(resultados);
    }
}
