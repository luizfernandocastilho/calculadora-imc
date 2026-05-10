package br.edu.ceub.calculadoraimc;

/**
 * Contrato para qualquer entidade capaz de calcular e classificar um IMC.
 *
 * <h2>Por que uma interface (e não uma classe concreta)?</h2>
 * <p>Trabalhar contra uma <em>abstração</em> em vez de uma implementação
 * concreta é o que permite o <strong>polimorfismo</strong> e o
 * <strong>baixo acoplamento</strong> do sistema. Quem usa esta interface
 * (ex.: {@link SistemaIMC}) não precisa saber se está lidando com uma
 * {@link Pessoa} comum ou com um {@link Atleta} — basta cumprir o contrato.</p>
 *
 * <h2>Princípios SOLID em ação</h2>
 * <ul>
 *   <li><strong>ISP — Interface Segregation Principle</strong>: a interface
 *       é pequena e coesa. Tem apenas dois métodos diretamente relacionados
 *       (calcular e classificar). Em vez de um "deus-objeto" com 20 métodos,
 *       contratos enxutos forçam clientes a depender só do que precisam.</li>
 *   <li><strong>DIP — Dependency Inversion Principle</strong>: módulos de
 *       alto nível ({@link SistemaIMC}) dependem desta abstração, não das
 *       classes concretas. Trocar a implementação não exige mudar o
 *       orquestrador.</li>
 * </ul>
 *
 * <h2>Decisões de design</h2>
 * <p>Optamos por retornar a classificação como {@code String} para alinhar
 * com o exemplo do enunciado e priorizar clareza didática. Em código de
 * produção, um {@code enum ClassificacaoIMC} seria mais robusto (evita typos,
 * permite {@code switch} exaustivo, internacionalização). Fica como
 * sugestão de evolução.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 * @see Pessoa
 * @see Atleta
 */
public interface CalculadoraIMC {

    /**
     * Calcula o valor numérico do Índice de Massa Corporal.
     *
     * <p>Fórmula: {@code IMC = peso / (altura²)}.</p>
     *
     * @param peso   peso corporal em <strong>quilogramas</strong>; deve ser positivo
     * @param altura altura em <strong>metros</strong>; deve ser positiva
     * @return o IMC calculado, sem unidade (kg/m²)
     * @throws EntradaInvalidaException se peso ou altura forem ≤ 0
     */
    double calcularIMC(double peso, double altura);

    /**
     * Classifica textualmente um valor de IMC segundo as faixas adotadas
     * pela implementação.
     *
     * <p>Implementações distintas podem usar faixas distintas — é
     * justamente nesse método que o <strong>polimorfismo</strong> se
     * manifesta. Veja {@link Pessoa#classificarIMC(double)} (faixas da OMS)
     * e {@link Atleta#classificarIMC(double)} (faixas para esportistas).</p>
     *
     * @param imc valor numérico do IMC, geralmente obtido de
     *            {@link #calcularIMC(double, double)}
     * @return descrição textual da faixa (ex.: {@code "Peso normal"})
     */
    String classificarIMC(double imc);
}
