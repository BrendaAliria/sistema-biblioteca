package com.seuprojeto.biblioteca.test;

import com.seuprojeto.biblioteca.model.Livro;
import com.seuprojeto.biblioteca.model.Membro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a Entidade Membro")
class MembroTest {

    private Membro membro;
    private Livro livro1;

    @BeforeEach
    void setUp() {
        membro = new Membro("MEM-001", "Alice Silva");
        livro1 = new Livro("ISBN-101", "Livro Teste 1", "Autor X", 2020);
    }

    @Test
    @DisplayName("Teste de Construtor e Getters - Lista Vazia Inicial")
    void testConstrutorEGetters() {
        assertEquals("MEM-001", membro.getId());
        assertTrue(membro.getLivrosEmprestados().isEmpty());
    }

    @Test
    @DisplayName("Teste de emprestarLivro - Sucesso")
    void testEmprestarLivro_Sucesso() {
        membro.emprestarLivro(livro1);
        assertEquals(1, membro.getLivrosEmprestados().size());
        assertTrue(membro.getLivrosEmprestados().contains(livro1));
    }

    @Test
    @DisplayName("Teste de emprestarLivro - Livro já na lista (Exceção)")
    void testEmprestarLivro_LivroDuplicado() {
        membro.emprestarLivro(livro1);
        assertThrows(IllegalArgumentException.class, () -> membro.emprestarLivro(livro1),
                "Deve lançar exceção se o livro já estiver na lista.");
    }

    @Test
    @DisplayName("Teste de devolverLivro - Sucesso")
    void testDevolverLivro_Sucesso() {
        membro.emprestarLivro(livro1);
        membro.devolverLivro(livro1);
        assertTrue(membro.getLivrosEmprestados().isEmpty());
    }

    @Test
    @DisplayName("Teste de devolverLivro - Livro não emprestado (Exceção)")
    void testDevolverLivro_LivroNaoEmprestado() {
        assertThrows(IllegalArgumentException.class, () -> membro.devolverLivro(livro1),
                "Deve lançar exceção se o livro não estiver na lista.");
    }
    
    @Test
    @DisplayName("Teste de getLivrosEmprestados - Lista imutável")
    void testGetLivrosEmprestados_Imutabilidade() {
        membro.emprestarLivro(livro1);
        // Tenta modificar a lista retornada, deve lançar UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> membro.getLivrosEmprestados().clear());
    }
}
