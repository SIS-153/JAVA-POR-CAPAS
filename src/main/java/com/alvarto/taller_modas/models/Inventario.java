package com.alvarto.taller_modas.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "producto_id", nullable = false, unique = true)
    private Producto producto;
    
    private Integer cantidad;
    
    @Column(name = "nivel_minimo")
    private Integer nivelMinimo;
    
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;
    
    @PrePersist
    @PreUpdate
    public void actualizarFecha() {
        ultimaActualizacion = LocalDateTime.now();
    }
}