package AtividadeF1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Carro extends Thread {
    private String nome;
    private int distanciaPercorrida = 0;
    private static final int DISTANCIA_TOTAL = 450; // 450 metros (exemplo)
    private static final Random random = new Random();
    private static List<String> ordemChegada = Collections.synchronizedList(new ArrayList<>());
    private int velocidadeAtual;

    public Carro(String nome) {
        this.nome = nome;
        this.velocidadeAtual = 80 + random.nextInt(10);
    }

    @Override
    public void run() {
        while (distanciaPercorrida < DISTANCIA_TOTAL) {
            try {
                int chance = random.nextInt(100);

                // Eventos aleatórios
                if (chance < 2) {
                    System.out.println(nome + " teve problemas técnicos e abandonou a corrida!");
                    return;
                } else if (chance < 7) {
                    System.out.println(nome + " entrou nos boxes (velocidade reduzida a 5 m/s)");
                    velocidadeAtual = 20; // Velocidade no box
                    Thread.sleep(3000); // Tempo no box
                    velocidadeAtual = 80 + random.nextInt(15); // Retorna à corrida
                } else if (chance < 10) {
                    System.out.println(nome + " perdeu velocidade por causa de um erro!");
                    velocidadeAtual = Math.max(10, velocidadeAtual - 5);
                    Thread.sleep(1000);
                }
                else if (chance < 15 && !CorridaF1.isSafetyCarAtivo()) { 
                    System.out.println("SAFETY CAR NA PISTA!");
                    CorridaF1.ativarSafetyCar();
                    new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                            CorridaF1.desativarSafetyCar();
                            System.out.println("SAFETY CAR VAZOU!");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }

                // Efeito do Safety Car
                if (CorridaF1.isSafetyCarAtivo()) {
                    velocidadeAtual = Math.min(velocidadeAtual, 20); // Limite de 80 km/h (~22 m/s)
                } else {
                    velocidadeAtual += random.nextInt(15) - 2; // -2 a +2 m/s de variação
                    velocidadeAtual = Math.max(60, Math.min(120, velocidadeAtual));
                }

                // Atualiza posição
                distanciaPercorrida += velocidadeAtual;
                
                // Mostra velocidade atual em km/h (mais intuitivo)
                int velocidadeKmh = (int) (velocidadeAtual * 3.6);
                System.out.println(nome + ": " + distanciaPercorrida + "m | Velocidade: " + velocidadeKmh + " km/h");
                
                Thread.sleep(1000); // Intervalo de atualização

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (ordemChegada) {
            ordemChegada.add(nome);
        }
        System.out.println(nome + " cruzou a linha de chegada!");
    }

    public static List<String> getOrdemChegada() {
        return ordemChegada;
    }
}