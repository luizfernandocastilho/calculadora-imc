package br.edu.ceub.calculadoraimc;

import java.util.Objects;

public class SistemaIMC {

    private final Historico historico;

    public SistemaIMC() {
        this(new Historico());
    }

    public SistemaIMC(final Historico historico) {
        this.historico = Objects.requireNonNull(
            historico, "Historico não pode ser nulo."
        );
    }

    public void processar(final Pessoa pessoa) {
        if (pessoa == null) {
            throw new EntradaInvalidaException(
                "Não há pessoa cadastrada para calcular IMC."
            );
        }

        final double imc      = pessoa.calcularIMC(pessoa.getPeso(), pessoa.getAltura());
        final String classe   = pessoa.classificarIMC(imc);
        final double imcRound = CalculadoraRecursiva.arredondarDuasCasas(imc);

        final String linha = String.format(
            "%s → IMC: %.2f (%s)",
            pessoa.getNome(), imcRound, classe
        );

        historico.adicionar(linha);

        System.out.println();
        System.out.println(pessoa.exibirPerfil());
        System.out.println("Resultado: " + linha);
        System.out.println();
    }

    public void exibirHistorico() {
        historico.exibir();
    }

    public int totalCalculos() {
        return historico.tamanho();
    }

    @Override
    public String toString() {
        return "SistemaIMC[" + totalCalculos() + " cálculo(s) processado(s)]";
    }
}
