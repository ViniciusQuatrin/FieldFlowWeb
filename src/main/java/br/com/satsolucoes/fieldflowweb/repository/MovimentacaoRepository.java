package br.com.satsolucoes.fieldflowweb.repository;

import br.com.satsolucoes.fieldflowweb.model.Movimentacao;
import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    // Buscar movimentações por material com JOIN FETCH para evitar N+1
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.material WHERE m.material.id = :materialId")
    Page<Movimentacao> findByMaterialId(@Param("materialId") Long materialId, Pageable pageable);

    // Filtrar por tipo de movimentação com JOIN FETCH para evitar N+1
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.material WHERE m.tipo = :tipo")
    Page<Movimentacao> findByTipo(@Param("tipo") TipoMovimentacao tipo, Pageable pageable);

    // Listar todas as movimentações de um material com JOIN FETCH para evitar N+1
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.material WHERE m.material.id = :materialId ORDER BY m.dataHora DESC")
    List<Movimentacao> findByMaterialIdOrderByDataHoraDesc(@Param("materialId") Long materialId);

    // Listar todas as movimentações com JOIN FETCH para evitar N+1
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.material")
    Page<Movimentacao> findAllWithMaterial(Pageable pageable);

    // Filtrar movimentações com observação com JOIN FETCH
    @Query("SELECT m FROM Movimentacao m JOIN FETCH m.material WHERE m.observacao IS NOT NULL AND m.observacao <> ''")
    Page<Movimentacao> findComObservacao(Pageable pageable);
}
