package br.edu.ceub.calculadoraimc;

public class Pessoa extends PessoaBase implements CalculadoraIMC {

    public static final double LIMITE_ABAIXO_PESO  = 18.5;
    public static final double LIMITE_PESO_NORMAL  = 25.0;
    public static final double LIMITE_SOBREPESO    = 30.0;
    public static final double LIMITE_OBESIDADE_I  = 35.0;
    public static final double LIMITE_OBESIDADE_II = 40.0;

    private static final double ALTURA_MAXIMA = 3.0;

    private double  peso;
    private double  altura;
    private boolean ativo;

    public Pessoa(final String nome, final int idade,
                  final double peso, final double altura) {
        super(nome, idade);
        setPeso(peso);
        setAltura(altura);
        this.ativo = true;
    }

    public double getPeso()    { return peso; }

    public double getAltura()  { return altura; }

    public boolean isAtivo()   { return ativo; }

    public final void setPeso(final double peso) {
        if (peso <= 0) {
            throw new EntradaInvalidaException(
                "Peso deve ser positivo (recebido: " + peso + " kg)."
            );
        }
        this.peso = peso;
    }

    public final void setAltura(final double altura) {
        if (altura <= 0 || altura > ALTURA_MAXIMA) {
            throw new EntradaInvalidaException(
                "Altura deve estar entre 0 e " + ALTURA_MAXIMA
                + " m (recebido: " + altura + ")."
            );
        }
        this.altura = altura;
    }

    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public double calcularIMC(final double peso, final double altura) {
        return peso / CalculadoraRecursiva.potencia(altura, 2);
    }

    @Override
    public String classificarIMC(final double imc) {
        if (imc < LIMITE_ABAIXO_PESO)   return "Abaixo do peso";
        if (imc < LIMITE_PESO_NORMAL)   return "Peso normal";
        if (imc < LIMITE_SOBREPESO)     return "Sobrepeso";
        if (imc < LIMITE_OBESIDADE_I)   return "Obesidade grau I";
        if (imc < LIMITE_OBESIDADE_II)  return "Obesidade grau II";
        return "Obesidade grau III (mórbida)";
    }

    @Override
    public String exibirPerfil() {
        return String.format(
            "Pessoa: %s | Idade: %d | Peso: %.2f kg | Altura: %.2f m",
            nome, idade, peso, altura
        );
    }
}
