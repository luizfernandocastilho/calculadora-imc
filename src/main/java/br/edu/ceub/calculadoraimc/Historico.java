package br.edu.ceub.calculadoraimc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Armazena e exibe os registros textuais dos cálculos de IMC realizados
 * durante a sessão.
 *
 * <h2>Single Responsibility Principle (SRP) em ação</h2>
 * <p>Esta classe tem uma única razão para mudar: a forma como guardamos ou
 * exibimos registros. Não calcula IMC, não conhece {@link Pessoa}, não lê
 * dados do usuário. Essa coesão alta torna a classe trivial de testar e de
 * substituir — se quisermos persistir o histórico em arquivo ou banco,
 * basta criar uma nova classe que ofereça os mesmos métodos.</p>
 *
 * <h2>Papel na Composição</h2>
 * <p>Esta classe é o "componente" da relação de composição que existe
 * em {@link SistemaIMC}. SistemaIMC <em>tem-um</em> {@code Historico}
 * — não <em>é-um</em>. Esse "tem-um" se materializa como um atributo do
 * tipo {@code Historico} dentro de {@code SistemaIMC}, não como herança.</p>
 *
 * <h2>Encapsulamento defensivo</h2>
 * <p>O método {@link #getRegistros()} retorna uma <em>view imutável</em>
 * da lista interna, não a lista em si. Por que? Se retornássemos a lista
 * mutável, código externo poderia chamar {@code getRegistros().clear()} e
 * apagar todo o histórico sem passar pelos métodos da classe. Devolver
 * uma view imutável protege a invariante de "o histórico só muda via
 * {@link #adicionar(String)}". Effective Java, item 50: faça cópias
 * defensivas quando necessário.</p>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class Historico {

    /**
     * Lista interna de registros. {@code final} indica que a referência
     * nunca muda (sempre é a mesma lista, não pode ser substituída por
     * outra). O conteúdo da lista, sim, muda — adicionamos itens.
     */
    private final List<String> registros = new ArrayList<>();

    /**
     * Adiciona um novo registro ao histórico. Registros nulos ou em
     * branco são silenciosamente ignorados — política deliberada para
     * não poluir a saída com linhas vazias acidentais.
     *
     * @param registro linha já formatada (pode ser {@code null} — será ignorada)
     */
    public void adicionar(final String registro) {
        if (registro == null || registro.isBlank()) {
            return;
        }
        registros.add(registro);
    }

    /**
     * Imprime todos os registros no console em ordem cronológica.
     * Se não houver nenhum registro, exibe mensagem informativa.
     */
    public void exibir() {
        if (registros.isEmpty()) {
            System.out.println("(Nenhum cálculo registrado nesta sessão.)");
            return;
        }
        System.out.println("--- Histórico de cálculos ---");
        int n = 1;
        // for-each — preferível ao for clássico quando não precisamos do índice;
        // expressa a intenção "para cada elemento" sem ruído de incrementador.
        for (final String r : registros) {
            System.out.printf("%2d. %s%n", n++, r);
        }
        System.out.println("-----------------------------");
    }

    /**
     * @return quantidade de registros (útil para testes e diagnóstico)
     */
    public int tamanho() {
        return registros.size();
    }

    /**
     * Retorna uma <strong>view imutável</strong> dos registros.
     *
     * <p>Tentar adicionar/remover na lista retornada lança
     * {@link UnsupportedOperationException}. Isso garante que a única forma
     * de alterar o histórico continua sendo via {@link #adicionar(String)}.</p>
     *
     * @return lista somente-leitura dos registros, na ordem em que foram adicionados
     */
    public List<String> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    @Override
    public String toString() {
        return "Historico[" + registros.size() + " registro(s)]";
    }
}
