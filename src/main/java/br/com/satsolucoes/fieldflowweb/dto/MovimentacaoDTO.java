package br.com.satsolucoes.fieldflowweb.dto;

import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoDTO(
    Long id,
    TipoMovimentacao tipo,
    BigDecimal quantidade,
    LocalDateTime dataHora,
    String observacao,
    Long materialId,
    String materialNome
) {}

