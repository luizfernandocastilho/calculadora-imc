/**
 * Interface CalculadoraIMC — define o contrato que toda classe capaz de
 * calcular e classificar IMC deve cumprir.
 *
 * <p>Conceito praticado: <b>Interface</b> (contrato sem implementação).
 * Métodos declarados aqui são <i>abstratos por padrão</i> — quem implementa
 * esta interface é obrigado a fornecer o corpo deles.</p>
 *
 * <p>Desacoplamento: o resto do sistema depende deste contrato, não de uma
 * classe concreta. Isso permite trocar implementações sem mudar quem usa.</p>
 */
public interface CalculadoraIMC {

    /**
     * Calcula o valor numérico do IMC.
     *
     * @param peso   peso em quilogramas (deve ser positivo)
     * @param altura altura em metros (deve ser positiva)
     * @return o IMC calculado pela fórmula peso / (altura²)
     */
    double calcularIMC(double peso, double altura);

    /**
     * Classifica textualmente um valor de IMC.
     *
     * @param imc valor numérico do IMC
     * @return descrição da faixa correspondente (ex.: "Peso normal")
     */
    String classificarIMC(double imc);
}
