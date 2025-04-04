package com.alvarto.taller_modas.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alvarto.taller_modas.models.Inventario;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    
    Optional<Inventario> findByProductoId(Long productoId);
    
    @Query("SELECT i FROM Inventario i WHERE i.cantidad <= i.nivelMinimo")
    List<Inventario> findBajoStock();
}