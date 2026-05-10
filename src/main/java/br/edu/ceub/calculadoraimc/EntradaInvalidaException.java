package br.edu.ceub.calculadoraimc;

/**
 * Exceção lançada quando uma entrada do usuário viola as regras do sistema.
 *
 * <h2>Por que herdar de {@link RuntimeException} e não de {@link Exception}?</h2>
 * <p>Java distingue duas famílias de exceções:</p>
 * <ul>
 *   <li><strong>Verificadas (checked)</strong> — herdam de {@link Exception}
 *       (mas não de {@link RuntimeException}). O compilador <em>obriga</em>
 *       quem chama o método a tratar com {@code try/catch} ou declarar
 *       {@code throws}. Adequadas para condições recuperáveis em pontos
 *       específicos (ex.: arquivo não encontrado).</li>
 *   <li><strong>Não-verificadas (unchecked)</strong> — herdam de
 *       {@link RuntimeException}. O compilador não exige tratamento
 *       explícito. Adequadas para erros de programação ou condições que
 *       costumam ser tratadas em camadas superiores (ex.: argumento ilegal).</li>
 * </ul>
 *
 * <p>Escolhemos <em>unchecked</em> porque entradas inválidas podem ocorrer
 * em vários pontos do código (leitura de menu, leitura de números, validação
 * em setters), e não queremos poluir cada assinatura com {@code throws}.
 * O tratamento centraliza no laço principal de {@link Main}, que captura
 * e exibe a mensagem ao usuário sem encerrar o programa.</p>
 *
 * <h2>Boas práticas seguidas</h2>
 * <ul>
 *   <li><strong>Effective Java, item 72</strong>: prefira exceções padrão
 *       quando fizer sentido — usamos {@code RuntimeException} como pai.</li>
 *   <li><strong>Effective Java, item 73</strong>: lance exceções apropriadas
 *       à abstração — não vazamos {@code InputMismatchException} para o
 *       código de domínio; encapsulamos.</li>
 *   <li><strong>Effective Java, item 75</strong>: inclua na mensagem todas
 *       as informações de captura úteis — nossas mensagens dizem qual
 *       campo falhou e qual valor foi recebido.</li>
 *   <li><strong>{@code serialVersionUID}</strong>: declarado para silenciar
 *       o aviso do compilador sobre serializável e fixar a versão da classe
 *       caso algum dia seja persistida.</li>
 * </ul>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class EntradaInvalidaException extends RuntimeException {

    /**
     * Versão da classe para fins de serialização. Um valor fixo evita que
     * mudanças cosméticas (renomear um campo) quebrem deserialização de
     * exceções persistidas. Effective Java, item 87.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção apenas com mensagem.
     *
     * @param mensagem descrição clara do erro, exibida ao usuário
     */
    public EntradaInvalidaException(final String mensagem) {
        super(mensagem);
    }

    /**
     * Cria a exceção encadeando uma causa raiz. Útil quando reembrulhamos
     * uma exceção mais técnica (ex.: {@link NumberFormatException}) numa
     * mensagem amigável, sem perder o stack trace original.
     *
     * @param mensagem descrição clara do erro
     * @param causa    exceção original capturada
     */
    public EntradaInvalidaException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
