/**
 * Atleta — pessoa com critérios de classificação de IMC diferenciados.
 *
 * <p>Demonstra:</p>
 * <ul>
 *   <li><b>Herança multinível</b>: Atleta → Pessoa → PessoaBase.</li>
 *   <li><b>Polimorfismo</b>: sobrescreve {@link #classificarIMC(double)} com
 *       faixas próprias para esportistas. O mesmo método chamado em uma
 *       referência {@code Pessoa} despacha para a versão correta em runtime,
 *       dependendo do tipo real do objeto.</li>
 *   <li><b>Reutilização via {@code super()}</b>: o construtor delega para
 *       {@link Pessoa#Pessoa(String, int, double, double)} e
 *       {@link #exibirPerfil()} chama {@code super.exibirPerfil()}.</li>
 * </ul>
 *
 * <p>Justificativa das faixas: atletas têm percentual maior de massa magra,
 * então um IMC "normal" pela OMS pode estar abaixo do ideal para eles. As
 * faixas abaixo são uma simplificação didática — em prática, avaliação
 * esportiva usa percentual de gordura e bioimpedância.</p>
 */
public class Atleta extends Pessoa {

    /** Modalidade esportiva (ex.: "Natação", "Musculação", "Corrida"). */
    private String modalidade;

    /**
     * Construtor — delega os 4 primeiros parâmetros ao construtor de Pessoa
     * e inicializa apenas o que é específico do Atleta.
     */
    public Atleta(String nome, int idade, double peso, double altura, String modalidade) {
        super(nome, idade, peso, altura); // sobe a cadeia até PessoaBase
        this.modalidade = modalidade;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    // ----- Polimorfismo: mesma assinatura, comportamento diferente -----

    /**
     * Classificação adaptada para atletas. Sobrescreve a versão de
     * {@link Pessoa} usando {@code @Override} — o compilador valida que
     * estamos de fato sobrescrevendo um método existente.
     */
    @Override
    public String classificarIMC(double imc) {
        if (imc < 20.0)      return "Abaixo do ideal para atleta";
        else if (imc < 27.0) return "Ideal para atleta";
        else                 return "Acima do ideal para atleta";
    }

    /**
     * Reutiliza o perfil base e adiciona a modalidade. Note o uso de
     * {@code super.exibirPerfil()} — não duplicamos a montagem da string.
     */
    @Override
    public String exibirPerfil() {
        return super.exibirPerfil() + " | Modalidade: " + modalidade + " (atleta)";
    }
}
