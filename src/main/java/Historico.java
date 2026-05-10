import java.util.ArrayList;
import java.util.List;

/**
 * Historico — armazena os registros textuais de cada cálculo feito na sessão.
 *
 * <p>Responsabilidade ÚNICA: guardar e exibir registros. Nada além disso.
 * Esta classe é o "componente" da relação de <b>composição</b>: ela será
 * usada como atributo dentro de {@link SistemaIMC} (relação <i>tem-um</i>,
 * não <i>é-um</i>).</p>
 *
 * <p>Não herda de nada e não implementa nenhuma interface — é deliberadamente
 * simples e isolada. Isso facilita testar e substituir.</p>
 */
public class Historico {

    /** Lista de registros já formatados como String. */
    private final List<String> registros = new ArrayList<>();

    /**
     * Adiciona um novo registro ao histórico.
     *
     * @param registro linha já formatada (ex.: "Maria → IMC: 22,86 (Peso normal)")
     */
    public void adicionar(String registro) {
        if (registro == null || registro.isBlank()) {
            // Registro vazio é silenciosamente ignorado para não poluir o histórico.
            return;
        }
        registros.add(registro);
    }

    /**
     * Exibe todos os registros no console. Se a lista estiver vazia, informa.
     */
    public void exibir() {
        if (registros.isEmpty()) {
            System.out.println("(Nenhum cálculo registrado nesta sessão.)");
            return;
        }
        System.out.println("--- Histórico de cálculos ---");
        int n = 1;
        for (String r : registros) { // loop for-each
            System.out.printf("%2d. %s%n", n++, r);
        }
        System.out.println("-----------------------------");
    }

    /** @return quantidade de registros (útil para testes/depuração). */
    public int tamanho() {
        return registros.size();
    }
}
