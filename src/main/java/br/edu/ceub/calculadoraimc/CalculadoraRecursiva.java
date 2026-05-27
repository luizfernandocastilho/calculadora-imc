package br.edu.ceub.calculadoraimc;

public final class CalculadoraRecursiva {

    private CalculadoraRecursiva() {
        throw new AssertionError(
            "CalculadoraRecursiva é uma classe utilitária — não instancie."
        );
    }

    public static double potencia(final double base, final int exp) {
        if (exp == 0) {
            return 1.0;
        }
        if (exp < 0) {
            return 1.0 / potencia(base, -exp);
        }
        return base * potencia(base, exp - 1);
    }

    public static double arredondarDuasCasas(final double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public static void linhaSeparadora() {
        System.out.println("=".repeat(50));
    }
}
