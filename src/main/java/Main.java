import java.util.Scanner;

/**
 * Main — ponto de entrada do sistema. Apresenta menu CLI interativo,
 * orquestra a leitura de dados, instanciação de objetos e chamada do
 * {@link SistemaIMC}.
 *
 * <p>Esta classe é a <b>cola</b> que junta todos os conceitos:</p>
 * <ul>
 *   <li>Tipos de dados básicos (int, double, String, boolean) e operadores.</li>
 *   <li>Entrada/saída via {@link Scanner} e {@link System#out}.</li>
 *   <li>Controle de fluxo: loop {@code while} no menu e {@code switch} nas opções.</li>
 *   <li>Instanciação de objetos {@link Pessoa} e {@link Atleta} via {@code new}.</li>
 *   <li>Polimorfismo de referência: variável {@code Pessoa} guarda também {@code Atleta}.</li>
 *   <li>Recursão: {@link CalculadoraRecursiva#potencia(double, int)} é chamada
 *       indiretamente via {@link Pessoa#calcularIMC(double, double)}.</li>
 *   <li>Tratamento de exceções: leituras protegidas por {@code try/catch}.</li>
 * </ul>
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final SistemaIMC sistema = new SistemaIMC();

    /** Última pessoa cadastrada — pode ser Pessoa ou Atleta (polimorfismo de referência). */
    private static Pessoa ultimaPessoa = null;

    /** Variável boolean demonstrando tipo lógico (requisito do barema). */
    private static boolean houveCadastro = false;

    public static void main(String[] args) {
        int opcao = -1;
        boolean continuar = true; // tipo lógico

        imprimirCabecalho();

        // ----- LOOP DE REPETIÇÃO do menu -----
        while (continuar) {
            try {
                exibirMenu();
                opcao = lerInt("Escolha uma opção");

                // Estrutura condicional via switch (também demonstra controle de fluxo)
                switch (opcao) {
                    case 1 -> cadastrarPessoa();
                    case 2 -> cadastrarAtleta();
                    case 3 -> calcularIMCDaUltimaPessoa();
                    case 4 -> sistema.exibirHistorico();
                    case 5 -> exibirPerfilUltimaPessoa();
                    case 0 -> {
                        continuar = false;
                        System.out.println("Encerrando o sistema. Até a próxima!");
                    }
                    default -> throw new EntradaInvalidaException(
                        "Opção inválida: " + opcao + ". Escolha entre 0 e 5."
                    );
                }
            } catch (EntradaInvalidaException e) {
                System.out.println();
                System.out.println("[Erro] " + e.getMessage());
                System.out.println("Por favor, tente novamente.");
                System.out.println();
            } catch (RuntimeException e) {
                // Salvaguarda final — qualquer erro inesperado não derruba o programa.
                System.out.println();
                System.out.println("[Erro inesperado] " + e.getMessage());
                System.out.println();
            }
        }

        sc.close();
    }

    // ============================================================
    //                  MENU E AÇÕES
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
        System.out.println(" [1] Cadastrar pessoa comum");
        System.out.println(" [2] Cadastrar atleta");
        System.out.println(" [3] Calcular IMC da última pessoa cadastrada");
        System.out.println(" [4] Exibir histórico de cálculos");
        System.out.println(" [5] Exibir perfil da última pessoa cadastrada");
        System.out.println(" [0] Sair");
    }

    private static void cadastrarPessoa() {
        System.out.println();
        System.out.println("--- Cadastro de Pessoa ---");
        String nome    = lerString("Nome");
        int idade      = lerInt("Idade (anos)");
        double peso    = lerDouble("Peso (kg)");
        double altura  = lerDouble("Altura (m, ex.: 1.75)");

        // Polimorfismo de referência: variável Pessoa armazena tanto Pessoa quanto Atleta.
        ultimaPessoa = new Pessoa(nome, idade, peso, altura);
        houveCadastro = true;

        System.out.println("✓ Pessoa cadastrada com sucesso.");
    }

    private static void cadastrarAtleta() {
        System.out.println();
        System.out.println("--- Cadastro de Atleta ---");
        String nome       = lerString("Nome");
        int idade         = lerInt("Idade (anos)");
        double peso       = lerDouble("Peso (kg)");
        double altura     = lerDouble("Altura (m, ex.: 1.85)");
        String modalidade = lerString("Modalidade esportiva");

        // Atleta extends Pessoa — atribuição válida graças ao polimorfismo.
        ultimaPessoa = new Atleta(nome, idade, peso, altura, modalidade);
        houveCadastro = true;

        System.out.println("✓ Atleta cadastrado com sucesso.");
    }

    private static void calcularIMCDaUltimaPessoa() {
        if (!houveCadastro || ultimaPessoa == null) {
            throw new EntradaInvalidaException(
                "Nenhuma pessoa cadastrada ainda. Use a opção [1] ou [2] antes."
            );
        }
        // O método processar() despacha a classificação correta via polimorfismo.
        sistema.processar(ultimaPessoa);
    }

    private static void exibirPerfilUltimaPessoa() {
        if (!houveCadastro || ultimaPessoa == null) {
            throw new EntradaInvalidaException(
                "Nenhuma pessoa cadastrada ainda. Use a opção [1] ou [2] antes."
            );
        }
        System.out.println();
        System.out.println(ultimaPessoa.exibirPerfil());
    }

    // ============================================================
    //               LEITURAS COM VALIDAÇÃO
    // ============================================================

    /**
     * Lê uma linha de texto e valida que não está em branco.
     */
    private static String lerString(String campo) {
        System.out.print(campo + ": ");
        String linha = sc.nextLine().trim();
        if (linha.isBlank()) {
            throw new EntradaInvalidaException("O campo '" + campo + "' não pode ficar vazio.");
        }
        return linha;
    }

    /**
     * Lê um inteiro com tratamento de erro. Encapsula o erro em
     * {@link EntradaInvalidaException} para padronizar o tratamento.
     */
    private static int lerInt(String campo) {
        System.out.print(campo + ": ");
        String linha = sc.nextLine().trim();
        try {
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(
                "Valor inválido para '" + campo + "'. Digite um número inteiro.", e
            );
        }
    }

    /**
     * Lê um double aceitando tanto vírgula quanto ponto como separador
     * decimal (ergonomia para usuário brasileiro). Valores não positivos
     * são rejeitados pelo setter da Pessoa, mas validamos básico aqui também.
     */
    private static double lerDouble(String campo) {
        System.out.print(campo + ": ");
        String linha = sc.nextLine().trim().replace(',', '.');
        try {
            double valor = Double.parseDouble(linha);
            if (valor <= 0) {
                throw new EntradaInvalidaException(
                    "O campo '" + campo + "' deve ser positivo (recebido: " + valor + ")."
                );
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(
                "Valor inválido para '" + campo + "'. Digite um número (ex.: 1.75 ou 1,75).", e
            );
        }
    }
}
