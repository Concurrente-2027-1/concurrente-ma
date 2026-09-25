// Ejercicio 1

interface Lock {
    void lock(int id);
    void unlock(int id);
}

/**
 * Candado clásico de Peterson para 2 hilos (id = 0 o 1).
 * Usa flag[] y victim volatile para resolver Visibilidad y Reordenamiento (JMM).
 */
class Peterson implements Lock {
    private final boolean[] flag = new boolean[2];
    private volatile int victim;

    /**
     * Bloquea la sección crítica para el hilo
     *
     * @param me id local del hilo que solicita el candado (0 o 1)
     */
    @Override
    public void lock(int me) {
        int other = 1 - me;
        flag[me] = true;
        victim = me;
        while (flag[other] && victim == me) {
            // espera activa mientras el otro quiera entrar 
        }
    }

    /**
     * Libera la sección crítica para el hilo 
     *
     * @param me id local del hilo que libera el candado (0 o 1)
     */
    @Override
    public void unlock(int me) {
        flag[me] = false;
    }
}

/**
 * Cada hilo recibe un id fijo (0..3) la primera vez que participa, mediante
 * ThreadLocal y un contador 
 */
class DoublePeterson {
    private final Peterson lock1 = new Peterson(); // hilos {0,1}
    private final Peterson lock2 = new Peterson(); // hilos {2,3}
    private final Peterson lock3 = new Peterson(); // hilos que pasan de los candados antwriores

    private static int nextIdCounter = 0;

    private static synchronized int assignNextId() {
        int id = nextIdCounter;
        nextIdCounter = (nextIdCounter + 1) % 4;
        return id;
    }

    private static final ThreadLocal<Integer> threadId =
            ThreadLocal.withInitial(DoublePeterson::assignNextId);

    /**
     * Nos va a ayudar para el conteo por hilo de la pregunta b
     *
     * @return el id local (0..3) del hilo actual
     */
    public static int getThreadId() {
        return threadId.get();
    }

    /**
     * Bloquea la sección crítica y aquí es donde se define qué 
     * hilo pasa 
     */
    public void lock() {
        int me = threadId.get();

        if (me == 0 || me == 1) {
            lock1.lock(me);
        }
        if (me == 2 || me == 3) {
            lock2.lock(me - 2);
        }
        int group = (me == 0 || me == 1) ? 0 : 1;
        lock3.lock(group);
    }

    public void unlock() {
        int me = threadId.get();
        int group = (me == 0 || me == 1) ? 0 : 1;

        lock3.unlock(group);
        if (me == 0 || me == 1) {
            lock1.unlock(me);
        }
        if (me == 2 || me == 3) {
            lock2.unlock(me - 2);
        }
    }
}


class Contador {
    private volatile int valor = 0;

    public void incrementar() {
        int temp = valor;
        temp = temp + 1;
        valor = temp;
    }

    public int getValor() {
        return valor;
    }
}