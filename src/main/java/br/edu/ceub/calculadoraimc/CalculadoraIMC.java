package br.edu.ceub.calculadoraimc;

public interface CalculadoraIMC {

    double calcularIMC(double peso, double altura);

    String classificarIMC(double imc);
}
