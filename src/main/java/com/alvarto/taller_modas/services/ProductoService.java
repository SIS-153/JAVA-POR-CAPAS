package com.alvarto.taller_modas.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alvarto.taller_modas.dtos.ProductoDTO;
import com.alvarto.taller_modas.error.BadRequestException;
import com.alvarto.taller_modas.error.ResourceNotFoundException;
import com.alvarto.taller_modas.models.Categoria;
import com.alvarto.taller_modas.models.Producto;
import com.alvarto.taller_modas.repositories.CategoriaRepository;
import com.alvarto.taller_modas.repositories.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    
    // Mapear de Entidad a DTO
    private ProductoDTO mapToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setColor(producto.getColor());
        dto.setTalla(producto.getTalla());
        dto.setImagen(producto.getImagen());
        dto.setCategoriaId(producto.getCategoria().getId());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        return dto;
    }
    
    // Mapear de DTO a Entidad
    private Producto mapToEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setColor(dto.getColor());
        producto.setTalla(dto.getTalla());
        producto.setImagen(dto.getImagen());
        
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));
        producto.setCategoria(categoria);
        
        return producto;
    }
    
    // Obtener todos los productos
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener un producto por ID
    @Transactional(readOnly = true)
    public ProductoDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return mapToDTO(producto);
    }
    
    // Crear un nuevo producto
    @Transactional
    public ProductoDTO create(ProductoDTO productoDTO) {
        if (productoDTO.getId() != null) {
            throw new BadRequestException("Un nuevo producto no puede tener ID asignado");
        }
        
        Producto producto = mapToEntity(productoDTO);
        Producto saved = productoRepository.save(producto);
        return mapToDTO(saved);
    }
    
    // Actualizar un producto existente
    @Transactional
    public ProductoDTO update(Long id, ProductoDTO productoDTO) {
        Producto existingProducto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        productoDTO.setId(id);
        Producto producto = mapToEntity(productoDTO);
        Producto updated = productoRepository.save(producto);
        return mapToDTO(updated);
    }
    
    // Eliminar un producto
    @Transactional
    public void delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    // Buscar productos por categoría
    @Transactional(readOnly = true)
    public List<ProductoDTO> findByCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId);
        }
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar productos por nombre
    @Transactional(readOnly = true)
    public List<ProductoDTO> search(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Filtrar productos
    @Transactional(readOnly = true)
    public List<ProductoDTO> filtrar(Long categoriaId, String color, String talla) {
        return productoRepository.filtrarProductos(categoriaId, color, talla).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}