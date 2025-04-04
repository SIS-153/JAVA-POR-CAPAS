package com.alvarto.taller_modas.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alvarto.taller_modas.models.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByCategoriaId(Long categoriaId);
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT p FROM Producto p WHERE " +
           "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
           "(:color IS NULL OR p.color = :color) AND " +
           "(:talla IS NULL OR p.talla = :talla)")
    List<Producto> filtrarProductos(
            @Param("categoriaId") Long categoriaId,
            @Param("color") String color,
            @Param("talla") String talla);
}