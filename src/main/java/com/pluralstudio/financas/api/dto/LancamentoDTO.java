package com.pluralstudio.financas.api.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class LancamentoDTO {

    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private Long usuario;
    private String tipo;
    private String status;


}
