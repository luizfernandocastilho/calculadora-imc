/**
 * CalculadoraRecursiva — utilitário matemático com funções estáticas.
 *
 * <p>Centraliza duas demonstrações de <b>funções com responsabilidade única</b>:
 * uma <b>recursiva</b> ({@link #potencia(double, int)}) e uma simples não
 * recursiva ({@link #arredondarDuasCasas(double)}).</p>
 */
public final class CalculadoraRecursiva {

    /** Construtor privado: classe utilitária não deve ser instanciada. */
    private CalculadoraRecursiva() {
        throw new AssertionError("CalculadoraRecursiva é classe utilitária — não instancie.");
    }

    /**
     * Calcula {@code base^exp} usando RECURSÃO, sem usar {@code Math.pow()}.
     *
     * <pre>
     *   Caso-base:    exp == 0  →  retorna 1.
     *   Passo rec.:   base * potencia(base, exp - 1)
     *   Expoente neg: 1 / potencia(base, -exp)   (ex.: 2^-3 = 1/2^3)
     * </pre>
     *
     * @param base número de qualquer valor (incluindo decimais)
     * @param exp  expoente inteiro (positivo, zero ou negativo)
     * @return resultado de base elevado a exp
     */
    public static double potencia(double base, int exp) {
        // Caso-base: qualquer número elevado a 0 é 1.
        if (exp == 0) {
            return 1.0;
        }
        // Suporte opcional a expoentes negativos (não obrigatório, mas robusto).
        if (exp < 0) {
            return 1.0 / potencia(base, -exp);
        }
        // Passo recursivo: a função chama a si mesma com expoente decrementado.
        return base * potencia(base, exp - 1);
    }

    /**
     * Função simples (não recursiva) — arredonda um {@code double} para
     * 2 casas decimais. Existe para satisfazer o item do enunciado que pede
     * "também uma função não recursiva".
     *
     * @param valor valor original
     * @return valor arredondado a 2 casas decimais (meio-caso para cima)
     */
    public static double arredondarDuasCasas(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    /** Função simples que imprime uma linha separadora padronizada no console. */
    public static void linhaSeparadora() {
        System.out.println("=".repeat(50));
    }
}
