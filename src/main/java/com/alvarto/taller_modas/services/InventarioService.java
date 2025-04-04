package com.alvarto.taller_modas.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alvarto.taller_modas.dtos.InventarioDTO;
import com.alvarto.taller_modas.error.BadRequestException;
import com.alvarto.taller_modas.error.ResourceNotFoundException;
import com.alvarto.taller_modas.models.Inventario;
import com.alvarto.taller_modas.models.Producto;
import com.alvarto.taller_modas.repositories.InventarioRepository;
import com.alvarto.taller_modas.repositories.ProductoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    
    // Mapear de Entidad a DTO
    private InventarioDTO mapToDTO(Inventario inventario) {
        InventarioDTO dto = new InventarioDTO();
        dto.setId(inventario.getId());
        dto.setProductoId(inventario.getProducto().getId());
        dto.setProductoNombre(inventario.getProducto().getNombre());
        dto.setCantidad(inventario.getCantidad());
        dto.setNivelMinimo(inventario.getNivelMinimo());
        dto.setUltimaActualizacion(inventario.getUltimaActualizacion());
        dto.setBajoStock(inventario.getCantidad() <= inventario.getNivelMinimo());
        return dto;
    }
    
    // Mapear de DTO a Entidad
    private Inventario mapToEntity(InventarioDTO dto) {
        Inventario inventario = new Inventario();
        inventario.setId(dto.getId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setNivelMinimo(dto.getNivelMinimo());
        
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + dto.getProductoId()));
        inventario.setProducto(producto);
        
        return inventario;
    }
    
    // Obtener todos los inventarios
    @Transactional(readOnly = true)
    public List<InventarioDTO> findAll() {
        return inventarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener inventario por ID
    @Transactional(readOnly = true)
    public InventarioDTO findById(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con ID: " + id));
        return mapToDTO(inventario);
    }
    
    // Obtener inventario por ID de producto
    @Transactional(readOnly = true)
    public InventarioDTO findByProductoId(Long productoId) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto con ID: " + productoId));
        return mapToDTO(inventario);
    }
    
    // Crear un nuevo registro de inventario
    @Transactional
    public InventarioDTO create(InventarioDTO inventarioDTO) {
        if (inventarioDTO.getId() != null) {
            throw new BadRequestException("Un nuevo inventario no puede tener ID asignado");
        }
        
        // Verificar si ya existe un inventario para este producto
        Optional<Inventario> existingInventario = inventarioRepository.findByProductoId(inventarioDTO.getProductoId());
        if (existingInventario.isPresent()) {
            throw new BadRequestException("Ya existe un inventario para el producto con ID: " + inventarioDTO.getProductoId());
        }
        
        Inventario inventario = mapToEntity(inventarioDTO);
        Inventario saved = inventarioRepository.save(inventario);
        return mapToDTO(saved);
    }
    
    // Actualizar un registro de inventario existente
    @Transactional
    public InventarioDTO update(Long id, InventarioDTO inventarioDTO) {
        Inventario existingInventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con ID: " + id));
        
        // Verificar si el producto ha cambiado
        if (!existingInventario.getProducto().getId().equals(inventarioDTO.getProductoId())) {
            // Si ha cambiado, verificar que no exista otro inventario para ese producto
            Optional<Inventario> otherInventario = inventarioRepository.findByProductoId(inventarioDTO.getProductoId());
            if (otherInventario.isPresent()) {
                throw new BadRequestException("Ya existe un inventario para el producto con ID: " + inventarioDTO.getProductoId());
            }
        }
        
        inventarioDTO.setId(id);
        Inventario inventario = mapToEntity(inventarioDTO);
        Inventario updated = inventarioRepository.save(inventario);
        return mapToDTO(updated);
    }
    
    // Eliminar un registro de inventario
    @Transactional
    public void delete(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventario no encontrado con ID: " + id);
        }
        inventarioRepository.deleteById(id);
    }
    
    // Actualizar la cantidad en inventario
    @Transactional
    public InventarioDTO actualizarCantidad(Long productoId, Integer cantidad) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para el producto con ID: " + productoId));
        
        if (cantidad < 0) {
            throw new BadRequestException("La cantidad no puede ser negativa");
        }
        
        inventario.setCantidad(cantidad);
        Inventario updated = inventarioRepository.save(inventario);
        return mapToDTO(updated);
    }
    
    // Obtener productos con bajo stock
    @Transactional(readOnly = true)
    public List<InventarioDTO> getBajoStock() {
        return inventarioRepository.findBajoStock().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}