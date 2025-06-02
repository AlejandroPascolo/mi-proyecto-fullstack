package com.tuempresa.aplicacion.repository;

import com.tuempresa.aplicacion.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);
    List<Producto> findByCategoriaId(Long categoriaId);
}
