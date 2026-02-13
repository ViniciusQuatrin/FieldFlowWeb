package br.com.satsolucoes.fieldflowweb.repository;

import br.com.satsolucoes.fieldflowweb.model.Material;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Material m WHERE m.id = :id")
    Optional<Material> findByIdWithLock(@Param("id") Long id);

    // Buscar por nome (contém, ignorando case)
    Page<Material> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // Filtrar por unidade de medida
    Page<Material> findByUnidadeMedida(String unidadeMedida, Pageable pageable);
}