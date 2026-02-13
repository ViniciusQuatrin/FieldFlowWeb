package br.com.satsolucoes.fieldflowweb.repository;

import br.com.satsolucoes.fieldflowweb.model.Movimentacao;
import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Buscar movimentações por material
    Page<Movimentacao> findByMaterialId(Long materialId, Pageable pageable);

    // Filtrar por tipo de movimentação
    Page<Movimentacao> findByTipo(TipoMovimentacao tipo, Pageable pageable);

    // Listar todas as movimentações de um material
    List<Movimentacao> findByMaterialIdOrderByDataHoraDesc(Long materialId);
}
