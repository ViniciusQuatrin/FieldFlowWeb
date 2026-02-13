package br.com.satsolucoes.fieldflowweb.service;

import br.com.satsolucoes.fieldflowweb.dto.MovimentacaoDTO;
import br.com.satsolucoes.fieldflowweb.exception.MaterialNaoEncontradoException;
import br.com.satsolucoes.fieldflowweb.model.Material;
import br.com.satsolucoes.fieldflowweb.model.Movimentacao;
import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import br.com.satsolucoes.fieldflowweb.repository.MaterialRepository;
import br.com.satsolucoes.fieldflowweb.repository.MovimentacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final MaterialRepository materialRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, MaterialRepository materialRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public MovimentacaoDTO registrar(MovimentacaoDTO dto) {
        Material material = materialRepository.findById(dto.materialId())
                .orElseThrow(() -> new MaterialNaoEncontradoException(dto.materialId()));

        if (dto.tipo() == TipoMovimentacao.SAIDA) {
            material.debitarEstoque(dto.quantidade());
        } else if (dto.tipo() == TipoMovimentacao.ENTRADA) {
            material.creditarEstoque(dto.quantidade());
        }

        materialRepository.save(material);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo(dto.tipo());
        movimentacao.setQuantidade(dto.quantidade());
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setObservacao(dto.observacao());
        movimentacao.setMaterial(material);

        Movimentacao salvo = movimentacaoRepository.save(movimentacao);
        return toDTO(salvo);
    }

    // ============ LISTAGEM E PAGINAÇÃO ============

    public Page<MovimentacaoDTO> listarTodasPaginado(Pageable pageable) {
        return movimentacaoRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<MovimentacaoDTO> listarPorMaterial(Long materialId, Pageable pageable) {
        return movimentacaoRepository.findByMaterialId(materialId, pageable).map(this::toDTO);
    }

    public List<MovimentacaoDTO> listarHistoricoMaterial(Long materialId) {
        return movimentacaoRepository.findByMaterialIdOrderByDataHoraDesc(materialId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ============ FILTROS ============

    public Page<MovimentacaoDTO> filtrarPorTipo(TipoMovimentacao tipo, Pageable pageable) {
        return movimentacaoRepository.findByTipo(tipo, pageable).map(this::toDTO);
    }

    // ============ CONVERSÃO ============

    private MovimentacaoDTO toDTO(Movimentacao movimentacao) {
        return new MovimentacaoDTO(
                movimentacao.getId(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getDataHora(),
                movimentacao.getObservacao(),
                movimentacao.getMaterial().getId(),
                movimentacao.getMaterial().getNome());
    }
}
