# Calculadora de IMC via CLI

Sistematização da disciplina **Programação Orientada a Objetos** —
CEUB, Turma B, Campus Virtual (27/04/26 GV2).

Calculadora de Índice de Massa Corporal (IMC) que funciona inteiramente
via linha de comando, demonstrando todos os pilares da Programação
Orientada a Objetos exigidos pelo enunciado: tipos básicos, controle de
fluxo, funções com responsabilidade única, recursão, interface, classe
abstrata, encapsulamento, herança multinível, polimorfismo, composição,
exceções personalizadas e gerenciamento de dependências via Maven.

---

## 📋 Pré-requisitos

Para compilar e executar o projeto você precisa ter instalado:

- **Java JDK 17 ou superior** (testado com OpenJDK 21)
- **Apache Maven 3.8 ou superior**

Verificação rápida:

```bash
java -version
mvn -version
```

---

## 🔧 Como compilar

A partir da raiz do projeto (a pasta que contém o `pom.xml`):

```bash
mvn clean package
```

Esse comando faz três coisas: limpa builds anteriores, compila o
código-fonte (em `src/main/java`) e empacota tudo num JAR executável
dentro de `target/`.

Se quiser apenas compilar sem empacotar:

```bash
mvn compile
```

---

## ▶️ Como executar

Após o `mvn package`, há duas formas de rodar:

**Opção 1 — Via JAR gerado:**
```bash
java -jar target/calculadora-imc-1.0.0.jar
```

**Opção 2 — Via plugin do Maven (não precisa empacotar):**
```bash
mvn exec:java
```

**Opção 3 — Sem Maven, usando apenas `javac` e `java`** (útil se ainda
não instalou Maven):
```bash
mkdir -p target/classes
javac -d target/classes -encoding UTF-8 src/main/java/*.java
java -cp target/classes Main
```

---

## 🧪 Como rodar os testes

A suíte JUnit 5 valida que cada conceito de POO está implementado e
funcionando como esperado:

```bash
mvn test
```

São 12 testes cobrindo: recursão (caso-base e passo recursivo), cálculo
do IMC, polimorfismo entre Pessoa e Atleta, classificação OMS completa,
encapsulamento (validação no setter), herança multinível, composição
SistemaIMC↔Historico e exceção personalizada.

---

## 💻 Demonstração de uso

Sessão de exemplo no terminal:

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
Altura (m, ex.: 1.75): 1.75
✓ Pessoa cadastrada com sucesso.

(...escolhe opção 3...)

Pessoa: Maria Silva | Idade: 25 | Peso: 70.00 kg | Altura: 1.75 m
Resultado: Maria Silva → IMC: 22.86 (Peso normal)

(...cadastra um atleta com mesmas medidas para mostrar polimorfismo...)
Resultado: João Atleta → IMC: 22.86 (Abaixo do ideal para atleta)
```

A última linha mostra **polimorfismo na prática**: o mesmo método
`classificarIMC(22.86)` retorna `"Peso normal"` para Pessoa e
`"Abaixo do ideal para atleta"` para Atleta.

A entrada aceita tanto vírgula quanto ponto como separador decimal
(ex.: `1,75` ou `1.75`).

---

## 📁 Estrutura do projeto

```
calculadora-imc/
├── src/
│   ├── main/java/
│   │   ├── CalculadoraIMC.java         ← interface (contrato do cálculo)
│   │   ├── PessoaBase.java             ← classe abstrata (molde base)
│   │   ├── Pessoa.java                 ← herança + encapsulamento + interface
│   │   ├── Atleta.java                 ← herança multinível + polimorfismo
│   │   ├── Historico.java              ← componente da composição
│   │   ├── SistemaIMC.java             ← composição + orquestração
│   │   ├── CalculadoraRecursiva.java   ← recursão (potência) e função simples
│   │   ├── EntradaInvalidaException.java ← exceção personalizada
│   │   └── Main.java                   ← ponto de entrada + menu CLI
│   └── test/java/
│       └── CalculadoraIMCTest.java     ← suíte JUnit 5
├── pom.xml                              ← gerenciamento de dependências
├── .gitignore                           ← arquivos ignorados pelo Git
└── README.md                            ← este arquivo
```

---

## 🧠 Conceitos de POO aplicados

| Conceito | Onde aparece no código |
|---|---|
| **Tipos de dados** (int, double, String, boolean) | `Main.java` (variáveis `idade`, `peso`, `altura`, `nome`, `houveCadastro`) |
| **Operadores** aritméticos, relacionais e lógicos | `Pessoa.calcularIMC()` (aritméticos), `Pessoa.classificarIMC()` (relacionais) |
| **Controle de fluxo** | Loop `while` do menu e `switch` das opções em `Main.java`; `if/else if` em todas as classificações |
| **Funções com responsabilidade única** | Métodos `lerInt`, `lerDouble`, `lerString` em `Main`; cada classe com papel claro |
| **Recursão** | `CalculadoraRecursiva.potencia()` com caso-base (`exp == 0`) e passo recursivo |
| **Interface** | `CalculadoraIMC.java`, implementada por `Pessoa` |
| **Classe abstrata** | `PessoaBase.java` com método abstrato `exibirPerfil()` e métodos concretos `getNome`/`getIdade` |
| **Encapsulamento** | Atributos `private` em `Pessoa` com getters e setter validador (lança exceção se peso/altura ≤ 0) |
| **Herança** | `Pessoa extends PessoaBase` reaproveitando atributos via `super()` |
| **Herança multinível** | `Atleta extends Pessoa extends PessoaBase` (3 níveis) |
| **Polimorfismo** | `Atleta` sobrescreve `classificarIMC` com `@Override`; `SistemaIMC.processar()` chama o método sem saber o tipo concreto — a JVM despacha em runtime |
| **Composição** | `SistemaIMC` *tem-um* `Historico` como atributo (não herda) |
| **Exceção personalizada** | `EntradaInvalidaException extends RuntimeException`, lançada em validações e capturada em `Main` com `try/catch` em todo bloco de leitura |
| **Gerenciamento de dependências** | `pom.xml` com Maven; dependência externa: JUnit Jupiter 5.10.2 |

---

## 📦 Dependência externa

O projeto declara **JUnit Jupiter 5.10.2** como dependência de teste no
`pom.xml`. A escolha foi feita porque:

1. É uma **dependência real** (não um placeholder vazio).
2. É **utilizada de fato** — a suíte em `src/test/java` cobre 12 cenários.
3. É a sugestão explícita do enunciado da Sistematização.
4. Permite que o avaliador verifique objetivamente, com `mvn test`, que
   cada conceito de POO está funcional — não só presente.

---

## 👤 Autor

Atividade individual / em grupo (até 5) da disciplina POO — Turma B.

**Disciplina:** Programação Orientada a Objetos
**Professor:** Prof. Romes
**Instituição:** CEUB — Centro Universitário de Brasília
