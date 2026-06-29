package com.posref.pos.service;

import com.posref.pos.dto.ProductoCompatibilidadMotoDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.MotoVersiones;
import com.posref.pos.model.ProductoCompatibilidadMoto;
import com.posref.pos.model.Productos;
import com.posref.pos.repository.MotoVersionesRepository;
import com.posref.pos.repository.ProductoCompatibilidadMotoRepository;
import com.posref.pos.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoCompatiilidadMotoService implements IProductoCompatibilidadMotoService{

    @Autowired
    ProductoCompatibilidadMotoRepository compatibilidadRepo;
    @Autowired
    ProductosRepository productoRepo;
    @Autowired
    MotoVersionesRepository versionRepo;

    @Override
    public List<ProductoCompatibilidadMotoDTO> traerCompatibilidades(){
        return compatibilidadRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductoCompatibilidadMotoDTO crearCompatibilidad(ProductoCompatibilidadMotoDTO compatibilidadDto){

        if (compatibilidadDto.getProductoId() == null) throw new RuntimeException("Debe indicar el id del producto");
        if (compatibilidadDto.getMotoVersionId() == null) throw new RuntimeException("Debe indicar el id de la motoversion");

        //Buscar el producto
        Productos producto = productoRepo.findById(compatibilidadDto.getProductoId()).orElseThrow(() -> new NotFoundException("Id Prodcuto no encontrado"));
        if (producto == null) {
            throw new NotFoundException("Producto no encontrado");
        }

        //Buscar la version
        MotoVersiones version = versionRepo.findById(compatibilidadDto.getMotoVersionId()).orElseThrow(() -> new NotFoundException("Id version no encontrado"));
        if (version == null) {
            throw new NotFoundException("Version no encontrado");
        }

        ProductoCompatibilidadMoto compatibilidad = ProductoCompatibilidadMoto.builder()
                .id(compatibilidadDto.getId())
                .producto(producto)
                .motoVersion(version)
                .observaciones(compatibilidadDto.getObservaciones())
                .activo(true)
                .build();


        return Mapper.toDTO(compatibilidadRepo.save(compatibilidad));
    }

    @Override
    public ProductoCompatibilidadMotoDTO actualizarConpatibilidad(Long id, ProductoCompatibilidadMotoDTO compatibilidadDto){

        //buscar si ProductoCompatibilidadMotoDTO existe para actualizarlo
        ProductoCompatibilidadMoto pc = compatibilidadRepo.findById(id).orElseThrow(() -> new NotFoundException("ProductoCompatibilidad no encontrado"));
        if (pc == null) throw new RuntimeException("ProductoCompatibilidad no encontrado");

       pc.setId(compatibilidadDto.getId());

        if (compatibilidadDto.getId() !=null) {
            Productos producto = productoRepo.findById(compatibilidadDto.getProductoId()).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
            if (producto == null) throw new NotFoundException("Modelo  no encontrado");
            pc.setProducto(producto);

            MotoVersiones version = versionRepo.findById(compatibilidadDto.getMotoVersionId()).orElseThrow(() -> new NotFoundException("version no encontrado"));
            if (version == null) throw new NotFoundException("Version  no encontrado");
            pc.setMotoVersion(version);
        }

        pc.setObservaciones(compatibilidadDto.getObservaciones());
        pc.setActivo(true);

        compatibilidadRepo.save(pc);

        ProductoCompatibilidadMotoDTO compatibilidad = Mapper.toDTO(pc);

        return compatibilidad;
    }

    @Override
    public void eliminarCompatibilidad(Long id) {
        //if (!repo.existsById(id)) {
        //  throw new NotFoundException("Categoria no encontrado para eliminar");
        //}

        //repo.deleteById(id);
        ProductoCompatibilidadMoto pc = compatibilidadRepo.findById(id).orElseThrow(() -> new NotFoundException("compatibilidad no encontrada"));
        if (pc == null) throw new RuntimeException("compatibilidad no encontrada");
        compatibilidadRepo.delete(pc);
    }

}
