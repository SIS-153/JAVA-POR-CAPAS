package com.alvarto.taller_modas.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alvarto.taller_modas.dtos.ProductoDTO;
import com.alvarto.taller_modas.services.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<ProductoDTO> createProducto(@RequestBody ProductoDTO productoDTO) {
        return new ResponseEntity<>(productoService.create(productoDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.update(id, productoDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getProductosByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoria(categoriaId));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ProductoDTO>> searchProductos(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.search(nombre));
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<List<ProductoDTO>> filtrarProductos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String talla) {
        return ResponseEntity.ok(productoService.filtrar(categoriaId, color, talla));
    }
}