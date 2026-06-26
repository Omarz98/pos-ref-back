package com.posref.pos.dto;

import com.posref.pos.model.MotoVersiones;
import com.posref.pos.model.Productos;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoCompatibilidadMotoDTO {

    private Long id;

    private Long productoId;

    private Long motoVersionId;

    private String observaciones;

    private Boolean activo;

}
