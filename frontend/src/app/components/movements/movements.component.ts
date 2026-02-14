import { Component, OnInit, OnDestroy } from '@angular/core';
import { MaterialService } from '../../services/material.service';
import { Movimentacao } from '../../models/movimentacao.model';
import { Subject, of } from 'rxjs';
import { takeUntil, catchError, finalize } from 'rxjs/operators';

@Component({
  selector: 'app-movements',
  template: `
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
      <div>
        <h1 class="text-2xl font-bold text-slate-900 dark:text-white">Movimentações</h1>
        <p class="text-slate-500 dark:text-slate-400 text-sm mt-1">Histórico de todas as entradas e saídas de estoque.</p>
      </div>
    </div>

    <!-- Error Message -->
    <div *ngIf="errorMessage" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
      <strong class="font-bold">Erro!</strong>
      <span class="block sm:inline"> {{ errorMessage }}</span>
    </div>

    <!-- Loading Spinner (Simple) -->
    <div *ngIf="loading" class="flex justify-center mb-4">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
    </div>

    <div class="bg-white dark:bg-surface-dark border border-slate-200 dark:border-slate-700/50 rounded-xl shadow-sm overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-200 dark:divide-slate-700/50">
          <thead class="bg-slate-50 dark:bg-background-dark/50">
            <tr>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Data/Hora</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Material</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Tipo</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Quantidade</th>
              <th class="px-6 py-4 text-left text-xs font-semibold text-slate-500 dark:text-slate-400 uppercase tracking-wider">Observação</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-200 dark:divide-slate-700/50">
            <tr *ngFor="let m of movimentacoes" class="hover:bg-slate-50 dark:hover:bg-surface-dark-lighter transition-colors">
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-700 dark:text-slate-300">
                {{ m.dataHora | date:'dd/MM/yyyy HH:mm' }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-primary">
                {{ m.materialNome }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span [class]="m.tipo === 'ENTRADA' ? 'text-green-600 bg-green-100 dark:bg-green-900/30 px-2 py-1 rounded text-xs font-bold' : 'text-red-600 bg-red-100 dark:bg-red-900/30 px-2 py-1 rounded text-xs font-bold'">
                  {{ m.tipo }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-slate-900 dark:text-white font-mono">
                {{ m.quantidade }}
              </td>
              <td class="px-6 py-4 text-sm text-slate-500 dark:text-slate-400 truncate max-w-xs">
                {{ m.observacao || '-' }}
              </td>
            </tr>
             <tr *ngIf="!loading && movimentacoes.length === 0">
              <td colspan="5" class="px-6 py-12 text-center text-slate-500 dark:text-slate-400">
                 Nenhuma movimentação registrada.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
       <!-- Pagination (Simple Previous/Next for MVP) -->
      <div class="bg-white dark:bg-surface-dark px-4 py-3 flex items-center justify-between border-t border-slate-200 dark:border-slate-700/50 sm:px-6">
        <div class="flex-1 flex justify-between sm:justify-end gap-3">
          <button (click)="loadMovimentacoes(page - 1)" [disabled]="page === 0 || loading" class="relative inline-flex items-center px-4 py-2 border border-slate-300 dark:border-slate-600 text-sm font-medium rounded-md text-slate-700 dark:text-slate-200 bg-white dark:bg-surface-dark hover:bg-slate-50 dark:hover:bg-surface-dark-lighter disabled:opacity-50">
            Anterior
          </button>
          <button (click)="loadMovimentacoes(page + 1)" [disabled]="page >= totalPages - 1 || loading" class="relative inline-flex items-center px-4 py-2 border border-slate-300 dark:border-slate-600 text-sm font-medium rounded-md text-slate-700 dark:text-slate-200 bg-white dark:bg-surface-dark hover:bg-slate-50 dark:hover:bg-surface-dark-lighter disabled:opacity-50">
            Próxima
          </button>
        </div>
      </div>

    </div>
  `
})
export class MovementsComponent implements OnInit, OnDestroy {
  movimentacoes: Movimentacao[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  loading = false;
  errorMessage: string | null = null;
  private destroy$ = new Subject<void>();

  constructor(private service: MaterialService) { }

  ngOnInit() {
    this.loadMovimentacoes(0);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadMovimentacoes(page: number) {
    if (page < 0) return;

    this.loading = true;
    this.errorMessage = null;

    this.service.listarMovimentacoes(page, this.size)
      .pipe(
        takeUntil(this.destroy$),
        catchError(error => {
          this.errorMessage = 'Erro ao carregar movimentações. Tente novamente mais tarde.';
          console.error('Erro ao carregar movimentações:', error);
          return of(null);
        }),
        finalize(() => this.loading = false)
      )
      .subscribe(res => {
        if (res) {
          this.movimentacoes = res.content;
          this.totalPages = res.totalPages;
          this.page = res.number;
        }
      });
  }
}
