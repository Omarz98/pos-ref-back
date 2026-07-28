package com.posref.pos.service;

import com.posref.pos.dto.ProductoCompatibleResponse;
import com.posref.pos.exception.RecursoNoEncontradoException;
import com.posref.pos.mapper.ProductoCompatibleMapper;
import com.posref.pos.repository.MotoVersionesRepository;
import com.posref.pos.repository.ProductoCompatibilidadMotoRepository;
import com.posref.pos.repository.ProductosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoCompatibilidadService implements IProductoCompatibilidadService{

    private ProductoCompatibilidadMotoRepository
            compatibilidadRepository;

    private MotoVersionesRepository motoVersionesRepository;
    private ProductosRepository productosRepository;
    private ProductoCompatibleMapper productoCompatibleMapper;

    @Override
    @Transactional
    public List<ProductoCompatibleResponse>
    obtenerProductosCompatibles(Long motoVersionId) {

        if (!motoVersionesRepository.existsById(motoVersionId)) {
            throw new RecursoNoEncontradoException(
                    "No existe la versión de motocicleta con id: "
                            + motoVersionId
            );
        }

        return compatibilidadRepository
                .buscarProductosCompatiblesOUniversales(motoVersionId)
                .stream()
                .map(productoCompatibleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean esCompatible(
            Long productoId,
            Long motoVersionId
    ) {

        var producto = productosRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el producto con id: " + productoId
                ));

        if (Boolean.TRUE.equals(
                producto.getCompatibilidadUniversal()
        )) {
            return true;
        }

        return compatibilidadRepository
                .existsByProductoIdAndMotoVersionIdAndActivoTrue(
                        productoId,
                        motoVersionId
                );
    }

}
