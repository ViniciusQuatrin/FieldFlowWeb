package br.com.satsolucoes.fieldflowweb.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MaterialDTO(
        Long id,

        @NotBlank(message = "Nome é obrigatório") @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres") String nome,

        @NotNull(message = "Quantidade disponível é obrigatória") @DecimalMin(value = "0.0", message = "Quantidade disponível não pode ser negativa") BigDecimal quantidadeDisponivel,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres") String descricao,

        @NotBlank(message = "Unidade de medida é obrigatória") @Size(max = 10, message = "Unidade de medida deve ter no máximo 10 caracteres") String unidadeMedida) {
}
