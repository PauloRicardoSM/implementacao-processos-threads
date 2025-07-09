package exProcessThreads;

import java.io.IOException;

public class ExecComandoExterno {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		ProcessBuilder pb = new ProcessBuilder("notepad.exe"); //ProcessBuilder executa um processo fora da JVM
		Process process = pb.start(); // Inicia o processo
		
		Thread.sleep(5000);
		// Formas de encerrar um processo
//		process.destroy(); // encerra processos no terminal principalmente
//	process.destroyForcibly(); // força o encerramento de processos no terminal principalmente
//		new ProcessBuilder("taskkill", "/F", "/IM", "notepad.exe").start(); // encerra processos fora da JVM, com GUI
	}

}
