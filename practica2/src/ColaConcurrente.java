package src;

/*  Ejercicio 1: ColaConcurrente que utiliza la clase Nodo.
 *  Implementa una cola concurrente utilizando una pool de hilos (ExecutorService).
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ColaConcurrente {
    private Nodo head;
    private Nodo tail;

    // Creamos un "candado" con un pool de un solo hilo
    private final ExecutorService executor;

    public ColaConcurrente() {
        this.head  = new Nodo("hnull");
        this.tail  = new Nodo("tnull");
        this.head.next = this.tail;
        
        // Inicializamos el pool solamente con 1 hilo.
        this.executor = Executors.newFixedThreadPool(1);
    }

    public Boolean enq(String x) {
        // Ponemos toda la sección crítica dentro del Callable para convertirla en una "tarea".
        // De esta forma encapsulamos la ejecución de este código mediante una función lambda.
        Callable<Boolean> tareaEnq = () -> {
            Nodo newnode = new Nodo(x);
            if(this.head.next == this.tail) {
                newnode.next = this.tail;
                this.head.next = newnode;
            } else {
                Nodo last =  this.tail.next;
                newnode.next = this.tail;
                last.next = newnode;
            }
            this.tail.next = newnode;
            return true;
        };

        try {
            // Mandamos la tarea al executor y esperamos el resultado
            Future<Boolean> future = executor.submit(tareaEnq);
            return future.get(); // El hilo se mantiene esperando hasta que la tarea termine
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String deq() {
        // Análogo a enq(), encapsulamos esta CS.
        Callable<String> tareaDeq = () -> {
            if(this.head.next == this.tail) {
                return "empty";
            }
            Nodo first = this.head.next;
            this.head.next = first.next;
            return first.item;
        };

        try {
            // Mandamos la tarea al executor y esperamos el resultado
            Future<String> future = executor.submit(tareaDeq);
            return future.get(); // El hilo se mantiene ewsperando hasta que la tarea termine
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void print() {
        // Como este método no devuelve nada, usamos un Runnable con execute()
        executor.execute(() -> {
            System.out.println("Print ");
            Nodo pred = this.head;
            Nodo curr = pred.next;
            System.out.println(pred.item);
            
            while (!curr.item.equals("tnull")) {
                pred = curr;
                curr = curr.next;
                System.out.println(pred.item);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}