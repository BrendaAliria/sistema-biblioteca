package com.seuprojeto.biblioteca.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Membro {
    private final String id;
    private String nome;
    private final List<Livro> livrosEmprestados;

    public Membro(String id, String nome) {
        if (id == null || id.trim().isEmpty() || nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Dados de Membro inválidos no construtor.");
        }
        this.id = id;
        this.nome = nome;
        this.livrosEmprestados = new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }

    public List<Livro> getLivrosEmprestados() {
        return Collections.unmodifiableList(livrosEmprestados);
    }

    // Métodos de Ação
    public void emprestarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("O livro não pode ser nulo.");
        }
        if (livrosEmprestados.contains(livro)) {
            throw new IllegalArgumentException("O livro com ID " + livro.getId() + " já está emprestado a este membro.");
        }
        livrosEmprestados.add(livro);
    }

    public void devolverLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("O livro não pode ser nulo.");
        }
        if (!livrosEmprestados.contains(livro)) {
            throw new IllegalArgumentException("O livro com ID " + livro.getId() + " não está na lista de livros emprestados deste membro.");
        }
        livrosEmprestados.remove(livro);
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membro membro = (Membro) o;
        return Objects.equals(id, membro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
