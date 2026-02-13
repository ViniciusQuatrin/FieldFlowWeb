package br.com.satsolucoes.fieldflowweb.controller;

import br.com.satsolucoes.fieldflowweb.dto.MaterialDTO;
import br.com.satsolucoes.fieldflowweb.service.MaterialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // ============ LISTAGEM E PAGINAÇÃO ============

    @GetMapping("/paginado")
    public ResponseEntity<Page<MaterialDTO>> listarPaginado(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(materialService.listarTodosPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.buscarPorId(id));
    }

    // ============ FILTROS ============

    @GetMapping("/buscar")
    public ResponseEntity<Page<MaterialDTO>> buscarPorNome(
            @RequestParam String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(materialService.buscarPorNome(nome, pageable));
    }

    @GetMapping("/filtro/unidade-medida")
    public ResponseEntity<Page<MaterialDTO>> filtrarPorUnidadeMedida(
            @RequestParam String unidadeMedida,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(materialService.filtrarPorUnidadeMedida(unidadeMedida, pageable));
    }

    // ============ CRUD ============

    @PostMapping
    public ResponseEntity<MaterialDTO> criar(@RequestBody MaterialDTO dto) {
        MaterialDTO salvo = materialService.salvar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialDTO> atualizar(@PathVariable Long id, @RequestBody MaterialDTO dto) {
        return ResponseEntity.ok(materialService.atualizar(id, dto));
    }
}
