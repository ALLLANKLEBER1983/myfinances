package com.pluralstudio.financas.service;

import com.pluralstudio.financas.model.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email,String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

}
