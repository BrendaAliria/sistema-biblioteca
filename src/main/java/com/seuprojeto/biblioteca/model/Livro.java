package com.seuprojeto.biblioteca.model;

import java.util.Objects;

/**
 * Classe que representa um Livro.
 */
public class Livro {
    private final String id; 
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private boolean disponivel;

    public Livro(String id, String titulo, String autor, int anoPublicacao) {
        if (id == null || id.trim().isEmpty() || titulo == null || titulo.trim().isEmpty() || autor == null || autor.trim().isEmpty() || anoPublicacao <= 0) {
            throw new IllegalArgumentException("Dados de Livro inválidos no construtor.");
        }
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.disponivel = true; 
    }

    // --- GETTERS (MÉTODO getId() ADICIONADO/VERIFICADO AQUI) ---
    public String getId() { return id; } // <-- ESTA LINHA É A SOLUÇÃO
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public boolean isDisponivel() { return disponivel; }

    // Métodos de Ação
    public void marcarComoEmprestado() {
        this.disponivel = false;
    }

    public void marcarComoDisponivel() {
        this.disponivel = true;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}