import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suíte de testes que valida a presença e o comportamento de cada
 * conceito de POO exigido pelo barema.
 */
class CalculadoraIMCTest {

    private static final double DELTA = 0.001;

    // ======================================================
    //                       RECURSÃO
    // ======================================================

    @Test
    @DisplayName("Recursão: caso-base de potência (exp = 0) retorna 1")
    void potenciaCasoBase() {
        assertEquals(1.0, CalculadoraRecursiva.potencia(2.0, 0), DELTA);
        assertEquals(1.0, CalculadoraRecursiva.potencia(1.75, 0), DELTA);
    }

    @Test
    @DisplayName("Recursão: 1.75² ≈ 3.0625 (passo recursivo correto)")
    void potenciaPassoRecursivo() {
        assertEquals(3.0625, CalculadoraRecursiva.potencia(1.75, 2), DELTA);
        assertEquals(8.0,    CalculadoraRecursiva.potencia(2.0, 3),  DELTA);
        assertEquals(81.0,   CalculadoraRecursiva.potencia(3.0, 4),  DELTA);
    }

    // ======================================================
    //                  CÁLCULO DO IMC
    // ======================================================

    @Test
    @DisplayName("IMC do exemplo do enunciado: 70 kg / 1.75 m ≈ 22.86")
    void imcDoExemplo() {
        Pessoa p = new Pessoa("Maria", 25, 70.0, 1.75);
        double imc = p.calcularIMC(p.getPeso(), p.getAltura());
        assertEquals(22.857, imc, 0.01);
    }

    // ======================================================
    //   POLIMORFISMO — Pessoa vs Atleta classificam diferente
    // ======================================================

    @Test
    @DisplayName("Polimorfismo: Pessoa e Atleta classificam o mesmo IMC de formas diferentes")
    void polimorfismoNaClassificacao() {
        Pessoa pessoa = new Pessoa("Joana", 30, 60.0, 1.65);
        Atleta atleta = new Atleta("Bruno", 28, 60.0, 1.65, "Corrida");

        // Caso 1 — IMC 19: Pessoa diz "Peso normal" / Atleta diz "Abaixo do ideal"
        assertEquals("Peso normal",                  pessoa.classificarIMC(19.0));
        assertEquals("Abaixo do ideal para atleta",  atleta.classificarIMC(19.0));

        // Caso 2 — IMC 26: Pessoa diz "Sobrepeso" / Atleta diz "Ideal para atleta"
        assertEquals("Sobrepeso",            pessoa.classificarIMC(26.0));
        assertEquals("Ideal para atleta",    atleta.classificarIMC(26.0));
    }

    @Test
    @DisplayName("Polimorfismo de referência: variável Pessoa pode segurar Atleta e despacha o método correto")
    void polimorfismoDeReferencia() {
        Pessoa ref = new Atleta("Carla", 27, 65.0, 1.70, "Natação");
        // A variável é declarada como Pessoa, mas o objeto é Atleta —
        // a JVM despacha a versão de Atleta.classificarIMC em runtime.
        assertEquals("Ideal para atleta", ref.classificarIMC(22.5));
    }

    // ======================================================
    //   CLASSIFICAÇÃO OMS — Pessoa cobre todas as faixas
    // ======================================================

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

    // ======================================================
    //   ENCAPSULAMENTO — setter valida e lança exceção
    // ======================================================

    @Test
    @DisplayName("Encapsulamento: setter rejeita peso não positivo")
    void encapsulamentoSetterPesoInvalido() {
        Pessoa p = new Pessoa("Y", 30, 70, 1.75);
        assertThrows(EntradaInvalidaException.class, () -> p.setPeso(0));
        assertThrows(EntradaInvalidaException.class, () -> p.setPeso(-10));
    }

    @Test
    @DisplayName("Encapsulamento: construtor já valida via setter")
    void encapsulamentoConstrutorValida() {
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("Z", 25, -5.0, 1.70));
        assertThrows(EntradaInvalidaException.class,
                     () -> new Pessoa("Z", 25, 70.0, 0));
    }

    // ======================================================
    //                    HERANÇA MULTINÍVEL
    // ======================================================

    @Test
    @DisplayName("Herança multinível: Atleta herda de Pessoa que herda de PessoaBase")
    void herancaMultinivel() {
        Atleta atleta = new Atleta("Diego", 25, 80.0, 1.85, "Vôlei");
        // Atleta IS-A Pessoa
        assertTrue(atleta instanceof Pessoa);
        // Atleta IS-A PessoaBase (pelo encadeamento)
        assertTrue(atleta instanceof PessoaBase);
        // Reutiliza getter de PessoaBase
        assertEquals("Diego", atleta.getNome());
        assertEquals(25, atleta.getIdade());
        // E ainda tem acesso ao próprio
        assertEquals("Vôlei", atleta.getModalidade());
    }

    // ======================================================
    //                    COMPOSIÇÃO
    // ======================================================

    @Test
    @DisplayName("Composição: SistemaIMC TEM-UM Historico que registra cálculos")
    void composicaoRegistraHistorico() {
        SistemaIMC sistema = new SistemaIMC();
        assertEquals(0, sistema.totalCalculos());

        sistema.processar(new Pessoa("Ana",   30, 60.0, 1.65));
        sistema.processar(new Atleta("Pedro", 28, 80.0, 1.90, "Basquete"));

        assertEquals(2, sistema.totalCalculos());
    }

    // ======================================================
    //              EXCEÇÃO PERSONALIZADA
    // ======================================================

    @Test
    @DisplayName("EntradaInvalidaException herda de RuntimeException")
    void excecaoPersonalizadaHerdaRuntime() {
        EntradaInvalidaException e = new EntradaInvalidaException("teste");
        assertTrue(e instanceof RuntimeException);
        assertEquals("teste", e.getMessage());
    }

    @Test
    @DisplayName("processar() lança EntradaInvalidaException quando recebe null")
    void processarNullLancaExcecao() {
        SistemaIMC sistema = new SistemaIMC();
        assertThrows(EntradaInvalidaException.class, () -> sistema.processar(null));
    }
}
