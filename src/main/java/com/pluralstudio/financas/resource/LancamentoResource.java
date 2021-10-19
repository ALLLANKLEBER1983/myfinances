package com.pluralstudio.financas.resource;

import com.pluralstudio.financas.api.dto.AtualizaStatusDTO;
import com.pluralstudio.financas.api.dto.LancamentoDTO;
import com.pluralstudio.financas.exceptions.RegraNegocioException;
import com.pluralstudio.financas.model.entities.Lancamento;
import com.pluralstudio.financas.model.entities.Usuario;
import com.pluralstudio.financas.model.enuns.StatusLancamento;
import com.pluralstudio.financas.model.enuns.TipoLancamento;
import com.pluralstudio.financas.service.LancamentoService;
import com.pluralstudio.financas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamento")
@RequiredArgsConstructor
public class LancamentoResource {

    private final LancamentoService service;

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto){
        try {
            Lancamento entidade = converter(dto);
            entidade = service.salvar(entidade);
            return ResponseEntity.ok(entidade);
        }
        catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id,@RequestBody LancamentoDTO dto){
       return service.obterPorId(id).map(entity -> {
           try {
               Lancamento lancamento = converter(dto);
               lancamento.setId(entity.getId());
               service.atualizar(lancamento);
               return ResponseEntity.ok(lancamento);
           }
           catch (RegraNegocioException e ){
               return ResponseEntity.badRequest().body(e.getMessage());
           }
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST));

    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable Long id,@RequestBody AtualizaStatusDTO dto){

        return  service.obterPorId(id).map( entity -> {
            StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
            if (statusSelecionado == null){
                return ResponseEntity.badRequest().body("Não foi possível o status do lançamento, envie um status válido");
            }
            try {
                entity.setStatus(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            }
            catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }

        }).orElseGet( () -> new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST) );

    }

    private Lancamento converter(LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setAno(dto.getAno());
        lancamento.setMes(dto.getMes());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService
                .obterUsuarioPorId(dto.getUsuario())
                .orElseThrow(() ->new RegraNegocioException("Usuário não encontrado para o Id informado "));

        lancamento.setUsuario(usuario);
        if(dto.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
        }
        if(dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.obterPorId(id).map(entidade -> {
            try {
                service.deletar(entidade);
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado na base de dados.",HttpStatus.BAD_REQUEST));

    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao",required = false) String descricao,
            @RequestParam(value = "mes",required = false) Integer mes,
            @RequestParam(value = "ano",required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.obterUsuarioPorId(idUsuario);
        if(usuario.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body("Não foi possível realizar a consulta.Usuário não encontrado para o Id informado");
        }
        else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
        return ResponseEntity.ok(lancamentos);

    }
}
