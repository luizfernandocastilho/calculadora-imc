package br.edu.ceub.calculadoraimc;

import java.util.Objects;

/**
 * Classe-base abstrata de qualquer pessoa no sistema.
 *
 * <h2>Por que uma classe abstrata?</h2>
 * <p>Existe um conceito comum entre {@link Pessoa} e {@link Atleta} —
 * ambas têm <em>nome</em> e <em>idade</em>, e ambas têm um "perfil" a ser
 * exibido. Mas <strong>não faz sentido instanciar "uma PessoaBase"</strong>
 * isolada: ela é genérica demais. Por isso a marcamos como {@code abstract}
 * — o compilador impede {@code new PessoaBase(...)} e força o uso de uma
 * subclasse concreta.</p>
 *
 * <h2>Concreto + abstrato na mesma classe</h2>
 * <p>Esta classe combina dois tipos de método:</p>
 * <ul>
 *   <li><strong>Concretos</strong> ({@link #getNome()}, {@link #getIdade()})
 *       — implementação completa, herdada e reutilizada por todas as
 *       subclasses sem cópia de código.</li>
 *   <li><strong>Abstrato</strong> ({@link #exibirPerfil()}) — apenas a
 *       assinatura. Cada subclasse tem que fornecer sua própria versão.
 *       É um "buraco" deliberado no design, garantindo flexibilidade.</li>
 * </ul>
 *
 * <h2>Por que {@code protected} nos atributos?</h2>
 * <p>Atributos {@code protected} são acessíveis pela própria classe e por
 * suas subclasses, mas <em>não</em> pelo mundo externo. Isso permite que
 * {@link Pessoa} use {@code this.nome} diretamente em {@code exibirPerfil()},
 * mas blinda contra acesso indevido de fora. O encapsulamento mais estrito
 * ({@code private} + getters/setters) acontece nos atributos específicos das
 * subclasses concretas (peso, altura, etc.).</p>
 *
 * <h2>Validação <em>fail-fast</em> no construtor</h2>
 * <p>O construtor rejeita imediatamente entradas inválidas (nome nulo, idade
 * fora de faixa). Esse padrão — chamado <em>fail-fast</em> — evita que o
 * objeto exista em estado inconsistente, simplificando o resto do código:
 * uma vez que a {@code PessoaBase} foi construída, nome e idade são
 * <strong>garantidamente</strong> válidos.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class PessoaBase {

    // ============================================================
    //                       CONSTANTES
    // ============================================================
    // Boa prática Java: extrair "magic numbers" para constantes nomeadas.
    // 'static final' = constante de classe (Effective Java, item 22).

    /** Idade mínima aceita (não-negativa). */
    protected static final int IDADE_MINIMA = 0;

    /**
     * Idade máxima aceita. 130 é um teto generoso — a pessoa mais velha
     * documentada (Jeanne Calment) viveu 122 anos. Usar um teto razoável
     * captura erros de digitação (ex.: 250) sem rejeitar casos extremos.
     */
    protected static final int IDADE_MAXIMA = 130;

    // ============================================================
    //                       ATRIBUTOS
    // ============================================================

    /**
     * Nome da pessoa. Marcado como {@code final} porque, depois de
     * construído, o nome não muda — é parte da identidade do objeto.
     * Atributos imutáveis simplificam o raciocínio sobre o código e
     * tornam a classe naturalmente <em>thread-safe</em> nesse aspecto.
     */
    protected final String nome;

    /**
     * Idade em anos completos. Também {@code final} pelo mesmo motivo:
     * representamos a idade no momento do cadastro. Se quiséssemos uma
     * idade que se atualiza, guardaríamos a data de nascimento.
     */
    protected final int idade;

    // ============================================================
    //                      CONSTRUTOR
    // ============================================================

    /**
     * Cria uma nova {@code PessoaBase} validando todas as pré-condições.
     *
     * @param nome  nome completo, não pode ser {@code null} nem em branco
     * @param idade idade em anos, deve estar entre {@value #IDADE_MINIMA}
     *              e {@value #IDADE_MAXIMA}
     * @throws EntradaInvalidaException se algum argumento for inválido
     */
    protected PessoaBase(final String nome, final int idade) {
        // Objects.requireNonNull lança NPE com mensagem clara se for null —
        // é uma forma idiomática Java (Effective Java, item 50).
        // Encadeamos com nossa exceção customizada para uniformizar o tratamento.
        if (nome == null) {
            throw new EntradaInvalidaException("O nome não pode ser nulo.");
        }
        if (nome.isBlank()) {
            throw new EntradaInvalidaException("O nome não pode estar em branco.");
        }
        if (idade < IDADE_MINIMA || idade > IDADE_MAXIMA) {
            throw new EntradaInvalidaException(
                "Idade deve estar entre " + IDADE_MINIMA + " e " + IDADE_MAXIMA
                + " (recebido: " + idade + ")."
            );
        }
        // 'trim()' tira espaços nas pontas — pequena melhoria de UX.
        this.nome  = nome.trim();
        this.idade = idade;
    }

    // ============================================================
    //                  MÉTODO ABSTRATO
    // ============================================================

    /**
     * Retorna uma descrição textual do perfil desta pessoa.
     *
     * <p>Cada subclasse concreta DEVE fornecer sua própria implementação.
     * Se uma subclasse esquecer de implementar este método, o compilador
     * acusa o erro em tempo de compilação — uma das proteções da
     * abstração obrigatória.</p>
     *
     * @return string formatada com as informações principais
     */
    public abstract String exibirPerfil();

    // ============================================================
    //                       GETTERS
    // ============================================================
    // Não fornecemos setters porque os atributos são final — a
    // imutabilidade é uma decisão deliberada de design.

    /** @return nome da pessoa (nunca {@code null}) */
    public String getNome() {
        return nome;
    }

    /** @return idade em anos completos */
    public int getIdade() {
        return idade;
    }

    // ============================================================
    //                  MÉTODOS DA CLASSE Object
    // ============================================================

    /**
     * Sobrescreve {@link Object#toString()} delegando para
     * {@link #exibirPerfil()}.
     *
     * <p>Por que? Quase todo framework, debugger e IDE chama
     * {@code toString()} ao exibir um objeto. Sem esse override, veríamos
     * algo como {@code Pessoa@1f32e575} — útil para ninguém.
     * <strong>Effective Java, item 12</strong>: sempre sobrescreva
     * {@code toString()} em classes de domínio.</p>
     *
     * <p>Como {@link #exibirPerfil()} já retorna a representação textual
     * que queremos, simplesmente delegamos.</p>
     */
    @Override
    public String toString() {
        return exibirPerfil();
    }

    /**
     * Igualdade estrutural baseada em nome e idade. Duas instâncias com
     * os mesmos valores são consideradas iguais.
     *
     * <p>Sempre que sobrescrevemos {@link #equals(Object)}, devemos também
     * sobrescrever {@link #hashCode()} para preservar o contrato (objetos
     * iguais devem ter o mesmo hash). <strong>Effective Java, itens 10 e 11</strong>.</p>
     */
    @Override
    public boolean equals(final Object outro) {
        if (this == outro) return true;
        if (!(outro instanceof PessoaBase)) return false;
        final PessoaBase p = (PessoaBase) outro;
        return idade == p.idade && Objects.equals(nome, p.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, idade);
    }
}
