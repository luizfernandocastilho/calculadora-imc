package br.edu.ceub.calculadoraimc;

import java.util.Scanner;

/**
 * Ponto de entrada do sistema — apresenta menu CLI interativo, lê dados
 * do usuário e orquestra a chamada das demais classes.
 *
 * <h2>Papel desta classe</h2>
 * <p>{@code Main} concentra a interação com o usuário (camada de
 * apresentação CLI). Ela <em>não</em> sabe calcular IMC nem armazenar
 * histórico — delega tudo para {@link SistemaIMC}, {@link Pessoa} e
 * {@link Atleta}. Essa separação é o início da clássica organização em
 * camadas (apresentação ↔ domínio).</p>
 *
 * <h2>Conceitos integrados aqui</h2>
 * <ul>
 *   <li><strong>Tipos básicos</strong>: {@code int} (opção, idade),
 *       {@code double} (peso, altura), {@link String} (nome, modalidade),
 *       {@code boolean} ({@link #houveCadastro}).</li>
 *   <li><strong>Operadores</strong>: aritméticos no IMC, relacionais nas
 *       comparações, lógicos nos {@code &&} de validação.</li>
 *   <li><strong>Controle de fluxo</strong>: laço {@code while} do menu e
 *       {@code switch} sobre a opção.</li>
 *   <li><strong>Instanciação</strong>: {@code new Pessoa(...)} e
 *       {@code new Atleta(...)}.</li>
 *   <li><strong>Polimorfismo de referência</strong>: a variável
 *       {@link #ultimaPessoa} é declarada {@code Pessoa} mas pode segurar
 *       {@code Atleta} sem cast.</li>
 *   <li><strong>Recursão</strong>: chamada indireta a
 *       {@link CalculadoraRecursiva#potencia(double, int)} via
 *       {@link Pessoa#calcularIMC(double, double)}.</li>
 *   <li><strong>Tratamento de exceções</strong>: bloco {@code try/catch}
 *       em volta do menu inteiro garante que o programa nunca é encerrado
 *       por entradas inválidas.</li>
 * </ul>
 *
 * <h2>Decisões de UX</h2>
 * <ul>
 *   <li>Aceita vírgula <em>e</em> ponto como separador decimal — usuário
 *       brasileiro digita "1,75" naturalmente.</li>
 *   <li>Nunca encerra por erro; volta ao menu com mensagem amigável.</li>
 *   <li>Validações usam mensagens com contexto: "Peso deve ser positivo
 *       (recebido: -5)" é mais útil que "Erro".</li>
 * </ul>
 *
 * @author Sistematização POO — CEUB
 * @version 1.0.0
 * @since 1.0.0
 */
public class Main {

    // ============================================================
    //          CONSTANTES — Opções do menu
    // ============================================================
    // Magic numbers no switch são uma das fontes mais clássicas de bugs
    // em programas CLI ("o '3' agora faz outra coisa"). Constantes
    // nomeadas resolvem isso e tornam o código autodocumentado.

    private static final int OPCAO_CADASTRAR_PESSOA = 1;
    private static final int OPCAO_CADASTRAR_ATLETA = 2;
    private static final int OPCAO_CALCULAR_IMC     = 3;
    private static final int OPCAO_EXIBIR_HISTORICO = 4;
    private static final int OPCAO_EXIBIR_PERFIL    = 5;
    private static final int OPCAO_SAIR             = 0;

    // ============================================================
    //              ESTADO DA SESSÃO
    // ============================================================
    // Em código de produção poderíamos isolar esse estado numa classe
    // ContextoSessao. Para o escopo da Sistematização, manter aqui é
    // suficiente e mais legível.

    /** Sistema responsável por calcular, classificar e registrar IMCs. */
    private static final SistemaIMC sistema = new SistemaIMC();

    /**
     * Última pessoa cadastrada na sessão.
     * <p>Tipo declarado {@link Pessoa}, mas na prática pode ser
     * {@link Atleta} — exemplo de <em>polimorfismo de referência</em>.</p>
     */
    private static Pessoa ultimaPessoa = null;

