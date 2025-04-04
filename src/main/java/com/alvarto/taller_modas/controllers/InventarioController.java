package com.alvarto.taller_modas.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alvarto.taller_modas.dtos.InventarioDTO;
import com.alvarto.taller_modas.services.InventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
public class InventarioController {
    private final InventarioService inventarioService;
    
    @GetMapping
    public ResponseEntity<List<InventarioDTO>> getAllInventarios() {
        return ResponseEntity.ok(inventarioService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventarioById(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.findById(id));
    }
    
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioDTO> getInventarioByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.findByProductoId(productoId));
    }
    
    @PostMapping
    public ResponseEntity<InventarioDTO> createInventario(@RequestBody InventarioDTO inventarioDTO) {
        return new ResponseEntity<>(inventarioService.create(inventarioDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> updateInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO) {
        return ResponseEntity.ok(inventarioService.update(id, inventarioDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable Long id) {
        inventarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/producto/{productoId}/cantidad")
    public ResponseEntity<InventarioDTO> actualizarCantidad(@PathVariable Long productoId, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.actualizarCantidad(productoId, cantidad));
    }
    
    @GetMapping("/bajo-stock")
    public ResponseEntity<List<InventarioDTO>> getBajoStock() {
        return ResponseEntity.ok(inventarioService.getBajoStock());
    }
}