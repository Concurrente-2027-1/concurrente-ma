import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Clase para asignar IDs fijos (0 a 3) a los hilos del Executor
class ThreadID {
    private static final ThreadLocal<Integer> threadID = new ThreadLocal<Integer>() {
        private int nextId = 0;
        @Override
        // Usamos synchronized para asegurar que cada nueva creación de un hilo, se garantize un correcto conteo de IDs
        protected synchronized Integer initialValue() {
            return nextId++;
        }
    };

    public static int get() {
        return threadID.get();
    }
}

public class TestBakeryCuatroHilos {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_THREADS = 4;
        final int NUM_TASKS = 400;

        BakeryCuatroHilos lock = new BakeryCuatroHilos();
        CounterNaive contador = new CounterNaive();
        int[] porHilo = new int[NUM_THREADS];       // Conteo por hilo para el inciso (b)

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int t = 0; t < NUM_TASKS; t++) {
            executor.submit(() -> {
                int me = ThreadID.get();
                lock.lock(me);
                try {
                    contador.increment();
                    porHilo[me]++;
                } finally {
                    lock.unlock(me);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Valor final del contador: " + contador.getValue());
        for (int i = 0; i < NUM_THREADS; i++) {
            System.out.println("Hilo id " + i + " incrementó " + porHilo[i] + " veces");
        }
    }
}