package br.com.satsolucoes.fieldflowweb.dto;

import java.math.BigDecimal;

public record MaterialDTO(
    Long id,
    String nome,
    BigDecimal quantidadeDisponivel,
    String descricao,
    String unidadeMedida
) {}


