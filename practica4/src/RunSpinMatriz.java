package unam.fc.concurrent.practica4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import unam.fc.concurrent.practica4.*;

/*
Programa 3: Se toma como base el Programa 2 (RunSpin), pero en lugar de un contador
utilzamos una matriz 10x10 para emular tareas más pesada y para medir el tiempo de:
//		Lock lock = new TASLock();
//		Lock lock = new TTASLock();
//		Lock lock = new BackoffLock();
//		Lock lock = new MCSLock();
//		Lock lock = new ALock(numberThreads);
//		Lock lock = new ReentrantLock();
//		Lock lock = new CLHLock();
*/

public class RunSpinMatriz {
	static int[][] matriz = new int[10][10];
	
	// Método para la asignación de valores
    public static void asignarMatriz() {
        long id = Thread.currentThread().getId();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
				// Elegimos llenar los valores de la matriz con el ID del hilo para distinguir quién realizó la tarea.
                matriz[i][j] = (int) id;
            }
        }
    }

	// Método para la impresión de la matriz
    public static void imprimirMatriz() {
        System.out.println("Matriz del hilo: " + Thread.currentThread().getId());
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }
	
	// Modificación para la tarea pesada
    private static int task(Lock lock) {
        try {
            lock.lock();
            // Ahora la Sección Crítica realiza un trabajo más pesado
            asignarMatriz();
            imprimirMatriz();
        } finally {
            lock.unlock();		
        }
        return 1; // Retorno dummy para cumplir con la firma del Future
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		int numberThreads = 4;
		ExecutorService executor = Executors.newFixedThreadPool(numberThreads);
		Lock lock = new TASLock();
//		Lock lock = new TTASLock();
//		Lock lock = new BackoffLock();
//		Lock lock = new MCSLock();
//		Lock lock = new ALock(numberThreads);
//		Lock lock = new CLHLock();
//		Lock lock = new ReentrantLock();
		
		long startTime = System.nanoTime();//Start time
		for(int i = 0; i < 100; i++) {
			futures.add(executor.submit(() -> task(lock)));
		}
		executor.shutdown();
		
		for (int i = 0; i < futures.size(); i++) {
            while(!futures.get(i).isDone()){}; // Comprobar que todas las tareas terminen
		}
		long endTime = System.nanoTime();//Finish time
		
		
        System.out.println("Program took " +
                (endTime - startTime)*0.000001 + "ms") ; //En milisegundos
	}
	

}
