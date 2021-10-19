package com.pluralstudio.financas.resource;

import com.pluralstudio.financas.api.dto.UsuarioDTO;
import com.pluralstudio.financas.exceptions.ErroAutenticacao;
import com.pluralstudio.financas.exceptions.RegraNegocioException;
import com.pluralstudio.financas.model.entities.Usuario;
import com.pluralstudio.financas.service.LancamentoService;
import com.pluralstudio.financas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService service;
    private final LancamentoService lancamentoService;

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

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id){
        Optional<Usuario> usuario =service.obterUsuarioPorId(id);

        if(!usuario.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);

    }


}
