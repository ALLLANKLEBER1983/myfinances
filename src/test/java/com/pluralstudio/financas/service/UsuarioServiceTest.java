package com.pluralstudio.financas.service;

import com.pluralstudio.financas.exceptions.ErroAutenticacao;
import com.pluralstudio.financas.exceptions.RegraNegocioException;
import com.pluralstudio.financas.model.entities.Usuario;
import com.pluralstudio.financas.model.repository.UsuarioRepository;
import com.pluralstudio.financas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;


    @Test(expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //ação
        Usuario result = service.autenticar(email,senha);

        //verificações
        Assertions.assertThat(result).isNotNull();
    }

    @Test(expected = Test.None.class)
    public void deveSalvarUmUsuario(){
        //cenário
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().id(1l).nome("nome").senha("senha").email("email@email.com").build();
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        //ação
        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        //verificação
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
    }

    @Test(expected = RegraNegocioException.class)
    public void naoDeveSalvarUsuarioComEmailJaCadastrado(){
        //cenário
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        //ação
        service.salvarUsuario(usuario);

        //verificação
        Mockito.verify(repository,Mockito.never()).save(usuario);

    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado(){
        //cenário
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //ação
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com","senha"));

        //verificação
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");

    }

    @Test
    public void deveLancarErroQandoSenhaNaoBater(){
        //cenário
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        //ação
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com","123"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
    }

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //ação
        service.validarEmail("email@email.com");

    }

    @Test(expected = RegraNegocioException.class)
    public void deveLançarErroAoValidarEmailQuandoExistirEmailCadastrado(){

       Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);


        service.validarEmail("email@email.com");
    }
}
