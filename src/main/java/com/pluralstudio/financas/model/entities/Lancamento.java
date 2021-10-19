package com.pluralstudio.financas.model.entities;

import com.pluralstudio.financas.model.enuns.StatusLancamento;
import com.pluralstudio.financas.model.enuns.TipoLancamento;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
public class Lancamento {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mes;

    private Integer ano;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private BigDecimal valor;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dataCadastro;

    @Enumerated(value = EnumType.STRING)
    private TipoLancamento tipo;

    @Enumerated(value = EnumType.STRING)
    private StatusLancamento status;
}
