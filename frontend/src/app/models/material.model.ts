export interface Material {
    id?: number;
    nome: string;
    codigoUnico?: string;
    quantidadeDisponivel: number;
    categoria?: string;
    descricao?: string;
    unidadeMedida: string;
}

export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
}
