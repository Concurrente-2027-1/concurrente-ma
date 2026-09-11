package src;

/*  Ejercicio 2: Pruebas para ColaConcurrente.
 *  Mediante varias simulaciones, se pondrá a prueba el prueba el programa de ConlaConcurrente.
 *  El objetivo es buscar errores basados en lo que pide el ejercicio.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PruebaColaConcurrente {

    public static void main(String[] args) {
        ColaConcurrente cola = new ColaConcurrente();
        
        // Este Executor simulará 10 hilos de un programa intentando acceder a la cola al mismo tiempo
        ExecutorService hilosSimulados = Executors.newFixedThreadPool(10);
        
        // Listas para guardar los Futures y no bloquear la ejecución tal como menciona el Hint
        List<Future<Boolean>> futuresEnq = new ArrayList<>();
        List<Future<String>> futuresDeq = new ArrayList<>();

        System.out.println("--- Iniciando pruebas de cola concurrente ---");

        // Creamos 10 tareas concurrentes para encolar (enq)
        for (int i = 1; i <= 10; i++) {
            final String valor = "Element-" + i;
            futuresEnq.add(hilosSimulados.submit(() -> cola.enq(valor)));
        }

        // Simulamos tiempo de espera
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        System.out.println("\nEstado de la cola después de 10 enq() concurrentes:");
        cola.print();

        // Creamos 5 tareas concurrentes para desencolar (deq)
        for (int i = 0; i < 5; i++) {
            futuresDeq.add(hilosSimulados.submit(() -> cola.deq()));
        }

        // Leemos los resultados de los Futures usando get()
        try {
            System.out.println("\nElementos desencolados (deq):");
            for (Future<String> f : futuresDeq) {
                // Obtenemosel valor real del future, o esperamos si el hilo aún no termina su deq()
                System.out.println(f.get()); 
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("\nEstado final de la cola:");
        cola.print();

        // Apagamos los ejecutores
        hilosSimulados.shutdown();
        cola.shutdown();
    }
}