// Ejercicio 1
// Prueba el candado DoublePeterson con 4 hilos y 400 tareas sobre un Contador

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestDoublePeterson {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_THREADS = 4;
        final int NUM_TASKS = 400;

        DoublePeterson lock = new DoublePeterson();
        Contador contador = new Contador();
        int[] porHilo = new int[NUM_THREADS]; // conteo por hilo para el inciso (b)

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int t = 0; t < NUM_TASKS; t++) {
            executor.submit(() -> {
                int me = DoublePeterson.getThreadId(); // 0..3, fijo por hilo del pool
                lock.lock();
                try {
                    contador.incrementar();
                    porHilo[me]++;
                } finally {
                    lock.unlock();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Valor final del contador: " + contador.getValor());
        for (int i = 0; i < NUM_THREADS; i++) {
            System.out.println("Hilo id " + i + " incrementó " + porHilo[i] + " veces");
        }
    }
}