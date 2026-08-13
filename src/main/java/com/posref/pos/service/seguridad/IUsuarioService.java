package com.posref.pos.service.seguridad;

import com.posref.pos.dto.usuario.*;

import java.util.List;

public interface IUsuarioService {

    List<UsuarioResponse> obtenerTodos();

    UsuarioResponse obtenerPorId(
            Long id
    );

    UsuarioResponse crear(
            UsuarioRequest request
    );

    UsuarioResponse actualizar(
            Long id,
            UsuarioActualizarRequest request
    );

    UsuarioResponse cambiarEstado(
            Long id,
            Boolean activo
    );

    void cambiarPassword(
            Long id,
            CambiarPasswordRequest request
    );

}
