package br.edu.ceub.calculadoraimc;

import java.util.Objects;

/**
 * Orquestrador do sistema: calcula, classifica, registra e exibe IMCs.
 *
 * <h2>Composição vs Herança</h2>
 * <p>Esta classe é o exemplo canônico da pergunta "herança ou composição?".
 * Poderíamos imaginar {@code SistemaIMC extends Historico} para reaproveitar
 * o código de armazenamento — mas seria <strong>errado</strong>:
 * {@code SistemaIMC} <em>não é</em> um histórico, ele <em>tem</em> um.
 * Essa confusão entre "é-um" e "tem-um" é a origem de muitos designs
 * ruins em POO.</p>
 *
 * <p>A regra prática (Gang of Four, "Design Patterns"):
 * <strong>"prefira composição a herança"</strong>. Composição:</p>
 * <ul>
 *   <li>É mais flexível: trocar a implementação não requer mudar a hierarquia.</li>
 *   <li>Evita herança frágil: classe-base não vaza detalhes para subclasses.</li>
 *   <li>Permite múltiplas relações: um sistema pode ter histórico, log,
 *       cache, validador — todos juntos.</li>
 * </ul>
 *
 * <h2>Polimorfismo no método {@link #processar(Pessoa)}</h2>
 * <p>O parâmetro é declarado como {@link Pessoa}, mas pode receber qualquer
 * subclasse (incluindo {@link Atleta}). Quando chamamos
 * {@code pessoa.classificarIMC(imc)}, a JVM escolhe a implementação correta
 * em runtime baseada no <strong>tipo real</strong> do objeto, não no tipo
 * da referência. Isso é o que viabiliza o sistema funcionar com qualquer
 * tipo futuro de pessoa sem precisar mexer aqui.</p>
 *
 * <h2>Dependency Injection</h2>
 * <p>Oferecemos dois construtores: o padrão (cria seu próprio {@code Historico})
 * e um que recebe o {@code Historico} de fora. O segundo é útil para testes
 * — permite injetar um histórico fake/spy/mock. <strong>Effective Java,
 * item 5</strong>: prefira injeção de dependências sobre instanciação interna.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class SistemaIMC {

    /**
     * Histórico interno — relação de COMPOSIÇÃO ("tem-um").
     * {@code final} porque a referência ao histórico não muda durante a
     * vida do sistema; apenas seu conteúdo evolui.
     */
    private final Historico historico;

    /**
     * Construtor padrão: cria um novo {@link Historico} próprio. Útil para
     * uso direto em código de produção.
     */
    public SistemaIMC() {
        this(new Historico());
    }

    /**
     * Construtor com injeção de dependência: recebe um {@link Historico}
     * pronto (útil para testes ou para compartilhar histórico entre
     * sistemas).
     *
     * @param historico histórico a ser usado pelo sistema (não-nulo)
     * @throws NullPointerException se {@code historico} for {@code null}
     */
    public SistemaIMC(final Historico historico) {
        // Objects.requireNonNull retorna o próprio valor se ok, senão lança
        // NullPointerException com mensagem clara.
        this.historico = Objects.requireNonNull(
            historico, "Historico não pode ser nulo."
        );
    }

    /**
     * Calcula o IMC de uma pessoa, classifica, registra no histórico e
     * imprime no console.
     *
     * <p>Despacho polimórfico acontece em duas chamadas:</p>
     * <ul>
     *   <li>{@code pessoa.classificarIMC(imc)} — Pessoa ou Atleta?</li>
     *   <li>{@code pessoa.exibirPerfil()} — formato comum ou com modalidade?</li>
     * </ul>
     * <p>Em ambos, esta classe não precisa saber o tipo real — confia na
     * abstração.</p>
     *
     * @param pessoa pessoa a processar (pode ser {@link Atleta})
     * @throws EntradaInvalidaException se {@code pessoa} for {@code null}
     */
    public void processar(final Pessoa pessoa) {
        if (pessoa == null) {
            throw new EntradaInvalidaException(
                "Não há pessoa cadastrada para calcular IMC."
            );
        }

        // Pegamos peso/altura via getters (encapsulamento) e calculamos.
        final double imc      = pessoa.calcularIMC(pessoa.getPeso(), pessoa.getAltura());
        // Aqui o polimorfismo despacha a implementação correta:
        final String classe   = pessoa.classificarIMC(imc);
        // Função utilitária NÃO recursiva, demonstrando o segundo método de
        // CalculadoraRecursiva.
        final double imcRound = CalculadoraRecursiva.arredondarDuasCasas(imc);

        // String.format aplica o locale padrão — em pt-BR, vírgula decimal.
        final String linha = String.format(
            "%s → IMC: %.2f (%s)",
            pessoa.getNome(), imcRound, classe
        );

        historico.adicionar(linha);

        System.out.println();
        System.out.println(pessoa.exibirPerfil()); // toString delega para isso também
        System.out.println("Resultado: " + linha);
        System.out.println();
    }

    /** Delega ao histórico interno (Lei de Demeter mínima: um nível). */
    public void exibirHistorico() {
        historico.exibir();
    }

    /** @return total de cálculos realizados na sessão */
    public int totalCalculos() {
        return historico.tamanho();
    }

    @Override
    public String toString() {
        return "SistemaIMC[" + totalCalculos() + " cálculo(s) processado(s)]";
    }
}
