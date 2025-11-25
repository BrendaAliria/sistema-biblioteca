package com.seuprojeto.biblioteca.test;

import com.seuprojeto.biblioteca.model.Livro;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para a Entidade Livro")
class LivroTest {

    private final String ID_VALIDO = "ISBN-1234";
    private final String TITULO_VALIDO = "O Senhor dos Anéis";
    private final String AUTOR_VALIDO = "J.R.R. Tolkien";
    private final int ANO_VALIDO = 1954;

    @Test
    @DisplayName("Teste de Construtor e Getters - Sucesso")
    void testConstrutorEGetters() {
        Livro livro = new Livro(ID_VALIDO, TITULO_VALIDO, AUTOR_VALIDO, ANO_VALIDO);
        
        assertNotNull(livro);
        assertEquals(ID_VALIDO, livro.getId());
        assertTrue(livro.isDisponivel());
    }
    
    @Test
    @DisplayName("Teste de Construtor - Dados Inválidos")
    void testConstrutorComDadosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Livro(null, TITULO_VALIDO, AUTOR_VALIDO, ANO_VALIDO),
                "Deve lançar IllegalArgumentException para ID nulo.");
    }

    @Test
    @DisplayName("Teste de marcarComoEmprestado/Disponivel - Mudança de Status")
    void testMudancaStatusDisponibilidade() {
        Livro livro = new Livro(ID_VALIDO, TITULO_VALIDO, AUTOR_VALIDO, ANO_VALIDO);
        
        // Emprestado
        livro.marcarComoEmprestado();
        assertFalse(livro.isDisponivel());

        // Disponível
        livro.marcarComoDisponivel();
        assertTrue(livro.isDisponivel());
    }

    @Test
    @DisplayName("Teste de equals e hashCode - IDs Iguais vs Diferentes")
    void testEqualsEHashCode() {
        Livro livro1 = new Livro(ID_VALIDO, TITULO_VALIDO, AUTOR_VALIDO, ANO_VALIDO);
        Livro livro2 = new Livro(ID_VALIDO, "Outro Título", "Outro Autor", 2000); // Mesmo ID
        Livro livro3 = new Livro("ISBN-4321", "O Hobbit", "J.R.R. Tolkien", 1937); // ID Diferente

        assertTrue(livro1.equals(livro2), "Livros com o mesmo ID devem ser iguais.");
        assertEquals(livro1.hashCode(), livro2.hashCode(), "HashCodes de objetos iguais devem ser iguais.");
        assertFalse(livro1.equals(livro3), "Livros com IDs diferentes não devem ser iguais.");
    }
}
