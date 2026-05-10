/**
 * SistemaIMC — orquestra o cálculo, a classificação e o registro de IMCs.
 *
 * <p><b>Composição em ação</b>: SistemaIMC <i>tem-um</i> {@link Historico},
 * não <i>é-um</i> Historico. Por isso o relacionamento é via atributo,
 * NÃO via herança. Trocar a implementação do histórico (por exemplo, para
 * gravar em arquivo) afeta apenas o tipo do atributo.</p>
 *
 * <p>É também o ponto onde o <b>polimorfismo</b> fica explícito: o método
 * {@link #processar(Pessoa)} chama {@code pessoa.classificarIMC(...)} sem
 * saber se a referência aponta para uma {@link Pessoa} comum ou um
 * {@link Atleta} — quem decide qual implementação rodar é a JVM em runtime,
 * com base no tipo real do objeto.</p>
 */
public class SistemaIMC {

    /** Histórico interno — relação "tem-um" (composição). */
    private final Historico historico = new Historico();

    /**
     * Calcula, classifica, registra no histórico e exibe o IMC da pessoa.
     *
     * @param pessoa pode ser {@link Pessoa} comum ou {@link Atleta} —
     *               graças ao polimorfismo, o método correto é despachado
     *               automaticamente conforme o tipo real do objeto.
     */
    public void processar(Pessoa pessoa) {
        if (pessoa == null) {
            throw new EntradaInvalidaException("Não há pessoa cadastrada para calcular IMC.");
        }

        double imc       = pessoa.calcularIMC(pessoa.getPeso(), pessoa.getAltura());
        String classe    = pessoa.classificarIMC(imc); // <- aqui o polimorfismo despacha
        double imcRound  = CalculadoraRecursiva.arredondarDuasCasas(imc);

        String linha = String.format(
            "%s → IMC: %.2f (%s)",
            pessoa.getNome(), imcRound, classe
        );

        historico.adicionar(linha);

        System.out.println();
        System.out.println(pessoa.exibirPerfil());
        System.out.println("Resultado: " + linha);
        System.out.println();
    }

    /** Delega a exibição para o objeto {@link Historico} interno. */
    public void exibirHistorico() {
        historico.exibir();
    }

    /** @return quantos cálculos foram feitos na sessão. */
    public int totalCalculos() {
        return historico.tamanho();
    }
}
