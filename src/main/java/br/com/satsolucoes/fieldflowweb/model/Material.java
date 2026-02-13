package br.com.satsolucoes.fieldflowweb.model;

import br.com.satsolucoes.fieldflowweb.exception.EstoqueInsuficienteException;
import br.com.satsolucoes.fieldflowweb.exception.QuantidadeInvalidaException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal quantidadeDisponivel;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String unidadeMedida = "UN";

    public Material() {
    }

    public Material(String nome, BigDecimal quantidadeDisponivel, String descricao, String unidadeMedida) {
        this.nome = nome;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.descricao = descricao;
        this.unidadeMedida = unidadeMedida;
    }

    private void validarQuantidadePositiva(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new QuantidadeInvalidaException("Quantidade deve ser positiva.");
        }
    }

    public void debitarEstoque(BigDecimal quantidade) {
        validarQuantidadePositiva(quantidade);
        if (this.quantidadeDisponivel.compareTo(quantidade) < 0) {
            throw new EstoqueInsuficienteException("Saldo atual: " + this.quantidadeDisponivel);
        }
        this.quantidadeDisponivel = this.quantidadeDisponivel.subtract(quantidade);
    }

    public void creditarEstoque(BigDecimal quantidade) {
        validarQuantidadePositiva(quantidade);
        this.quantidadeDisponivel = this.quantidadeDisponivel.add(quantidade);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(BigDecimal quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


