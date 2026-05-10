/**
 * Classe abstrata PessoaBase — molde para todas as subclasses que representam
 * uma pessoa no sistema.
 *
 * <p>Conceitos praticados: <b>classe abstrata</b>, <b>método abstrato</b>,
 * <b>herança de atributos</b> (atributos {@code protected} são herdados pelas
 * subclasses) e início do <b>encapsulamento</b> (getters concretos).</p>
 *
 * <p>Por ser abstrata, esta classe NÃO pode ser instanciada diretamente
 * (ex.: {@code new PessoaBase(...)} não compila). Ela serve apenas como base
 * comum de {@link Pessoa} e, por consequência, de {@link Atleta}.</p>
 */
public abstract class PessoaBase {

    /** Nome da pessoa — protegido para ser visível às subclasses. */
    protected String nome;

    /** Idade em anos completos — protegido para ser visível às subclasses. */
    protected int idade;

    /**
     * Construtor base — recebe os atributos comuns a qualquer pessoa.
     *
     * @param nome  nome completo
     * @param idade idade em anos
     */
    public PessoaBase(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    /**
     * Método ABSTRATO — toda subclasse concreta DEVE implementar.
     * Cada subclasse decide como exibir seu próprio perfil.
     *
     * @return descrição textual do perfil da pessoa
     */
    public abstract String exibirPerfil();

    // ----- Métodos concretos herdados pelas subclasses (reuso) -----

    /** @return nome da pessoa. */
    public String getNome() {
        return nome;
    }

    /** @return idade da pessoa. */
    public int getIdade() {
        return idade;
    }
}
