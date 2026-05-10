package br.edu.ceub.calculadoraimc;

/**
 * Coleção de funções matemáticas estáticas — entre elas, uma função
 * recursiva para calcular potências.
 *
 * <h2>Por que classe utilitária com construtor privado?</h2>
 * <p>Esta classe não tem estado: não faz sentido instanciá-la
 * ({@code new CalculadoraRecursiva()} não daria nada de útil). Para impedir
 * o uso indevido:</p>
 * <ul>
 *   <li>Marcamos a classe como {@code final} — ninguém estende.</li>
 *   <li>Tornamos o construtor {@code private} — ninguém instancia.</li>
 *   <li>Lançamos {@link AssertionError} no corpo do construtor — defesa
 *       contra reflexão (que pode burlar o {@code private}).</li>
 * </ul>
 * <p><strong>Effective Java, item 4</strong>: imponha não-instanciabilidade
 * com construtor privado.</p>
 *
 * <h2>Recursão</h2>
 * <p>A função {@link #potencia(double, int)} é o exemplo clássico de
 * recursão. Seu sucesso depende de dois ingredientes obrigatórios:</p>
 * <ol>
 *   <li><strong>Caso-base</strong>: condição que encerra a recursão sem
 *       chamar a si mesma. Aqui, {@code exp == 0 → 1}.</li>
 *   <li><strong>Passo recursivo</strong>: chamada que aproxima o problema
 *       do caso-base. Aqui, decrementamos {@code exp} a cada chamada.</li>
 * </ol>
 * <p>Sem caso-base, recursão vira loop infinito → {@link StackOverflowError}.</p>
 *
 * <h2>Análise de complexidade</h2>
 * <p>Esta implementação é <strong>O(n)</strong> em tempo e espaço (uma
 * chamada de pilha por unidade de expoente). Para expoentes grandes, a
 * versão idiomática de produção seria <em>exponenciação por quadrados</em>,
 * que reduz para <strong>O(log n)</strong>. Para o caso de uso real do
 * IMC ({@code potencia(altura, 2)}), a versão linear é mais que suficiente
 * e mais didática.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public final class CalculadoraRecursiva {

    /**
     * Construtor privado intencionalmente — esta classe é utilitária e
     * não deve ser instanciada. Lançar uma exceção dentro garante que
     * mesmo via reflexão a instanciação falhe.
     */
    private CalculadoraRecursiva() {
        throw new AssertionError(
            "CalculadoraRecursiva é uma classe utilitária — não instancie."
        );
    }

    /**
     * Calcula {@code base^exp} usando RECURSÃO, sem usar {@code Math.pow()}.
     *
     * <pre>
     *   Caso-base       : exp == 0  → retorna 1
     *   Passo recursivo : base * potencia(base, exp - 1)
     *   Exp negativo    : 1 / potencia(base, -exp)
     * </pre>
     *
     * @param base número-base (qualquer valor real)
     * @param exp  expoente inteiro (pode ser negativo, zero ou positivo)
     * @return {@code base} elevado a {@code exp}
     */
    public static double potencia(final double base, final int exp) {
        // Caso-base: matematicamente, qualquer número elevado a 0 é 1.
        // Esse retorno fecha a recursão.
        if (exp == 0) {
            return 1.0;
        }
        // Suporte opcional a expoentes negativos: x^-n = 1 / x^n.
        if (exp < 0) {
            return 1.0 / potencia(base, -exp);
        }
        // Passo recursivo: a função chama a si mesma com exp menor em 1.
        // Cada chamada empilha um quadro na pilha de execução.
        return base * potencia(base, exp - 1);
    }

    /**
     * Função simples (não recursiva) — arredonda um {@code double} para
     * 2 casas decimais usando a técnica clássica de "multiplica, arredonda,
     * divide".
     *
     * <p>Existe para demonstrar uma função utilitária <em>sem</em> recursão,
     * complementando {@link #potencia(double, int)}.</p>
     *
     * @param valor valor a arredondar
     * @return valor com no máximo duas casas decimais (arredondado para o
     *         inteiro mais próximo, com regra "half up")
     */
    public static double arredondarDuasCasas(final double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    /** Função utilitária para imprimir uma linha separadora padrão. */
    public static void linhaSeparadora() {
        System.out.println("=".repeat(50));
    }
}
