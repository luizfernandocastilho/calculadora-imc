# Calculadora de IMC via CLI

## Autor

**Disciplina:** Programação Orientada a Objetos
**Professor:** Prof. Romes
**Instituição:** CEUB — Centro Universitário de Brasília

**Aluno:** Luiz Fernando Castilho
**RA/DRT:** 72600407
**E-mail:** luiz.castilho@sempreceub.com

Sistematização da disciplina **Programação Orientada a Objetos** —
CEUB, Turma B, Campus Virtual.

Calculadora de Índice de Massa Corporal (IMC) que funciona inteiramente
via linha de comando, demonstrando todos os pilares da Programação
Orientada a Objetos exigidos pelo enunciado: tipos básicos, controle de
fluxo, funções com responsabilidade única, recursão, interface, classe
abstrata, encapsulamento, herança multinível, polimorfismo, composição,
exceções personalizadas e gerenciamento de dependências via Maven.

## Link para o projeto no GitHub

```bash
https://github.com/luizfernandocastilho/calculadora-imc/tree/main
```

---

## Pré-requisitos

- **Java JDK 17 ou superior** (testado com OpenJDK 21)
- **Apache Maven 3.8 ou superior**

```bash
java -version
mvn -version
```

---

## Como compilar

A partir da raiz do projeto (a pasta que contém o `pom.xml`):

```bash
mvn clean package
```

Ou apenas compilar sem empacotar:

```bash
mvn compile
```

---

## Como executar

**Opção 1 — JAR gerado:**
```bash
java -jar target/calculadora-imc-1.0.0.jar
```

**Opção 2 — plugin do Maven:**
```bash
mvn exec:java
```

**Opção 3 — sem Maven, usando apenas `javac` e `java`:**
```bash
mkdir -p target/classes
javac -d target/classes -encoding UTF-8 src/main/java/br/edu/ceub/calculadoraimc/*.java
java -cp target/classes br.edu.ceub.calculadoraimc.Main
```

---

## 💻 Demonstração de uso

```
==================================================
  Calculadora de IMC via CLI — Sistematização POO
  CEUB - Programação Orientada a Objetos - Turma B
==================================================

== MENU ==
 [1] Cadastrar pessoa comum
 [2] Cadastrar atleta
 [3] Calcular IMC da última pessoa cadastrada
 [4] Exibir histórico de cálculos
 [5] Exibir perfil da última pessoa cadastrada
 [0] Sair
Escolha uma opção: 1

--- Cadastro de Pessoa ---
Nome: Maria Silva
Idade (anos): 25
Peso (kg): 70
Altura (m, ex.: 1.75): 1,75
✓ Pessoa cadastrada com sucesso.

(...escolhe 3...)
Resultado: Maria Silva → IMC: 22,86 (Peso normal)

(...cadastra atleta com mesmas medidas...)
Resultado: João Atleta → IMC: 22,86 (Abaixo do ideal para atleta)
```

A última linha mostra **polimorfismo**: o mesmo `classificarIMC(22.86)`
retorna `"Peso normal"` para Pessoa e `"Abaixo do ideal para atleta"` para
Atleta. A entrada aceita tanto vírgula quanto ponto como separador
decimal.

---

## Estrutura do projeto

```
calculadora-imc/
├── src/
│   ├── main/java/br/edu/ceub/calculadoraimc/
│   │   ├── CalculadoraIMC.java         ← interface (contrato do cálculo)
│   │   ├── PessoaBase.java             ← classe abstrata (molde base)
│   │   ├── Pessoa.java                 ← herança + encapsulamento + interface
│   │   ├── Atleta.java                 ← herança multinível + polimorfismo
│   │   ├── Historico.java              ← componente da composição
│   │   ├── SistemaIMC.java             ← composição + orquestração
│   │   ├── CalculadoraRecursiva.java   ← recursão + função simples
│   │   ├── EntradaInvalidaException.java ← exceção personalizada
│   │   └── Main.java                   ← ponto de entrada + menu CLI
│   └── test/java/br/edu/ceub/calculadoraimc/
│       └── CalculadoraIMCTest.java     ← suíte JUnit 5
├── pom.xml                              ← gerenciamento de dependências
├── .gitignore                           ← arquivos ignorados pelo Git
└── README.md                            ← este arquivo
```

