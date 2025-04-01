package AtividadeF1;

import java.util.ArrayList;
import java.util.List;

public class CorridaF1 {
    private static boolean safetyCarAtivo = false;

    public static synchronized void ativarSafetyCar() {
        safetyCarAtivo = true;
    }

    public static synchronized void desativarSafetyCar() {
        safetyCarAtivo = false;
    }

    public static synchronized boolean isSafetyCarAtivo() {
        return safetyCarAtivo;
    }

    public static void main(String[] args) {
        int numCarros = 5;
        List<Carro> carros = new ArrayList<>();

        for (int i = 1; i <= numCarros; i++) {
            carros.add(new Carro("Carro " + i));
        }

        for (Carro carro : carros) {
            carro.start();
        }

        for (Carro carro : carros) {
            try {
                carro.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!Carro.getOrdemChegada().isEmpty()) {
            System.out.println("\n--- Ordem de chegada ---");
            List<String> ordemFinal = Carro.getOrdemChegada();
            for (int i = 0; i < ordemFinal.size(); i++) {
                System.out.println((i + 1) + "º - " + ordemFinal.get(i));
            }
        } else {
            System.out.println("\nNenhum carro terminou a corrida!");
        }
    }
}