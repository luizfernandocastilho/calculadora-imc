/**
 * EntradaInvalidaException — exceção personalizada lançada quando uma
 * entrada do usuário não atende às regras de validação do sistema.
 *
 * <p>Herda de {@link RuntimeException} (exceção <i>não-verificada</i>) por
 * decisão de design: erros de entrada são <i>condições recuperáveis</i> que
 * preferimos tratar com {@code try/catch} pontuais nos pontos de leitura,
 * sem poluir cada assinatura de método com {@code throws}.</p>
 *
 * <p><b>Quando é lançada:</b></p>
 * <ul>
 *   <li>Peso ou altura digitados como zero ou negativos.</li>
 *   <li>Valor não numérico digitado em campos numéricos.</li>
 *   <li>Opção de menu fora do intervalo permitido.</li>
 *   <li>Tentativa de calcular IMC sem pessoa cadastrada.</li>
 * </ul>
 */
public class EntradaInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Construtor padrão — recebe uma mensagem que explica o erro ao usuário.
     *
     * @param mensagem texto que será exibido pelo bloco {@code catch}
     */
    public EntradaInvalidaException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor que aceita causa raiz — útil para encadear exceções,
     * preservando o stack trace original (ex.: {@code InputMismatchException}).
     *
     * @param mensagem texto explicativo
     * @param causa    exceção original capturada
     */
    public EntradaInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
