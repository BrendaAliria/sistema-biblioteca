package com.seuprojeto.biblioteca.test;

import com.seuprojeto.biblioteca.model.Livro;
import com.seuprojeto.biblioteca.model.Membro;
import com.seuprojeto.biblioteca.service.Biblioteca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a Classe de Serviço Biblioteca")
class BibliotecaTest {

    private Biblioteca biblioteca;
    private Livro livro1; 
    private Livro livro2; 
    private Membro membro1;
    private Membro membro2;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca();

        livro1 = new Livro("ISBN-L1", "A Arte da Guerra", "Sun Tzu", 500);
        livro2 = new Livro("ISBN-L2", "1984", "George Orwell", 1949);

        membro1 = new Membro("M-100", "Carlos Dantas");
        membro2 = new Membro("M-200", "Mariana Lima");

        biblioteca.adicionarLivro(livro1);
        biblioteca.adicionarLivro(livro2);
        biblioteca.registrarMembro(membro1);
        biblioteca.registrarMembro(membro2);
    }

    // --- Testes de Adicionar/Remover Livro ---
    @Test
    @DisplayName("Adicionar um livro com sucesso e ID duplicado (Exceção)")
    void testAdicionarLivro() {
        Livro livro3 = new Livro("ISBN-L3", "Clean Code", "R.C. Martin", 2008);
        biblioteca.adicionarLivro(livro3);
        
        assertNotNull(biblioteca.buscarLivroPorId("ISBN-L3"));
        assertEquals(3, biblioteca.getCatalogoLivros().size());

        // ID duplicado
        Livro livroDuplicado = new Livro("ISBN-L1", "Outro", "Autor", 2000);
        assertThrows(IllegalArgumentException.class, () -> biblioteca.adicionarLivro(livroDuplicado));
    }

    @Test
    @DisplayName("Remover livro com sucesso e emprestado (Exceção)")
    void testRemoverLivro() {
        // Remover sucesso
        biblioteca.removerLivro(livro1.getId());
        assertNull(biblioteca.buscarLivroPorId(livro1.getId()));

        // Tenta remover livro emprestado
        biblioteca.emprestarLivro(livro2.getId(), membro1.getId());
        assertThrows(IllegalArgumentException.class, () -> biblioteca.removerLivro(livro2.getId()));
        
        // Tenta remover livro que não existe
        assertThrows(IllegalArgumentException.class, () -> biblioteca.removerLivro("ISBN-999"));
    }

    // --- Testes de Registrar/Remover Membro ---
    @Test
    @DisplayName("Registrar membro com sucesso e ID duplicado (Exceção)")
    void testRegistrarMembro() {
        Membro membro3 = new Membro("M-300", "Diana");
        biblioteca.registrarMembro(membro3);
        
        assertNotNull(biblioteca.buscarMembroPorId("M-300"));

        // ID duplicado
        Membro membroDuplicado = new Membro("M-100", "Outro Nome");
        assertThrows(IllegalArgumentException.class, () -> biblioteca.registrarMembro(membroDuplicado));
    }

    @Test
    @DisplayName("Remover membro com sucesso e com livros emprestados (Exceção)")
    void testRemoverMembro() {
        // Remover sucesso
        biblioteca.removerMembro(membro2.getId());
        assertNull(biblioteca.buscarMembroPorId(membro2.getId()));

        // Tenta remover membro com livros
        biblioteca.emprestarLivro(livro1.getId(), membro1.getId());
        assertThrows(IllegalArgumentException.class, () -> biblioteca.removerMembro(membro1.getId()));

        // Tenta remover membro que não existe
        assertThrows(IllegalArgumentException.class, () -> biblioteca.removerMembro("M-999"));
    }

    // --- Testes de Empréstimo ---
    @Test
    @DisplayName("Empréstimo com sucesso e tentativa em livro indisponível (Exceção)")
    void testEmprestarLivro() {
        // Sucesso
        biblioteca.emprestarLivro(livro1.getId(), membro1.getId());
        assertFalse(livro1.isDisponivel());
        assertTrue(membro1.getLivrosEmprestados().contains(livro1));

        // Tentar emprestar livro indisponível
        assertThrows(IllegalStateException.class, () -> biblioteca.emprestarLivro(livro1.getId(), membro2.getId()));

        // Tentar emprestar livro ou membro inexistente
        assertThrows(IllegalArgumentException.class, () -> biblioteca.emprestarLivro("ISBN-999", membro1.getId()));
        assertThrows(IllegalArgumentException.class, () -> biblioteca.emprestarLivro(livro2.getId(), "M-999"));
    }

    // --- Testes de Devolução ---
    @Test
    @DisplayName("Devolução com sucesso e casos de falha (Exceções)")
    void testDevolverLivro() {
        // Setup: Empréstimo inicial
        biblioteca.emprestarLivro(livro1.getId(), membro1.getId());

        // Sucesso
        biblioteca.devolverLivro(livro1.getId(), membro1.getId());
        assertTrue(livro1.isDisponivel());
        assertFalse(membro1.getLivrosEmprestados().contains(livro1));

        // Tentar devolver livro não emprestado (livro2)
        assertThrows(IllegalStateException.class, () -> biblioteca.devolverLivro(livro2.getId(), membro2.getId()),
                "Deve falhar: Livro não estava emprestado.");

        // Tentar devolver livro, mas pelo membro errado 
        biblioteca.emprestarLivro(livro2.getId(), membro2.getId());
        assertThrows(IllegalArgumentException.class, () -> biblioteca.devolverLivro(livro2.getId(), membro1.getId()),
                "Deve falhar: Livro não está na lista do membro1.");
        
        // Tentar devolver livro ou membro inexistente
        assertThrows(IllegalArgumentException.class, () -> biblioteca.devolverLivro("ISBN-999", membro1.getId()));
        assertThrows(IllegalArgumentException.class, () -> biblioteca.devolverLivro(livro2.getId(), "M-999"));
    }

    // --- Testes de Listagem ---
    @Test
    @DisplayName("Verificar listagens de livros disponíveis e emprestados")
    void testListagens() {
        // Inicialmente 2 disponíveis
        assertEquals(2, biblioteca.listarLivrosDisponiveis().size());
        
        // Emprestar Livro 1
        biblioteca.emprestarLivro(livro1.getId(), membro1.getId());
        
        // Agora 1 disponível
        assertEquals(1, biblioteca.listarLivrosDisponiveis().size());
        
        // Membro 1 tem 1 livro emprestado
        List<Livro> emprestadosMembro1 = biblioteca.listarLivrosEmprestadosPorMembro(membro1.getId());
        assertEquals(1, emprestadosMembro1.size());
        assertTrue(emprestadosMembro1.contains(livro1));

        // Membro inexistente na listagem
        assertThrows(IllegalArgumentException.class, () -> biblioteca.listarLivrosEmprestadosPorMembro("M-999"));
    }
}
