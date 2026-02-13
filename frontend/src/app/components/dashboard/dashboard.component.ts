import { Component, OnInit } from '@angular/core';
import { MaterialService, Material, Page, Movimentacao } from '../../services/material.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  materials: Material[] = [];

  // Pagination
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  pagesArray: number[] = [];

  // Filters
  searchTerm: string = '';
  selectedUnit: string = '';

  // KPI Placeholders
  totalItemsCount = 0;

  // Dialog State
  showMovementDialog = false;
  selectedMaterial: Material | null = null;
  movementType: 'ENTRADA' | 'SAIDA' = 'SAIDA';
  movementQuantity: number = 1;
  movementObservation: string = '';

  constructor(private materialService: MaterialService) { }

  ngOnInit(): void {
    this.loadMaterials();
  }

  loadMaterials(): void {
    let obs$;

    if (this.searchTerm) {
      obs$ = this.materialService.buscarPorNome(this.searchTerm, this.page, this.size);
    } else if (this.selectedUnit && this.selectedUnit !== 'Todos') {
      obs$ = this.materialService.filtrarPorUnidade(this.selectedUnit, this.page, this.size);
    } else {
      obs$ = this.materialService.listarPaginado(this.page, this.size);
    }

    obs$.subscribe({
      next: (response) => {
        this.materials = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.page = response.number;

        this.updatePagesArray();
        this.calculateKPIs(response.content);
      },
      error: (err) => console.error('Erro ao carregar materiais', err)
    });
  }

  onSearch(): void {
    this.page = 0;
    this.selectedUnit = ''; // Reset other filter
    this.loadMaterials();
  }

  onFilterUnit(unit: string): void {
    this.page = 0;
    this.searchTerm = ''; // Reset other filter
    this.selectedUnit = unit;
    this.loadMaterials();
  }

  changePage(newPage: number): void {
    if (newPage >= 0 && newPage < this.totalPages) {
      this.page = newPage;
      this.loadMaterials();
    }
  }

  updatePagesArray(): void {
    this.pagesArray = Array.from({ length: this.totalPages }, (_, i) => i);
  }

  calculateKPIs(currentList: Material[]): void {
    this.totalItemsCount = this.totalElements;
  }

  getStockStatus(qtd: number): 'Em Estoque' | 'Estoque Baixo' | 'Sem Estoque' {
    if (qtd <= 0) return 'Sem Estoque';
    if (qtd < 10) return 'Estoque Baixo';
    return 'Em Estoque';
  }

  getStatusColorClass(qtd: number): string {
    if (qtd <= 0) return 'bg-gray-100 text-gray-800 dark:bg-gray-500/10 dark:text-gray-400';
    if (qtd < 10) return 'bg-red-100 text-red-800 dark:bg-red-500/10 dark:text-red-400 border border-red-200 dark:border-red-500/20';
    return 'bg-green-100 text-green-800 dark:bg-green-500/10 dark:text-green-400 border border-green-200 dark:border-green-500/20';
  }

  // --- Movement Dialog ---

  openMovementDialog(material: Material): void {
    this.selectedMaterial = material;
    this.movementType = 'SAIDA';
    this.movementQuantity = 1;
    this.movementObservation = '';
    this.showMovementDialog = true;
  }

  closeMovementDialog(): void {
    this.showMovementDialog = false;
    this.selectedMaterial = null;
  }

  confirmMovement(): void {
    if (!this.selectedMaterial || !this.selectedMaterial.id) return;
    if (this.movementQuantity <= 0) {
      alert("Quantidade deve ser maior que zero.");
      return;
    }

    const movimentacao: Movimentacao = {
      materialId: this.selectedMaterial.id,
      tipo: this.movementType,
      quantidade: this.movementQuantity,
      observacao: this.movementObservation
    };

    this.materialService.registrarMovimentacao(movimentacao).subscribe({
      next: () => {
        this.closeMovementDialog();
        this.loadMaterials(); // Refresh list to update stock
        // Could add a toast success message here
      },
      error: (err) => {
        console.error('Erro ao registrar movimentação', err);
        alert('Erro ao registrar movimentação. Verifique o estoque.');
      }
    });
  }
}
