import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Material {
  id?: number;
  nome: string;
  codigoUnico?: string;
  quantidadeDisponivel: number;
  categoria?: string;
  descricao: string;
  unidadeMedida: string;
}

export interface Movimentacao {
  id?: number;
  tipo: 'ENTRADA' | 'SAIDA';
  quantidade: number;
  dataHora?: string;
  observacao: string;
  materialId: number;
  materialNome?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class MaterialService {

  private apiUrl = 'http://localhost:8080/api/materiais';
  private apiMovimentacoesUrl = 'http://localhost:8080/api/movimentacoes';

  constructor(private http: HttpClient) { }

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
