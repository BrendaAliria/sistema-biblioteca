package com.seuprojeto.biblioteca.service;

import com.seuprojeto.biblioteca.model.Livro;
import com.seuprojeto.biblioteca.model.Membro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de Serviço que gerencia o estado da Biblioteca e a lógica de negócio.
 */
public class Biblioteca {

    private final List<Livro> catalogoLivros;
    private final List<Membro> listaMembros;

    public Biblioteca() {
        this.catalogoLivros = new ArrayList<>();
        this.listaMembros = new ArrayList<>();
    }

    // --- Métodos de Busca (Auxiliares) ---
    public Livro buscarLivroPorId(String idLivro) {
        return catalogoLivros.stream()
                .filter(livro -> livro.getId().equals(idLivro))
                .findFirst()
                .orElse(null);
    }

    public Membro buscarMembroPorId(String idMembro) {
        return listaMembros.stream()
                .filter(membro -> membro.getId().equals(idMembro))
                .findFirst()
                .orElse(null);
    }

    // --- Gerenciamento de Livros ---
    public void adicionarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        if (buscarLivroPorId(livro.getId()) != null) {
            throw new IllegalArgumentException("Já existe um livro com o ID " + livro.getId() + " no catálogo.");
        }
        catalogoLivros.add(livro);
    }

    public void removerLivro(String idLivro) {
        Livro livro = buscarLivroPorId(idLivro);

        if (livro == null) {
            throw new IllegalArgumentException("Livro com ID " + idLivro + " não encontrado.");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalArgumentException("Livro com ID " + idLivro + " está emprestado e não pode ser removido.");
        }

        catalogoLivros.remove(livro);
    }

    // --- Gerenciamento de Membros ---
    public void registrarMembro(Membro membro) {
        if (membro == null) {
            throw new IllegalArgumentException("Membro não pode ser nulo.");
        }
        if (buscarMembroPorId(membro.getId()) != null) {
            throw new IllegalArgumentException("Já existe um membro com o ID " + membro.getId() + " registrado.");
        }
        listaMembros.add(membro);
    }

    public void removerMembro(String idMembro) {
        Membro membro = buscarMembroPorId(idMembro);

        if (membro == null) {
            throw new IllegalArgumentException("Membro com ID " + idMembro + " não encontrado.");
        }
        if (!membro.getLivrosEmprestados().isEmpty()) {
            throw new IllegalArgumentException("Membro com ID " + idMembro + " tem livros emprestados e não pode ser removido.");
        }

        listaMembros.remove(membro);
    }

    // --- Operações de Empréstimo ---
    public void emprestarLivro(String idLivro, String idMembro) {
        Livro livro = buscarLivroPorId(idLivro);
        Membro membro = buscarMembroPorId(idMembro);

        if (livro == null) {
            throw new IllegalArgumentException("Livro com ID " + idLivro + " não encontrado.");
        }
        if (membro == null) {
            throw new IllegalArgumentException("Membro com ID " + idMembro + " não encontrado.");
        }
        if (!livro.isDisponivel()) {
            throw new IllegalStateException("Livro com ID " + idLivro + " não está disponível para empréstimo.");
        }

        // Atualiza status e relaciona
        livro.marcarComoEmprestado();
        membro.emprestarLivro(livro);
    }

    public void devolverLivro(String idLivro, String idMembro) {
        Livro livro = buscarLivroPorId(idLivro);
        Membro membro = buscarMembroPorId(idMembro);

        if (livro == null) {
            throw new IllegalArgumentException("Livro com ID " + idLivro + " não encontrado.");
        }
        if (membro == null) {
            throw new IllegalArgumentException("Membro com ID " + idMembro + " não encontrado.");
        }

        if (livro.isDisponivel()) {
            throw new IllegalStateException("Livro com ID " + idLivro + " não estava emprestado.");
        }

        // O método do Membro verifica se o livro está na lista dele
        membro.devolverLivro(livro); 
        
        // Atualiza status
        livro.marcarComoDisponivel();
    }

    // --- Listagens ---
    public List<Livro> listarLivrosDisponiveis() {
        return catalogoLivros.stream()
                .filter(Livro::isDisponivel) 
                .collect(Collectors.toList());
    }

    public List<Livro> listarLivrosEmprestadosPorMembro(String idMembro) {
        Membro membro = buscarMembroPorId(idMembro);
        if (membro == null) {
            throw new IllegalArgumentException("Membro com ID " + idMembro + " não encontrado.");
        }
        return membro.getLivrosEmprestados();
    }
    
    // Getters para uso nos testes
    public List<Livro> getCatalogoLivros() {
        return Collections.unmodifiableList(catalogoLivros);
    }
    
    public List<Membro> getListaMembros() {
        return Collections.unmodifiableList(listaMembros);
    }
}
