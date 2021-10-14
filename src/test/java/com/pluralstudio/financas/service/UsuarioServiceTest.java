package com.pluralstudio.financas.service;

import com.pluralstudio.financas.exceptions.RegraNegocioException;
import com.pluralstudio.financas.model.entities.Usuario;
import com.pluralstudio.financas.model.repository.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test(expected = Test.None.class)
    public void deveValidarEmail(){
        //cenario
        usuarioRepository.deleteAll();

        //ação
        usuarioService.validarEmail("email@email.com");

    }

    @Test(expected = RegraNegocioException.class)
    public void deveLançarErroAoValidarEmailQuandoExistirEmailCadastrado(){

        Usuario usuario= Usuario.builder().nome("usuario").email("email@email.com").build();
        usuarioRepository.save(usuario);

        
        usuarioService.validarEmail("email@email.com");
    }
}
