package com.pluralstudio.financas.model.repository;

import com.pluralstudio.financas.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
