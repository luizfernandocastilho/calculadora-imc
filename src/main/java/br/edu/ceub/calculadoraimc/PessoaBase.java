package br.edu.ceub.calculadoraimc;

import java.util.Objects;

//Classe-base abstrata de qualquer pessoa no sistema.

public abstract class PessoaBase {

   
    //Idade mínima aceita (não-negativa)
    protected static final int IDADE_MINIMA = 0;

    //Idade máxima aceita
    protected static final int IDADE_MAXIMA = 130;

    //nome completo da pessoa. Marcado como 'final' porque o nome é parte da identidade da pessoa e não deve mudar após a criação do objeto. Se quiséssemos permitir mudanças, teríamos que lidar com as implicações disso (por exemplo, atualizar referências em outros objetos), o que complicaria o design sem um benefício claro. Além disso, a imutabilidade é uma prática recomendada para objetos de domínio, pois torna o código mais fácil de entender e menos propenso a erros.
    protected final String nome;

    //idade em anos completos. Marcado como 'final' porque a idade é um atributo fundamental da pessoa e não deve mudar após a criação do objeto. Se quiséssemos permitir mudanças na idade, teríamos que lidar com as implicações disso (por exemplo, atualizar referências em outros objetos), o que complicaria o design sem um benefício claro. Além disso, a imutabilidade é uma prática recomendada para objetos de domínio, pois torna o código mais fácil de entender e menos propenso a erros.
    protected final int idade;

   //criador protegido para ser chamado apenas por subclasses concretas. Força as subclasses a fornecerem os dados necessários (nome e idade) e garante que a validação seja sempre aplicada, mesmo que as subclasses sejam criadas por outros desenvolvedores no futuro. A validação de entrada é crucial para manter a integridade dos objetos e evitar erros em tempo de execução, e colocar essa lógica no construtor da classe base garante que todas as subclasses herdem essa proteção sem depender da implementação correta de cada uma.
    protected PessoaBase(final String nome, final int idade) {
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
        this.nome  = nome.trim();
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    //Cada subclasse concreta define como se apresenta. Declarado aqui
    //para que toString() possa delegar polimorficamente.
    public abstract String exibirPerfil();

    @Override
    public String toString() {
        return exibirPerfil();
    }

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
