import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MaterialService, Material } from '../../services/material.service';

@Component({
  selector: 'app-material-form',
  templateUrl: './material-form.component.html',
  styleUrls: ['./material-form.component.css']
})
export class MaterialFormComponent implements OnInit {

  material: Material = {
    nome: '',
    quantidadeDisponivel: 0,
    descricao: '',
    unidadeMedida: 'UN' // Default
  };

  isEditMode = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private materialService: MaterialService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.materialService.buscarPorId(+id).subscribe({
        next: (res) => this.material = res,
        error: (err) => this.errorMessage = 'Erro ao carregar material.'
      });
    }
  }

  save(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.material.nome) {
      this.errorMessage = 'Nome do material é obrigatório.';
      return;
    }

    const obs$ = this.isEditMode && this.material.id
      ? this.materialService.atualizar(this.material.id, this.material)
      : this.materialService.salvar(this.material);

    obs$.subscribe({
      next: () => {
        this.successMessage = 'Material salvo com sucesso!';
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Erro ao salvar material. Verifique os dados.';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/']);
  }

  // Helper for quantity stepper
  incrementQuantity(): void {
    this.material.quantidadeDisponivel++;
  }

  decrementQuantity(): void {
    if (this.material.quantidadeDisponivel > 0) {
      this.material.quantidadeDisponivel--;
    }
  }
}