Os arquivos estão organizados sob o package `br.edu.ceub.calculadoraimc`,
seguindo a convenção Java de nomeação reversa de domínio. Isso evita o
*default package* (não recomendado em código profissional) e prepara o
projeto para crescer com sub-packages se necessário.

---

## Conceitos de POO aplicados

| Conceito | Onde aparece no código |
|---|---|
| **Tipos de dados** (int, double, String, boolean) | `Main.java` (variáveis `idade`, `peso`, `altura`, `nome`, `houveCadastro`) |
| **Operadores** aritméticos, relacionais e lógicos | `Pessoa.calcularIMC()` (aritméticos), `classificarIMC()` (relacionais) |
| **Controle de fluxo** | Loop `while` do menu e `switch` das opções em `Main.java`; `if/else if` em todas as classificações |
| **Funções com responsabilidade única** | Métodos `lerInt`, `lerDouble`, `lerString` em `Main`; cada classe com papel claro |
| **Recursão** | `CalculadoraRecursiva.potencia()` com caso-base (`exp == 0`) e passo recursivo |
| **Interface** | `CalculadoraIMC.java`, implementada por `Pessoa` |
| **Classe abstrata** | `PessoaBase.java` com método abstrato `exibirPerfil()` e métodos concretos |
| **Encapsulamento** | Atributos `private` em `Pessoa` com getters e setters validadores |
| **Herança** | `Pessoa extends PessoaBase` reaproveitando atributos via `super()` |
| **Herança multinível** | `Atleta extends Pessoa extends PessoaBase` (3 níveis) |
| **Polimorfismo** | `Atleta` sobrescreve `classificarIMC` com `@Override`; `SistemaIMC.processar()` despacha em runtime |
| **Composição** | `SistemaIMC` *tem-um* `Historico` como atributo |
| **Exceção personalizada** | `EntradaInvalidaException extends RuntimeException`, capturada com `try/catch` em `Main` |
| **Gerenciamento de dependências** | `pom.xml` com Maven; dependência externa: JUnit Jupiter 5.10.2 |

---

## Boas práticas aplicadas

Além de cumprir os critérios do barema, o código segue boas práticas
estabelecidas (livro *Effective Java* de Joshua Bloch e padrões da
comunidade):

- **Princípios SOLID identificados nos comentários**: SRP em `Historico`,
  OCP/LSP em `Atleta`, ISP/DIP em `CalculadoraIMC`, DI em `SistemaIMC`.
- **Imutabilidade onde possível** (`final` em atributos que não mudam).
- **Validação fail-fast** nos construtores — objeto nunca existe em
  estado inválido.
- **Constantes nomeadas** para todos os limites das faixas de IMC,
  eliminando *magic numbers*.
- **Encapsulamento defensivo**: `Historico.getRegistros()` retorna view
  imutável (`Collections.unmodifiableList`).
- **Try-with-resources** no `Scanner` em `Main` (fechamento garantido).
- **`toString()` em todas as classes de domínio** (Effective Java item 12).
- **`equals()` e `hashCode()` consistentes** em `PessoaBase` (itens 10 e 11).
- **Construtor privado** em `CalculadoraRecursiva` para impedir
  instanciação de classe utilitária (item 4), com defesa contra reflexão.
- **Encadeamento de exceções** preservando causa raiz no construtor
  alternativo de `EntradaInvalidaException`.
- **Injeção de dependência** opcional em `SistemaIMC` (construtor que
  recebe `Historico`), facilitando testes.
- **UTF-8 declarado** no `pom.xml` (`project.build.sourceEncoding`),
  garantindo portabilidade de acentos entre sistemas operacionais.

---
