import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Material, Page } from '../models/material.model';
import { Movimentacao } from '../models/movimentacao.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MaterialService {

  private apiUrl = `${environment.apiUrl}/materiais`;
  private apiMovimentacoesUrl = `${environment.apiUrl}/movimentacoes`;

  constructor(@Inject(HttpClient) private http: HttpClient) { }

  // --- Materiais ---

  listar(): Observable<Material[]> {
    return this.http.get<Material[]>(this.apiUrl);
  }

  listarPaginado(page: number, size: number): Observable<Page<Material>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Material>>(`${this.apiUrl}/paginado`, { params });
  }

  buscarPorNome(nome: string, page: number, size: number): Observable<Page<Material>> {
    const params = new HttpParams()
      .set('nome', nome)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Material>>(`${this.apiUrl}/buscar`, { params });
  }

  filtrarPorUnidade(unidade: string, page: number, size: number): Observable<Page<Material>> {
    const params = new HttpParams()
      .set('unidadeMedida', unidade)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Material>>(`${this.apiUrl}/filtro/unidade-medida`, { params });
  }

  buscarPorId(id: number): Observable<Material> {
    return this.http.get<Material>(`${this.apiUrl}/${id}`);
  }

  salvar(material: Material): Observable<Material> {
    return this.http.post<Material>(this.apiUrl, material);
  }

  atualizar(id: number, material: Material): Observable<Material> {
    return this.http.put<Material>(`${this.apiUrl}/${id}`, material);
  }

  // --- Movimentações ---

  registrarMovimentacao(movimentacao: Movimentacao): Observable<Movimentacao> {
    return this.http.post<Movimentacao>(this.apiMovimentacoesUrl, movimentacao);
  }

  listarMovimentacoes(page: number, size: number): Observable<Page<Movimentacao>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Movimentacao>>(this.apiMovimentacoesUrl, { params });
  }
}
