package com.posref.pos.mapper;

import com.posref.pos.dto.MotocicletaResponse;
import com.posref.pos.model.Motocicleta;
import org.springframework.stereotype.Component;

@Component
public class MotocicletaMapper {

    public MotocicletaResponse toResponse(Motocicleta motocicleta) {

        if (motocicleta == null) {
            return null;
        }

        var cliente = motocicleta.getCliente();
        var motoVersion = motocicleta.getMotoVersion();
        var motoModelo = motoVersion.getMotoModelo();
        var motoMarca = motoModelo.getMarca();

        return MotocicletaResponse.builder()
                .id(motocicleta.getId())

                .clienteId(cliente.getId())
                .clienteNombre(cliente.getNombre())

                .motoMarcaId(motoMarca.getId())
                .motoMarcaNombre(motoMarca.getNombre())

                .motoModeloId(motoModelo.getId())
                .motoModeloNombre(motoModelo.getNombre())

                .motoVersionId(motoVersion.getId())
                .anio(motoVersion.getAnio())
                .cilindraje(motoVersion.getCilindraje())
                .version(motoVersion.getVersion())

                .numeroSerie(motocicleta.getNumeroSerie())
                .numeroMotor(motocicleta.getNumeroMotor())
                .placas(motocicleta.getPlacas())
                .color(motocicleta.getColor())
                .kilometrajeActual(motocicleta.getKilometrajeActual())
                .observaciones(motocicleta.getObservaciones())

                .activo(motocicleta.getActivo())
                .fechaCreacion(motocicleta.getFechaCreacion())
                .fechaActualizacion(motocicleta.getFechaActualizacion())

                .build();
    }

}
