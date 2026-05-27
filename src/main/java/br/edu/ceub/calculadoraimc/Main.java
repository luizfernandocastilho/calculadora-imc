package br.edu.ceub.calculadoraimc;

import java.util.Scanner;

public class Main {

    private static final int OPCAO_CADASTRAR_PESSOA = 1;
    private static final int OPCAO_CADASTRAR_ATLETA = 2;
    private static final int OPCAO_CALCULAR_IMC     = 3;
    private static final int OPCAO_EXIBIR_HISTORICO = 4;
    private static final int OPCAO_EXIBIR_PERFIL    = 5;
    private static final int OPCAO_SAIR             = 0;

    private static final SistemaIMC sistema = new SistemaIMC();

    private static Pessoa ultimaPessoa = null;

    private static boolean houveCadastro = false;

    public static void main(final String[] args) {
        imprimirCabecalho();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean continuar = true;
            while (continuar) {
                continuar = processarOpcao(scanner);
            }
        }
    }

    private static boolean processarOpcao(final Scanner scanner) {
        try {
            exibirMenu();
            final int opcao = lerInt(scanner, "Escolha uma opção");

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
            System.out.println();
            System.out.println("[Erro] " + e.getMessage());
            System.out.println("Por favor, tente novamente.");
            System.out.println();
        } catch (final RuntimeException e) {
            System.out.println();
            System.out.println("[Erro inesperado] " + e.getMessage());
            System.out.println();
        }
        return true;
    }

    private static void imprimirCabecalho() {
        CalculadoraRecursiva.linhaSeparadora();
        System.out.println("  Calculadora de IMC via CLI — Sistematização POO");
        System.out.println("  CEUB - Programação Orientada a Objetos");
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

        ultimaPessoa  = new Atleta(nome, idade, peso, altura, modalidade);
        houveCadastro = true;
        System.out.println("✓ Atleta cadastrado com sucesso.");
    }

    private static void calcularIMCDaUltimaPessoa() {
        garantirPessoaCadastrada();
        sistema.processar(ultimaPessoa);
    }

    private static void exibirPerfilUltimaPessoa() {
        garantirPessoaCadastrada();
        System.out.println();
        System.out.println(ultimaPessoa.exibirPerfil());
    }

    private static void garantirPessoaCadastrada() {
        if (!houveCadastro || ultimaPessoa == null) {
            throw new EntradaInvalidaException(
                "Nenhuma pessoa cadastrada ainda. Use a opção [1] ou [2] antes."
            );
        }
    }

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

    private static double lerDouble(final Scanner scanner, final String campo) {
        System.out.print(campo + ": ");
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