    /**
     * Sentinela que indica se houve algum cadastro. Usado para barrar a
     * tentativa de calcular IMC antes de cadastrar alguém. Demonstra o
     * uso de {@code boolean} como tipo lógico (requisito do barema).
     */
    private static boolean houveCadastro = false;

    // ============================================================
    //                      MAIN LOOP
    // ============================================================

    /**
     * Ponto de entrada. Executa o laço principal do menu até o usuário
     * escolher sair.
     *
     * <p>Note o <strong>try-with-resources</strong> na declaração do
     * {@link Scanner}: garante que o recurso é fechado automaticamente
     * ao fim do bloco, mesmo se uma exceção for lançada. Effective Java,
     * item 9.</p>
     *
     * @param args ignorados (interação é interativa via stdin)
     */
    public static void main(final String[] args) {
        imprimirCabecalho();

        // try-with-resources — o Scanner é fechado automaticamente.
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                continuar = processarOpcao(scanner);
            }
        }
    }

    /**
     * Lê e processa uma única opção do usuário. Encapsula o try/catch
     * para que o {@link #main} fique enxuto.
     *
     * @return {@code false} se o usuário pediu para sair, {@code true} caso contrário
     */
    private static boolean processarOpcao(final Scanner scanner) {
        try {
            exibirMenu();
            final int opcao = lerInt(scanner, "Escolha uma opção");

            // 'switch expression' (Java 14+): forma moderna, mais segura
            // que o switch tradicional — sem fall-through acidental.
            switch (opcao) {
                case OPCAO_CADASTRAR_PESSOA -> cadastrarPessoa(scanner);
                case OPCAO_CADASTRAR_ATLETA -> cadastrarAtleta(scanner);
                case OPCAO_CALCULAR_IMC     -> calcularIMCDaUltimaPessoa();
                case OPCAO_EXIBIR_HISTORICO -> sistema.exibirHistorico();
                case OPCAO_EXIBIR_PERFIL    -> exibirPerfilUltimaPessoa();
                case OPCAO_SAIR             -> {
                    System.out.println("Encerrando o sistema. Até a próxima!");
                    return false;
                }
                default -> throw new EntradaInvalidaException(
                    "Opção inválida: " + opcao + ". Escolha entre 0 e 5."
                );
            }
        } catch (final EntradaInvalidaException e) {
            // Erro previsto — mensagem amigável, segue o jogo.
            System.out.println();
            System.out.println("[Erro] " + e.getMessage());
            System.out.println("Por favor, tente novamente.");
            System.out.println();
        } catch (final RuntimeException e) {
            // Salvaguarda para erros inesperados — não derruba o programa.
            // Effective Java, item 73 (re-lançar uma exceção mais geral
            // captura aqui falhas que escaparam dos catches específicos).
            System.out.println();
            System.out.println("[Erro inesperado] " + e.getMessage());
            System.out.println();
        }
        return true;
    }

    // ============================================================
    //              EXIBIÇÃO E CADASTROS
    // ============================================================

    private static void imprimirCabecalho() {
        CalculadoraRecursiva.linhaSeparadora();
        System.out.println("  Calculadora de IMC via CLI — Sistematização POO");
        System.out.println("  CEUB - Programação Orientada a Objetos - Turma B");
        CalculadoraRecursiva.linhaSeparadora();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("== MENU ==");
        System.out.println(" [" + OPCAO_CADASTRAR_PESSOA + "] Cadastrar pessoa comum");
        System.out.println(" [" + OPCAO_CADASTRAR_ATLETA + "] Cadastrar atleta");
        System.out.println(" [" + OPCAO_CALCULAR_IMC     + "] Calcular IMC da última pessoa cadastrada");
        System.out.println(" [" + OPCAO_EXIBIR_HISTORICO + "] Exibir histórico de cálculos");
        System.out.println(" [" + OPCAO_EXIBIR_PERFIL    + "] Exibir perfil da última pessoa cadastrada");
        System.out.println(" [" + OPCAO_SAIR             + "] Sair");
    }

    private static void cadastrarPessoa(final Scanner scanner) {
        System.out.println();
        System.out.println("--- Cadastro de Pessoa ---");
        final String nome   = lerString(scanner, "Nome");
        final int    idade  = lerInt(scanner, "Idade (anos)");
        final double peso   = lerDouble(scanner, "Peso (kg)");
        final double altura = lerDouble(scanner, "Altura (m, ex.: 1.75)");

        // Atribuição via referência polimórfica.
        ultimaPessoa  = new Pessoa(nome, idade, peso, altura);
        houveCadastro = true;
        System.out.println("✓ Pessoa cadastrada com sucesso.");
    }

    private static void cadastrarAtleta(final Scanner scanner) {
        System.out.println();
        System.out.println("--- Cadastro de Atleta ---");
        final String nome       = lerString(scanner, "Nome");
        final int    idade      = lerInt(scanner, "Idade (anos)");
        final double peso       = lerDouble(scanner, "Peso (kg)");
        final double altura     = lerDouble(scanner, "Altura (m, ex.: 1.85)");
        final String modalidade = lerString(scanner, "Modalidade esportiva");

        // Aqui novamente o polimorfismo: 'ultimaPessoa' é Pessoa, recebe Atleta.
        ultimaPessoa  = new Atleta(nome, idade, peso, altura, modalidade);
        houveCadastro = true;
        System.out.println("✓ Atleta cadastrado com sucesso.");
    }

    private static void calcularIMCDaUltimaPessoa() {
        garantirPessoaCadastrada();
        // Dentro de processar(), classificarIMC é despachado polimorficamente.
        sistema.processar(ultimaPessoa);
    }

    private static void exibirPerfilUltimaPessoa() {
        garantirPessoaCadastrada();
        System.out.println();
        System.out.println(ultimaPessoa.exibirPerfil());
    }

    /** DRY: validação reutilizada nas opções 3 e 5. */
    private static void garantirPessoaCadastrada() {
        if (!houveCadastro || ultimaPessoa == null) {
            throw new EntradaInvalidaException(
                "Nenhuma pessoa cadastrada ainda. Use a opção [1] ou [2] antes."
            );
        }
    }

    // ============================================================
    //          MÉTODOS DE LEITURA COM VALIDAÇÃO
    // ============================================================
    // Recebem o Scanner como parâmetro para serem testáveis e não
    // dependerem de estado global. Isso é injeção de dependência
    // simples na escala de método.

    /**
     * Lê uma linha de texto e valida que não está em branco.
     *
     * @throws EntradaInvalidaException se vazia ou só espaços
     */
    private static String lerString(final Scanner scanner, final String campo) {
        System.out.print(campo + ": ");
        final String linha = scanner.nextLine().trim();
        if (linha.isBlank()) {
            throw new EntradaInvalidaException(
                "O campo '" + campo + "' não pode ficar vazio."
            );
        }
        return linha;
    }

    /**
     * Lê um inteiro com tratamento de erro. Encapsula
     * {@link NumberFormatException} em {@link EntradaInvalidaException}
     * para uniformizar o tratamento (Effective Java, item 73).
     */
    private static int lerInt(final Scanner scanner, final String campo) {
        System.out.print(campo + ": ");
        final String linha = scanner.nextLine().trim();
        try {
            return Integer.parseInt(linha);
        } catch (final NumberFormatException e) {
            throw new EntradaInvalidaException(
                "Valor inválido para '" + campo + "'. Digite um número inteiro.", e
            );
        }
    }

    /**
     * Lê um {@code double} aceitando vírgula ou ponto como separador
     * decimal — usuário brasileiro digita "1,75" naturalmente. Faz uma
     * normalização simples antes de parsear.
     */
    private static double lerDouble(final Scanner scanner, final String campo) {
        System.out.print(campo + ": ");
        // replace(',', '.') padroniza para o formato esperado por
        // Double.parseDouble (que aceita apenas ponto).
        final String linha = scanner.nextLine().trim().replace(',', '.');
        try {
            final double valor = Double.parseDouble(linha);
            if (valor <= 0) {
                throw new EntradaInvalidaException(
                    "O campo '" + campo + "' deve ser positivo (recebido: "
                    + valor + ")."
                );
            }
            return valor;
        } catch (final NumberFormatException e) {
            throw new EntradaInvalidaException(
                "Valor inválido para '" + campo
                + "'. Digite um número (ex.: 1.75 ou 1,75).", e
            );
        }
    }
}
