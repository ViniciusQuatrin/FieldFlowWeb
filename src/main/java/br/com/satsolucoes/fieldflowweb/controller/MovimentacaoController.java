package br.com.satsolucoes.fieldflowweb.controller;

import br.com.satsolucoes.fieldflowweb.dto.MovimentacaoDTO;
import br.com.satsolucoes.fieldflowweb.model.TipoMovimentacao;
import br.com.satsolucoes.fieldflowweb.service.MovimentacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoDTO> registrar(@RequestBody MovimentacaoDTO dto) {
        MovimentacaoDTO salvo = movimentacaoService.registrar(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(salvo);
    }

    // ============ LISTAGEM E PAGINAÇÃO ============

    @GetMapping
    public ResponseEntity<Page<MovimentacaoDTO>> listar(
            @PageableDefault(size = 10, sort = "dataHora", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movimentacaoService.listarTodasPaginado(pageable));
    }

    @GetMapping("/material/{materialId}")
    public ResponseEntity<Page<MovimentacaoDTO>> listarPorMaterial(
            @PathVariable Long materialId,
            @PageableDefault(size = 10, sort = "dataHora", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movimentacaoService.listarPorMaterial(materialId, pageable));
    }

    @GetMapping("/material/{materialId}/historico")
    public ResponseEntity<List<MovimentacaoDTO>> listarHistoricoMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok(movimentacaoService.listarHistoricoMaterial(materialId));
    }

    // ============ FILTROS ============

    @GetMapping("/filtro/tipo")
    public ResponseEntity<Page<MovimentacaoDTO>> filtrarPorTipo(
            @RequestParam TipoMovimentacao tipo,
            @PageableDefault(size = 10, sort = "dataHora", direction = Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(movimentacaoService.filtrarPorTipo(tipo, pageable));
    }
}
