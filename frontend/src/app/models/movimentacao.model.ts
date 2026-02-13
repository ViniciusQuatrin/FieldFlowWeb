export interface Movimentacao {
    id?: number;
    tipo: 'ENTRADA' | 'SAIDA';
    quantidade: number;
    dataHora?: string;
    observacao: string;
    materialId: number;
    materialNome?: string;
}
