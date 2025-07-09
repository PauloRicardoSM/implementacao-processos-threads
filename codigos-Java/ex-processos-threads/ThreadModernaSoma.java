package exProcessThreads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadModernaSoma {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		Callable<Integer> soma = () -> 2 + 2;
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Integer> resultado = exec.submit(soma);
        System.out.println("Resultado: " + resultado.get());
        exec.shutdown();
	}

}
