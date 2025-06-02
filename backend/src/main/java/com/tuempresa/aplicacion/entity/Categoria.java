package com.tuempresa.aplicacion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa la tabla 'categorias' en la base de datos.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Size(max = 250, message = "La descripción no puede superar los 250 caracteres")
    @Column(length = 250)
    private String descripcion;

    @OneToMany(
            mappedBy = "categoria",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Producto> productos = new ArrayList<>();

    public Categoria() {
        // Constructor por defecto para JPA
    }

    public Categoria(final String nombre, final String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(final List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Agrega un producto a esta categoría y establece la relación bidireccional.
     *
     * @param producto Objeto Producto a agregar
     */
    public void agregarProducto(final Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }

    /**
     * Remueve un producto de esta categoría y elimina la relación bidireccional.
     *
     * @param producto Objeto Producto a remover
     */
    public void quitarProducto(final Producto producto) {
        productos.remove(producto);
        producto.setCategoria(null);
    }
}
