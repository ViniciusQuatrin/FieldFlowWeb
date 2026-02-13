package br.com.satsolucoes.fieldflowweb.dto;

import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoDTO(
    Long id,

    @NotNull(message = "Tipo de movimentação é obrigatório")
    TipoMovimentacao tipo,

    @NotNull(message = "Quantidade é obrigatória")
    @DecimalMin(value = "0.01", message = "Quantidade deve ser maior que zero")
    BigDecimal quantidade,

    LocalDateTime dataHora,

    @Size(max = 500, message = "Observação deve ter no máximo 500 caracteres")
    String observacao,

    @NotNull(message = "ID do material é obrigatório")
    Long materialId,

    String materialNome
) {}

