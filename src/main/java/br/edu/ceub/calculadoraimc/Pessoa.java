package br.edu.ceub.calculadoraimc;

/**
 * Representa uma pessoa comum (não-atleta) e sua capacidade de calcular
 * e classificar o próprio IMC.
 *
 * <h2>Três conceitos numa só classe</h2>
 * <ul>
 *   <li><strong>Herança</strong> — estende {@link PessoaBase} via {@code extends},
 *       herdando atributos {@code nome}/{@code idade} e os getters concretos.</li>
 *   <li><strong>Implementação de interface</strong> — cumpre o contrato
 *       {@link CalculadoraIMC} via {@code implements}.</li>
 *   <li><strong>Encapsulamento</strong> — atributos {@code private},
 *       acessados via getters/setters, com <em>validação</em> nos setters.</li>
 * </ul>
 *
 * <h2>Por que herança E interface ao mesmo tempo?</h2>
 * <p>Java permite herdar de <strong>uma única classe</strong>, mas
 * <strong>múltiplas interfaces</strong>. Estendemos {@link PessoaBase} para
 * reaproveitar estado e comportamento, e implementamos {@link CalculadoraIMC}
 * para participar do polimorfismo do sistema. Os dois mecanismos resolvem
 * problemas diferentes:</p>
 * <ul>
 *   <li>Herança = "{@code Pessoa} <strong>é uma</strong> {@code PessoaBase}"</li>
 *   <li>Interface = "{@code Pessoa} <strong>é capaz de</strong> calcular IMC"</li>
 * </ul>
 *
 * <h2>Faixas da OMS</h2>
 * <p>Os limites usados em {@link #classificarIMC(double)} seguem as faixas
 * adotadas pela Organização Mundial da Saúde para população adulta em geral.
 * São extraídos como constantes para evitar <em>magic numbers</em>
 * (Effective Java, item 22) e tornar a manutenção mais segura.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class Pessoa extends PessoaBase implements CalculadoraIMC {

    // ============================================================
    //          CONSTANTES — Faixas de IMC pela OMS
    // ============================================================
    // Boa prática: nomes descritivos em vez de números soltos no código.
    // 'static' = uma cópia por classe (não por instância).
    // 'final'  = nunca muda em runtime (constante de verdade).

    /** Limite superior (exclusivo) da faixa "Abaixo do peso". */
    public static final double LIMITE_ABAIXO_PESO  = 18.5;
    /** Limite superior (exclusivo) da faixa "Peso normal". */
    public static final double LIMITE_PESO_NORMAL  = 25.0;
    /** Limite superior (exclusivo) da faixa "Sobrepeso". */
    public static final double LIMITE_SOBREPESO    = 30.0;
    /** Limite superior (exclusivo) da faixa "Obesidade grau I". */
    public static final double LIMITE_OBESIDADE_I  = 35.0;
    /** Limite superior (exclusivo) da faixa "Obesidade grau II". */
    public static final double LIMITE_OBESIDADE_II = 40.0;

    /**
     * Altura máxima razoável em metros. Usada para validação de
     * <em>sanity check</em> — o recordista mundial documentado teve 2,72 m,
     * então qualquer valor acima de 3 metros indica erro de digitação.
     */
    private static final double ALTURA_MAXIMA = 3.0;

    // ============================================================
    //                  ATRIBUTOS PRIVADOS
    // ============================================================
    // ENCAPSULAMENTO: o mundo externo NÃO acessa estes campos diretamente,
    // só via getters/setters que aplicam regras de negócio (validação).
    // Esse é o ponto-chave do encapsulamento: separar o "o que" do "como".

    private double  peso;    // em quilogramas
    private double  altura;  // em metros
    private boolean ativo;   // marca se a pessoa ainda está cadastrada

    // ============================================================
    //                      CONSTRUTOR
    // ============================================================

    /**
     * Cria uma {@code Pessoa} com todos os dados necessários, validados.
     *
     * <p><strong>Detalhe de design:</strong> reutilizamos os setters
     * {@link #setPeso(double)} e {@link #setAltura(double)} dentro do
     * construtor. Isso centraliza a regra de validação num único lugar
     * — alterando o setter, tanto novas instâncias quanto modificações
     * posteriores passam a respeitar a mesma regra. Os setters são
     * {@code final} para que subclasses não possam quebrar essa garantia
     * com sobrescrita inesperada.</p>
     *
     * @param nome   nome (não-nulo, não-em-branco)
     * @param idade  idade em anos (faixa válida em {@link PessoaBase})
     * @param peso   peso em kg (positivo)
     * @param altura altura em metros (entre 0 e {@value #ALTURA_MAXIMA})
     * @throws EntradaInvalidaException se qualquer argumento for inválido
     */
    public Pessoa(final String nome, final int idade,
                  final double peso, final double altura) {
        super(nome, idade); // delega ao construtor de PessoaBase
        setPeso(peso);
        setAltura(altura);
        this.ativo = true;
    }

    // ============================================================
    //                       GETTERS
    // ============================================================

    /** @return peso atual em kg */
    public double getPeso()    { return peso; }

    /** @return altura atual em metros */
    public double getAltura()  { return altura; }

    /** @return {@code true} se a pessoa está marcada como ativa */
    public boolean isAtivo()   { return ativo; }

    // ============================================================
    //              SETTERS COM VALIDAÇÃO
    // ============================================================
    // Marcados 'final' para que subclasses (ex.: Atleta) não possam
    // sobrescrever e remover a validação. Princípio: "manter as
    // invariantes da classe é responsabilidade da classe".

    /**
     * Define o peso. Lança exceção se o valor for ≤ 0.
     *
     * @throws EntradaInvalidaException se {@code peso} ≤ 0
     */
    public final void setPeso(final double peso) {
        if (peso <= 0) {
            throw new EntradaInvalidaException(
                "Peso deve ser positivo (recebido: " + peso + " kg)."
            );
        }
        this.peso = peso;
    }

    /**
     * Define a altura. Aceita apenas valores em (0, {@value #ALTURA_MAXIMA}].
     *
     * @throws EntradaInvalidaException se a altura for não-positiva ou
     *         maior que {@value #ALTURA_MAXIMA} m
     */
    public final void setAltura(final double altura) {
        if (altura <= 0 || altura > ALTURA_MAXIMA) {
            throw new EntradaInvalidaException(
                "Altura deve estar entre 0 e " + ALTURA_MAXIMA
                + " m (recebido: " + altura + ")."
            );
        }
        this.altura = altura;
    }

    /** Marca a pessoa como ativa ou inativa. */
    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    // ============================================================
    //         IMPLEMENTAÇÃO DA INTERFACE CalculadoraIMC
    // ============================================================

    /**
     * Calcula o IMC pela fórmula clássica.
     *
     * <p>Em vez de usar {@code altura * altura} ou {@code Math.pow(altura, 2)},
     * delegamos para {@link CalculadoraRecursiva#potencia(double, int)} —
     * isso satisfaz o requisito do enunciado de demonstrar recursão e
     * <em>de fato</em> usa a função recursiva no fluxo principal do programa.</p>
     */
    @Override
    public double calcularIMC(final double peso, final double altura) {
        return peso / CalculadoraRecursiva.potencia(altura, 2);
    }

    /**
     * Classifica o IMC nas faixas adotadas pela OMS para população adulta
     * em geral. Subclasses podem (e em alguns casos devem) sobrescrever.
     *
     * <p>Nota didática: esta cadeia de {@code if/else if} é correta, mas em
     * código de produção a forma mais robusta seria um {@code enum} de
     * faixas com método de busca, eliminando a chance de retornar uma
     * faixa errada por digitar mal um literal de string.</p>
     */
    @Override
    public String classificarIMC(final double imc) {
        if (imc < LIMITE_ABAIXO_PESO)   return "Abaixo do peso";
        if (imc < LIMITE_PESO_NORMAL)   return "Peso normal";
        if (imc < LIMITE_SOBREPESO)     return "Sobrepeso";
        if (imc < LIMITE_OBESIDADE_I)   return "Obesidade grau I";
        if (imc < LIMITE_OBESIDADE_II)  return "Obesidade grau II";
        return "Obesidade grau III (mórbida)";
    }

    // ============================================================
    //         IMPLEMENTAÇÃO DO MÉTODO ABSTRATO HERDADO
    // ============================================================

    /**
     * Retorna o perfil formatado da pessoa. {@code String.format} com
     * locale padrão da JVM — em ambientes pt-BR, decimais saem com
     * vírgula automaticamente.
     */
    @Override
    public String exibirPerfil() {
        return String.format(
            "Pessoa: %s | Idade: %d | Peso: %.2f kg | Altura: %.2f m",
            nome, idade, peso, altura
        );
    }
}
