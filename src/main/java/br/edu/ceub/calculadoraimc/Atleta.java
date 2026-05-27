package br.edu.ceub.calculadoraimc;

public class Atleta extends Pessoa {

    public static final double LIMITE_ABAIXO_IDEAL = 20.0;

    public static final double LIMITE_IDEAL        = 27.0;

    private final String modalidade;

    public Atleta(final String nome, final int idade,
                  final double peso, final double altura,
                  final String modalidade) {
        super(nome, idade, peso, altura);
        if (modalidade == null || modalidade.isBlank()) {
            throw new EntradaInvalidaException(
                "Modalidade esportiva é obrigatória."
            );
        }
        this.modalidade = modalidade.trim();
    }

    public String getModalidade() {
        return modalidade;
    }

    @Override
    public String classificarIMC(final double imc) {
        if (imc < LIMITE_ABAIXO_IDEAL) return "Abaixo do ideal para atleta";
        if (imc < LIMITE_IDEAL)        return "Ideal para atleta";
        return "Acima do ideal para atleta";
    }

    @Override
    public String exibirPerfil() {
        return super.exibirPerfil() + " | Modalidade: " + modalidade + " (atleta)";
    }
}
