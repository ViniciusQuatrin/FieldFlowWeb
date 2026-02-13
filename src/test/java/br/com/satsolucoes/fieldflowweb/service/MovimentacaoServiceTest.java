package br.com.satsolucoes.fieldflowweb.service;

import br.com.satsolucoes.fieldflowweb.dto.MovimentacaoDTO;
import br.com.satsolucoes.fieldflowweb.exception.EstoqueInsuficienteException;
import br.com.satsolucoes.fieldflowweb.exception.QuantidadeInvalidaException;
import br.com.satsolucoes.fieldflowweb.model.Material;
import br.com.satsolucoes.fieldflowweb.model.Movimentacao;
import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import br.com.satsolucoes.fieldflowweb.repository.MaterialRepository;
import br.com.satsolucoes.fieldflowweb.repository.MovimentacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MovimentacaoServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Test
    public void deveImpedirMovimentacaoDeSaidaComEstoqueInsuficiente() {
        // Cenário: Material com estoque 5
        Material material = new Material("Cabo", new BigDecimal("5.0"), "Descricao Cabo", "M");
        material.setId(1L);

        Mockito.when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        MovimentacaoDTO dto = new MovimentacaoDTO(null, TipoMovimentacao.SAIDA, new BigDecimal("10.0"), null,
                "Saída teste", 1L, null);

        // Ação: Tentar registrar saída de 10
        Exception exception = Assertions.assertThrows(EstoqueInsuficienteException.class, () -> {
            movimentacaoService.registrar(dto);
        });

        // Verificação
        Assertions.assertTrue(exception.getMessage().contains("Saldo atual"));
        Mockito.verify(materialRepository, Mockito.never()).save(material);
        Mockito.verify(movimentacaoRepository, Mockito.never()).save(any(Movimentacao.class));
    }

    @Test
    public void deveAtualizarEstoqueNaSaidaComSucesso() {
        // Cenário: Material com estoque 10
        Material material = new Material("Cabo", new BigDecimal("10.0"), "Descricao Cabo", "M");
        material.setId(1L);

        Mockito.when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        Mockito.when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(i -> i.getArguments()[0]);

        MovimentacaoDTO dto = new MovimentacaoDTO(null, TipoMovimentacao.SAIDA, new BigDecimal("5.0"), null,
                "Saída teste", 1L, null);

        // Ação: Tentar registrar saída de 5
        movimentacaoService.registrar(dto);

        // Verificação: Estoque deve ser 5
        Assertions.assertEquals(new BigDecimal("5.0"), material.getQuantidadeDisponivel());
        Mockito.verify(materialRepository, Mockito.times(1)).save(material);
        Mockito.verify(movimentacaoRepository, Mockito.times(1)).save(any(Movimentacao.class));
    }

    @Test
    public void deveAtualizarEstoqueNaEntradaComSucesso() {
        // Cenário: Material com estoque 10
        Material material = new Material("Cabo", new BigDecimal("10.0"), "Descricao Cabo", "M");
        material.setId(1L);

        Mockito.when(materialRepository.findById(1L)).thenReturn(Optional.of(material));
        Mockito.when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(i -> i.getArguments()[0]);

        MovimentacaoDTO dto = new MovimentacaoDTO(null, TipoMovimentacao.ENTRADA, new BigDecimal("5.0"), null,
                "Entrada teste", 1L, null);

        // Ação: Registrar entrada de 5
        movimentacaoService.registrar(dto);

        // Verificação: Estoque deve ser 15
        Assertions.assertEquals(new BigDecimal("15.0"), material.getQuantidadeDisponivel());
        Mockito.verify(materialRepository, Mockito.times(1)).save(material);
        Mockito.verify(movimentacaoRepository, Mockito.times(1)).save(any(Movimentacao.class));
    }

    @Test
    public void deveImpedirMovimentacaoComQuantidadeZero() {
        // Cenário: Material com estoque 10
        Material material = new Material("Cabo", new BigDecimal("10.0"), "Descricao Cabo", "M");
        material.setId(1L);

        Mockito.when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        MovimentacaoDTO dto = new MovimentacaoDTO(null, TipoMovimentacao.SAIDA, BigDecimal.ZERO, null, "Saída zero", 1L,
                null);

        // Ação: Tentar registrar saída com quantidade zero
        Exception exception = Assertions.assertThrows(QuantidadeInvalidaException.class, () -> {
            movimentacaoService.registrar(dto);
        });

        // Verificação
        Assertions.assertTrue(exception.getMessage().contains("positiva"));
        Mockito.verify(materialRepository, Mockito.never()).save(material);
        Mockito.verify(movimentacaoRepository, Mockito.never()).save(any(Movimentacao.class));
    }

    @Test
    public void deveImpedirMovimentacaoComQuantidadeNegativa() {
        // Cenário: Material com estoque 10
        Material material = new Material("Cabo", new BigDecimal("10.0"), "Descricao Cabo", "M");
        material.setId(1L);

        Mockito.when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        MovimentacaoDTO dto = new MovimentacaoDTO(null, TipoMovimentacao.ENTRADA, new BigDecimal("-5.0"), null,
                "Entrada negativa", 1L, null);

        // Ação: Tentar registrar entrada com quantidade negativa
        Exception exception = Assertions.assertThrows(QuantidadeInvalidaException.class, () -> {
            movimentacaoService.registrar(dto);
        });

        // Verificação
        Assertions.assertTrue(exception.getMessage().contains("positiva"));
        Mockito.verify(materialRepository, Mockito.never()).save(material);
        Mockito.verify(movimentacaoRepository, Mockito.never()).save(any(Movimentacao.class));
    }
}
