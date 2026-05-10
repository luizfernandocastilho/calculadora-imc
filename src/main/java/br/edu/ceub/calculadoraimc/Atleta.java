package br.edu.ceub.calculadoraimc;

/**
 * Especialização de {@link Pessoa} com critérios de classificação de IMC
 * adaptados a esportistas.
 *
 * <h2>Herança multinível</h2>
 * <p>Esta classe completa uma cadeia de três níveis:
 * {@code Atleta → Pessoa → PessoaBase}. Cada nível adiciona algo novo:</p>
 * <ul>
 *   <li>{@link PessoaBase}: identidade básica (nome, idade)</li>
 *   <li>{@link Pessoa}: corpo (peso, altura) e capacidade de calcular IMC</li>
 *   <li>{@link Atleta}: modalidade esportiva e classificação adaptada</li>
 * </ul>
 *
 * <h2>Princípios SOLID em ação</h2>
 * <ul>
 *   <li><strong>OCP — Open/Closed Principle</strong>: estamos
 *       <em>estendendo</em> {@link Pessoa} sem modificá-la. Pessoa fica
 *       fechada para alteração mas aberta para extensão. Isso evita
 *       efeitos colaterais em código já estável.</li>
 *   <li><strong>LSP — Liskov Substitution Principle</strong>: em qualquer
 *       lugar que o sistema espera uma {@code Pessoa}, podemos passar um
 *       {@code Atleta} sem quebrar nada. {@link SistemaIMC#processar(Pessoa)}
 *       depende disso para funcionar polimorficamente.</li>
 * </ul>
 *
 * <h2>Por que faixas diferentes?</h2>
 * <p>Atletas tendem a ter percentual maior de massa magra (músculo), que
 * pesa mais por unidade de volume que gordura. Um IMC de 26 num corredor de
 * elite normalmente <em>não</em> indica sobrepeso — indica massa muscular.
 * Por isso esta classe sobrescreve {@link #classificarIMC(double)} com
 * faixas diferentes.</p>
 *
 * <p>Aviso: estas faixas são uma <em>simplificação didática</em>. Avaliação
 * esportiva real usa percentual de gordura, bioimpedância, dobras cutâneas
 * — métodos que diferenciam massa magra de gordura corporal.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class Atleta extends Pessoa {

    // ============================================================
    //          CONSTANTES — Faixas de IMC para atletas
    // ============================================================

    /** Limite abaixo do qual o IMC é considerado insuficiente para um atleta. */
    public static final double LIMITE_ABAIXO_IDEAL = 20.0;

    /** Limite superior (exclusivo) da faixa "Ideal para atleta". */
    public static final double LIMITE_IDEAL        = 27.0;

    // ============================================================
    //                       ATRIBUTOS
    // ============================================================

    /**
     * Modalidade esportiva (ex.: "Natação", "Musculação"). {@code final}
     * porque a modalidade é parte da identidade do atleta no momento do
     * cadastro — se quiser mudar, prefira criar um novo objeto, evitando
     * estado mutável desnecessário.
     */
    private final String modalidade;

    // ============================================================
    //                      CONSTRUTOR
    // ============================================================

    /**
     * Cria um {@code Atleta} delegando todos os atributos comuns ao
     * construtor de {@link Pessoa} via {@code super(...)}.
     *
     * @param nome       nome (validado em {@link PessoaBase})
     * @param idade      idade (validada em {@link PessoaBase})
     * @param peso       peso em kg (validado em {@link Pessoa})
     * @param altura     altura em metros (validada em {@link Pessoa})
     * @param modalidade modalidade esportiva (não-nula, não-em-branco)
     * @throws EntradaInvalidaException se qualquer argumento for inválido
     */
    public Atleta(final String nome, final int idade,
                  final double peso, final double altura,
                  final String modalidade) {
        super(nome, idade, peso, altura); // sobe a cadeia até PessoaBase
        if (modalidade == null || modalidade.isBlank()) {
            throw new EntradaInvalidaException(
                "Modalidade esportiva é obrigatória."
            );
        }
        this.modalidade = modalidade.trim();
    }

    // ============================================================
    //                       GETTERS
    // ============================================================

    /** @return modalidade esportiva (nunca {@code null}, nunca em branco) */
    public String getModalidade() {
        return modalidade;
    }

    // ============================================================
    //                  POLIMORFISMO EM AÇÃO
    // ============================================================

    /**
     * Classifica o IMC com faixas adaptadas para esportistas.
     *
     * <p>Esta sobrescrita é a essência do <strong>polimorfismo</strong>:
     * o mesmo método {@code classificarIMC} chamado numa referência
     * {@code Pessoa} retorna resultados diferentes dependendo do tipo
     * <em>real</em> do objeto em runtime — comportamento que a JVM
     * resolve via <em>despacho dinâmico</em> (também conhecido como
     * <em>late binding</em>).</p>
     *
     * <p>A anotação {@code @Override} não é meramente decorativa: ela
     * pede ao compilador para verificar se realmente estamos sobrescrevendo
     * um método existente. Se errarmos a assinatura (digamos,
     * {@code clasificarIMC} com 's' a menos), o compilador acusa em vez de
     * silenciosamente criar um método novo. Effective Java, item 40.</p>
     */
    @Override
    public String classificarIMC(final double imc) {
        if (imc < LIMITE_ABAIXO_IDEAL) return "Abaixo do ideal para atleta";
        if (imc < LIMITE_IDEAL)        return "Ideal para atleta";
        return "Acima do ideal para atleta";
    }

    /**
     * Sobrescreve o perfil reaproveitando o resultado da superclasse.
     *
     * <p>Note o uso de {@code super.exibirPerfil()} — não duplicamos a
     * lógica de formatação. Se {@link Pessoa#exibirPerfil()} mudar no
     * futuro, esta classe automaticamente herda a mudança.</p>
     */
    @Override
    public String exibirPerfil() {
        return super.exibirPerfil() + " | Modalidade: " + modalidade + " (atleta)";
    }
}
