package br.edu.ceub.calculadoraimc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Historico {

    private final List<String> registros = new ArrayList<>();

    public void adicionar(final String registro) {
        if (registro == null || registro.isBlank()) {
            return;
        }
        registros.add(registro);
    }

    public void exibir() {
        if (registros.isEmpty()) {
            System.out.println("(Nenhum cálculo registrado nesta sessão.)");
            return;
        }
        System.out.println("--- Histórico de cálculos ---");
        int n = 1;
        for (final String r : registros) {
            System.out.printf("%2d. %s%n", n++, r);
        }
        System.out.println("-----------------------------");
    }

    public int tamanho() {
        return registros.size();
    }

    public List<String> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    @Override
    public String toString() {
        return "Historico[" + registros.size() + " registro(s)]";
    }
}
