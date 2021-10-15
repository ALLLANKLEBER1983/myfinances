package com.pluralstudio.financas.resource;

import com.pluralstudio.financas.api.dto.UsuarioDTO;
import com.pluralstudio.financas.exceptions.ErroAutenticacao;
import com.pluralstudio.financas.exceptions.RegraNegocioException;
import com.pluralstudio.financas.model.entities.Usuario;
import com.pluralstudio.financas.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioResource {

    private UsuarioService service;

    @PostMapping
    public ResponseEntity salvar( @RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario
                .builder()
                .email(dto.getEmail())
                .nome(dto.getNome())
                .senha(dto.getSenha())
                .build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);

        }
        catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto){
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        }
        catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
