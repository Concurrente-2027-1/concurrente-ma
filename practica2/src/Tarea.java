//package unam.fc.concurrent.practica2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tarea implements Runnable {
    int tiempoTarea;
    int task;
    final Semaphore smphre;             
    static Lock lock = new ReentrantLock(); 

    public Tarea(int i, Semaphore smphre) {
        this.task = i;
        this.smphre = smphre;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long id = currentThread.getId();
        int value = (int) (id % 6);

        switch (value) {
			  case 0, 2:
				  this.tiempoTarea = 500;
			    break;
			  case 1:
				  this.tiempoTarea = 2000;
			    break;
			  default:
				  this.tiempoTarea = 3000;
			}

        

        boolean restriccion = (value == 0 || value == 2);

        try {
            // Pedimos permiso al semáforo (máximo 3 al mismo tiempo)
            smphre.acquire();
            try {
                // Si el hillo es "0" o "2", entra el candado 
                if (restriccion) {
                    lock.lock();
                }
                try {
                    System.out.println("Running Thread " + value + " task: " + this.task);
                    Thread.sleep(this.tiempoTarea);
                    System.out.println("Running Thread " + value + " time: " + this.tiempoTarea);
                } finally {
                    if (restriccion) {
                        lock.unlock();
                    }
                }
            } finally {
                smphre.release();
            }
        } catch (InterruptedException e) {
            System.out.println(e);
            Thread.currentThread().interrupt();
        }

    }
}