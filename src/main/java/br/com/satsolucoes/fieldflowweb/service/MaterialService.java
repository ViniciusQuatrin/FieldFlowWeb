package br.com.satsolucoes.fieldflowweb.service;

import br.com.satsolucoes.fieldflowweb.dto.MaterialDTO;
import br.com.satsolucoes.fieldflowweb.exception.MaterialNaoEncontradoException;
import br.com.satsolucoes.fieldflowweb.model.Material;
import br.com.satsolucoes.fieldflowweb.repository.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    // ============ LISTAGEM E PAGINAÇÃO ============

    public Page<MaterialDTO> listarTodosPaginado(Pageable pageable) {
        return materialRepository.findAll(pageable).map(this::toDTO);
    }

    public MaterialDTO buscarPorId(Long id) {
        return materialRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));
    }

    // ============ FILTROS ============

    public Page<MaterialDTO> buscarPorNome(String nome, Pageable pageable) {
        return materialRepository.findByNomeContainingIgnoreCase(nome, pageable).map(this::toDTO);
    }

    public Page<MaterialDTO> filtrarPorUnidadeMedida(String unidadeMedida, Pageable pageable) {
        return materialRepository.findByUnidadeMedida(unidadeMedida, pageable).map(this::toDTO);
    }

    // ============ CRUD ============

    @Transactional(rollbackFor = Exception.class)
    public MaterialDTO salvar(MaterialDTO dto) {
        Material material = new Material();
        material.setNome(dto.nome());
        material.setQuantidadeDisponivel(dto.quantidadeDisponivel());
        material.setDescricao(dto.descricao());
        material.setUnidadeMedida(dto.unidadeMedida());

        Material saved = materialRepository.save(material);
        return toDTO(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public MaterialDTO atualizar(Long id, MaterialDTO dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNaoEncontradoException(id));

        material.setNome(dto.nome());
        material.setDescricao(dto.descricao());
        material.setUnidadeMedida(dto.unidadeMedida());

        return toDTO(materialRepository.save(material));
    }

    private MaterialDTO toDTO(Material material) {
        return new MaterialDTO(
                material.getId(),
                material.getNome(),
                material.getQuantidadeDisponivel(),
                material.getDescricao(),
                material.getUnidadeMedida());
    }
}
