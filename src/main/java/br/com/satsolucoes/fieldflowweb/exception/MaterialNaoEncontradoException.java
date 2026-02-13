package br.com.satsolucoes.fieldflowweb.exception;

public class MaterialNaoEncontradoException extends RuntimeException {
    public MaterialNaoEncontradoException(Long id) {
        super("Material não encontrado com ID: " + id);
    }
}

