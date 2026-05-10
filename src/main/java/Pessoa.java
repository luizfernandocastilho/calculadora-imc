/**
 * Pessoa — representa uma pessoa comum (não-atleta) no sistema.
 *
 * <p>Demonstra três conceitos ao mesmo tempo:</p>
 * <ul>
 *   <li><b>Herança</b>: estende {@link PessoaBase}, herdando {@code nome} e {@code idade}.</li>
 *   <li><b>Implementação de interface</b>: cumpre o contrato {@link CalculadoraIMC}.</li>
 *   <li><b>Encapsulamento</b>: atributos {@code private} acessíveis apenas via
 *       getters/setters, com <b>validação no setter</b>.</li>
 * </ul>
 *
 * <p>A classificação aqui usa as faixas oficiais da <b>OMS</b> para a população
 * em geral. Subclasses (como {@link Atleta}) podem sobrescrever para usar
 * critérios diferentes — é aí que o polimorfismo entra.</p>
 */
public class Pessoa extends PessoaBase implements CalculadoraIMC {

    // Atributos PRIVADOS — encapsulamento. Ninguém de fora acessa direto.
    private double peso;
    private double altura;
    private boolean ativo;

    /**
     * Construtor — repassa nome e idade à superclasse e inicializa os
     * atributos próprios desta classe.
     *
     * @param nome   nome da pessoa
     * @param idade  idade em anos
     * @param peso   peso em kg (deve ser positivo)
     * @param altura altura em metros (deve ser positiva)
     */
    public Pessoa(String nome, int idade, double peso, double altura) {
        super(nome, idade); // delega à superclasse
        // Reaproveita o setter para garantir validação já no construtor
        setPeso(peso);
        setAltura(altura);
        this.ativo = true;
    }

    // ----- Getters: leitura controlada -----

    public double getPeso()    { return peso; }
    public double getAltura()  { return altura; }
    public boolean isAtivo()   { return ativo; }

    // ----- Setters com validação: blindam o estado interno -----

    /**
     * Define o peso com validação. Lança {@link EntradaInvalidaException}
     * se o valor não for positivo.
     */
    public final void setPeso(double peso) {
        if (peso <= 0) {
            throw new EntradaInvalidaException("Peso deve ser positivo (recebido: " + peso + ")");
        }
        this.peso = peso;
    }

    /**
     * Define a altura com validação. Lança {@link EntradaInvalidaException}
     * se o valor não for positivo. Acima de 3,0 m também é rejeitado por
     * sanidade (recordista mundial tinha 2,72 m).
     */
    public final void setAltura(double altura) {
        if (altura <= 0 || altura > 3.0) {
            throw new EntradaInvalidaException("Altura deve estar entre 0 e 3,0 m (recebido: " + altura + ")");
        }
        this.altura = altura;
    }

    /** Permite marcar a pessoa como inativa (sem remover do sistema). */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // ----- Implementação do contrato CalculadoraIMC -----

    /**
     * Calcula o IMC pela fórmula clássica: peso / (altura²).
     * Usa {@link CalculadoraRecursiva#potencia(double, int)} para demonstrar
     * o uso da função recursiva exigida pelo enunciado.
     */
    @Override
    public double calcularIMC(double peso, double altura) {
        // Operadores aritméticos + chamada à função recursiva
        return peso / CalculadoraRecursiva.potencia(altura, 2);
    }

    /**
     * Classificação seguindo as faixas da OMS para a população em geral.
     * Subclasses podem (e devem, quando fizer sentido) sobrescrever.
     */
    @Override
    public String classificarIMC(double imc) {
        // Operadores relacionais + lógicos + condicional encadeada
        if (imc < 18.5)       return "Abaixo do peso";
        else if (imc < 25.0)  return "Peso normal";
        else if (imc < 30.0)  return "Sobrepeso";
        else if (imc < 35.0)  return "Obesidade grau I";
        else if (imc < 40.0)  return "Obesidade grau II";
        else                  return "Obesidade grau III (mórbida)";
    }

    // ----- Implementação do método abstrato herdado -----

    @Override
    public String exibirPerfil() {
        return String.format(
            "Pessoa: %s | Idade: %d | Peso: %.2f kg | Altura: %.2f m",
            nome, idade, peso, altura
        );
    }
}
