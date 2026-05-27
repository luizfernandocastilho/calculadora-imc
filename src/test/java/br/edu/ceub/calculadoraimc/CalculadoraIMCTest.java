package br.edu.ceub.calculadoraimc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraIMCTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Recursão: caso-base de potência (exp = 0) retorna 1")
    void potenciaCasoBase() {
        assertEquals(1.0, CalculadoraRecursiva.potencia(2.0, 0), DELTA);
        assertEquals(1.0, CalculadoraRecursiva.potencia(1.75, 0), DELTA);
    }

    @Test
    @DisplayName("Recursão: passo recursivo correto para diferentes bases e expoentes")
    void potenciaPassoRecursivo() {
        assertEquals(3.0625, CalculadoraRecursiva.potencia(1.75, 2), DELTA);
        assertEquals(8.0,    CalculadoraRecursiva.potencia(2.0, 3),  DELTA);
        assertEquals(81.0,   CalculadoraRecursiva.potencia(3.0, 4),  DELTA);
    }

    @Test
    @DisplayName("Recursão: aceita expoentes negativos (x^-n = 1/x^n)")
    void potenciaExpoenteNegativo() {
        assertEquals(0.125, CalculadoraRecursiva.potencia(2.0, -3), DELTA);
        assertEquals(0.25,  CalculadoraRecursiva.potencia(2.0, -2), DELTA);
    }

    @Test
    @DisplayName("Classe utilitária: construtor privado impede instanciação via reflexão")
    void utilitariaNaoInstanciavel() {
        assertThrows(Exception.class, () -> {
            var ctor = CalculadoraRecursiva.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
        });
    }

    @Test
    @DisplayName("IMC do exemplo do enunciado: 70 kg / 1.75 m ≈ 22.86")
    void imcDoExemplo() {
        Pessoa p = new Pessoa("Maria", 25, 70.0, 1.75);
        double imc = p.calcularIMC(p.getPeso(), p.getAltura());
        assertEquals(22.857, imc, 0.01);
    }

    @Test
    @DisplayName("Polimorfismo: Pessoa e Atleta classificam o mesmo IMC de formas diferentes")
    void polimorfismoNaClassificacao() {
        Pessoa pessoa = new Pessoa("Joana", 30, 60.0, 1.65);
        Atleta atleta = new Atleta("Bruno", 28, 60.0, 1.65, "Corrida");

        assertEquals("Peso normal",                 pessoa.classificarIMC(19.0));
        assertEquals("Abaixo do ideal para atleta", atleta.classificarIMC(19.0));

        assertEquals("Sobrepeso",         pessoa.classificarIMC(26.0));
        assertEquals("Ideal para atleta", atleta.classificarIMC(26.0));
    }

    @Test
    @DisplayName("Polimorfismo de referência: variável Pessoa pode segurar Atleta")
    void polimorfismoDeReferencia() {
        Pessoa ref = new Atleta("Carla", 27, 65.0, 1.70, "Natação");
        assertEquals("Ideal para atleta", ref.classificarIMC(22.5));
    }

    @Test
    @DisplayName("Classificação OMS cobre todas as faixas")
    void classificacaoOMSCompleta() {
        Pessoa p = new Pessoa("X", 30, 70, 1.75);
        assertEquals("Abaixo do peso",               p.classificarIMC(17.0));
        assertEquals("Peso normal",                  p.classificarIMC(22.0));
        assertEquals("Sobrepeso",                    p.classificarIMC(27.0));
        assertEquals("Obesidade grau I",             p.classificarIMC(32.0));
        assertEquals("Obesidade grau II",            p.classificarIMC(37.0));
        assertEquals("Obesidade grau III (mórbida)", p.classificarIMC(45.0));
    }

    @Test
    @DisplayName("Encapsulamento: setter rejeita peso não positivo")
    void encapsulamentoSetterPesoInvalido() {
        Pessoa p = new Pessoa("Y", 30, 70, 1.75);
        assertThrows(EntradaInvalidaException.class, () -> p.setPeso(0));
        assertThrows(EntradaInvalidaException.class, () -> p.setPeso(-10));
    }

    @Test
    @DisplayName("Encapsulamento: setter rejeita altura fora do intervalo permitido")
    void encapsulamentoSetterAlturaInvalida() {
        Pessoa p = new Pessoa("Y", 30, 70, 1.75);
        assertThrows(EntradaInvalidaException.class, () -> p.setAltura(0));
        assertThrows(EntradaInvalidaException.class, () -> p.setAltura(-1.5));
        assertThrows(EntradaInvalidaException.class, () -> p.setAltura(3.5));
    }

    @Test
    @DisplayName("Encapsulamento: construtor já valida via setter")
    void encapsulamentoConstrutorValida() {
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("Z", 25, -5.0, 1.70));
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("Z", 25, 70.0, 0));
    }

    @Test
    @DisplayName("Validação fail-fast: nome nulo ou em branco rejeitado")
    void validacaoNome() {
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa(null, 25, 70, 1.75));
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("   ", 25, 70, 1.75));
    }

    @Test
    @DisplayName("Validação fail-fast: idade fora da faixa válida rejeitada")
    void validacaoIdade() {
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("X", -1, 70, 1.75));
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("X", 200, 70, 1.75));
    }

    @Test
    @DisplayName("Validação fail-fast: modalidade obrigatória para Atleta")
    void validacaoModalidade() {
        assertThrows(EntradaInvalidaException.class,
                     () -> new Atleta("X", 25, 70, 1.75, null));
        assertThrows(EntradaInvalidaException.class,
                     () -> new Atleta("X", 25, 70, 1.75, "  "));
    }

    @Test
    @DisplayName("Herança multinível: Atleta É-UMA Pessoa que É-UMA PessoaBase")
    void herancaMultinivel() {
        Atleta atleta = new Atleta("Diego", 25, 80.0, 1.85, "Vôlei");
        assertTrue(atleta instanceof Pessoa);
        assertTrue(atleta instanceof PessoaBase);
        assertEquals("Diego", atleta.getNome());
        assertEquals(25, atleta.getIdade());
        assertEquals("Vôlei", atleta.getModalidade());
    }

    @Test
    @DisplayName("Composição: SistemaIMC TEM-UM Historico que registra cálculos")
    void composicaoRegistraHistorico() {
        SistemaIMC sistema = new SistemaIMC();
        assertEquals(0, sistema.totalCalculos());

        sistema.processar(new Pessoa("Ana", 30, 60.0, 1.65));
        sistema.processar(new Atleta("Pedro", 28, 80.0, 1.90, "Basquete"));

        assertEquals(2, sistema.totalCalculos());
    }

    @Test
    @DisplayName("Encapsulamento defensivo: getRegistros() retorna lista imutável")
    void historicoImutavel() {
        Historico h = new Historico();
        h.adicionar("teste");
        List<String> registros = h.getRegistros();
        assertThrows(UnsupportedOperationException.class,
                     () -> registros.add("hack"));
    }

    @Test
    @DisplayName("Composição com DI: SistemaIMC aceita Historico injetado")
    void sistemaComHistoricoInjetado() {
        Historico hCompartilhado = new Historico();
        SistemaIMC s1 = new SistemaIMC(hCompartilhado);
        s1.processar(new Pessoa("Foo", 30, 70, 1.75));
        assertEquals(1, hCompartilhado.tamanho());
    }

    @Test
    @DisplayName("EntradaInvalidaException herda de RuntimeException")
    void excecaoPersonalizadaHerdaRuntime() {
        EntradaInvalidaException e = new EntradaInvalidaException("teste");
        assertTrue(e instanceof RuntimeException);
        assertEquals("teste", e.getMessage());
    }

    @Test
    @DisplayName("EntradaInvalidaException preserva causa raiz quando encadeada")
    void excecaoPreservaCausa() {
        Throwable causa = new IllegalArgumentException("raiz");
        EntradaInvalidaException e = new EntradaInvalidaException("agrupada", causa);
        assertSame(causa, e.getCause());
    }

    @Test
    @DisplayName("processar() lança EntradaInvalidaException quando recebe null")
    void processarNullLancaExcecao() {
        SistemaIMC sistema = new SistemaIMC();
        assertThrows(EntradaInvalidaException.class,
                     () -> sistema.processar(null));
    }

    @Test
    @DisplayName("toString() da Pessoa delega para exibirPerfil()")
    void toStringDelegaParaPerfil() {
        Pessoa p = new Pessoa("Maria", 25, 70, 1.75);
        assertEquals(p.exibirPerfil(), p.toString());
    }

    @Test
    @DisplayName("equals/hashCode baseados em nome+idade respeitam contrato")
    void equalsEHashCode() {
        Pessoa p1 = new Pessoa("João", 30, 70, 1.75);
        Pessoa p2 = new Pessoa("João", 30, 80, 1.80);
        Pessoa p3 = new Pessoa("Maria", 30, 70, 1.75);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }
}
