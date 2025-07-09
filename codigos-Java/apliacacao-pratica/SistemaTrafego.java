package implementacaoPratica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

// Classe que representa um cruzamento com semáforo
class Cruzamento {
    // Semáforo com 1 permissão — apenas um carro pode acessar o cruzamento por vez
    private final Semaphore semaforo = new Semaphore(1);

    // Método que simula um carro tentando atravessar o cruzamento
    public long atravessar(String nomeCarro) {
        long inicio = System.currentTimeMillis(); // Marca o tempo em que o carro começa a esperar

        try {
            System.out.println(nomeCarro + " aguardando cruzamento...");

            // Tenta adquirir permissão para entrar no cruzamento
            semaforo.acquire();

            // Calcula quanto tempo o carro esperou para conseguir atravessar
            long tempoEspera = System.currentTimeMillis() - inicio;

            System.out.println(nomeCarro + " está atravessando (esperou " + tempoEspera + " ms)");

            // Simula o tempo necessário para o carro atravessar (entre 500ms e 1000ms)
            Thread.sleep(500 + new Random().nextInt(500));

            // Retorna o tempo que o carro esperou
            return tempoEspera;

        } catch (InterruptedException e) {
            // Caso a thread seja interrompida, define que houve erro retornando -1
            Thread.currentThread().interrupt();
            return -1;
        } finally {
            // Libera o semáforo permitindo que o próximo carro possa atravessar
            System.out.println(nomeCarro + " saiu do cruzamento.");
            semaforo.release();
        }
    }
}

// Classe que representa um carro, sendo uma thread que executa o método atravessar
class Carro extends Thread {
    private final Cruzamento cruzamento;          // Cruzamento que o carro tentará atravessar
    private final List<Long> temposDeEspera;      // Lista compartilhada para registrar o tempo de espera de cada carro

    // Construtor do carro: recebe o cruzamento, o nome da thread e a lista de tempos
    public Carro(Cruzamento c, String nome, List<Long> tempos) {
        super(nome); // Define o nome da thread (o carro)
        this.cruzamento = c;
        this.temposDeEspera = tempos;
    }

    // O que a thread executa quando iniciada
    public void run() {
        // Chama o método atravessar e pega o tempo de espera
        long tempo = cruzamento.atravessar(getName());

        // Se não houve erro, adiciona o tempo de espera à lista compartilhada
        if (tempo >= 0) {
            synchronized (temposDeEspera) { // Sincroniza para evitar concorrência na lista
                temposDeEspera.add(tempo);
            }
        }
    }
}

// Classe principal que simula um sistema de tráfego com múltiplos cruzamentos e carros
public class SistemaTrafego {
    public static void main(String[] args) throws InterruptedException {
        int numCruzamentos = 3;             // Número de cruzamentos independentes
        int carrosPorCruzamento = 5;        // Quantos carros por cruzamento serão criados

        // Lista para armazenar os cruzamentos
        List<Cruzamento> cruzamentos = new ArrayList<>();
        for (int i = 0; i < numCruzamentos; i++) {
            cruzamentos.add(new Cruzamento());
        }

        // Lista sincronizada onde serão armazenados os tempos de espera de todos os carros
        List<Long> temposTotais = Collections.synchronizedList(new ArrayList<>());

        // Lista de threads dos carros
        List<Thread> carros = new ArrayList<>();

        // Criação e inicialização das threads dos carros
        for (int i = 0; i < numCruzamentos; i++) {
            for (int j = 0; j < carrosPorCruzamento; j++) {
                // Nome do carro será algo como "Carro C1-0", "Carro C2-3", etc.
                Carro c = new Carro(cruzamentos.get(i), "Carro C" + i + "-" + j, temposTotais);
                carros.add(c);  // Adiciona à lista
                c.start();      // Inicia a thread
            }
        }

        // Aguarda todas as threads finalizarem
        for (Thread carro : carros) {
            carro.join();
        }

        // Calcula a média dos tempos de espera após todos os carros terem terminado
        double media = temposTotais.stream().mapToLong(Long::longValue).average().orElse(0);

        // Exibe o tempo médio de espera dos carros
        System.out.println("Tempo médio de espera: " + media + " ms");
    }
}
