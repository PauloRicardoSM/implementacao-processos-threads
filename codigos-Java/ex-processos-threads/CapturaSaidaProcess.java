package exProcessThreads; // Define o pacote onde a classe está localizada

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Classe principal que demonstra como capturar a saída de um processo externo
public class CapturaSaidaProcess {

	public static void main(String[] args) throws IOException {
		// Bloco try-catch para tratar exceções que podem ocorrer ao iniciar e manipular o processo externo
		try {
            // Cria um ProcessBuilder com o comando a ser executado externamente
            // Neste caso, é o comando do Windows "ping -n 3 google.com" que envia 3 pacotes para o Google
            ProcessBuilder pb = new ProcessBuilder("ping", "-n", "3", "google.com");

            // Inicia a execução do processo
            Process process = pb.start();

            // Cria um BufferedReader para ler a saída padrão (stdout) do processo
            // process.getInputStream() obtém a saída do processo em bytes
            // InputStreamReader converte os bytes em caracteres
            // BufferedReader lê linha por linha essa saída convertida
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            // Lê e imprime cada linha da saída do processo até que não haja mais linhas (null)
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Exibe a linha capturada no console
            }

            // Aguarda o processo terminar e obtém o código de saída
            int exitCode = process.waitFor(); // 0 normalmente significa sucesso
            System.out.println("Processo finalizado com código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            // Captura e imprime qualquer exceção que ocorra durante a execução do processo
            e.printStackTrace();
        }
	}
}
