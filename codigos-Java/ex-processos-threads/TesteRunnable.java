package exProcessThreads;

class MeuRunnable implements Runnable { //Runnable diz o que o thread fará
    public void run() {
    	while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Rodando... " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // espera 1 segundo
            } catch (InterruptedException e) {
                // Se a thread for interrompida durante o sleep, ela sai
                System.out.println("Thread interrompida!");
                break;
            }
    	}
    }
}

public class TesteRunnable {
    public static void main(String[] args) throws InterruptedException {
    	Thread t1 = new Thread(new MeuRunnable());
        t1.start();

        Thread.sleep(5000); // espera 5 segundos
        t1.interrupt();     // sinaliza que a thread deve parar
    }
}
