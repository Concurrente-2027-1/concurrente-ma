/* Ejercicio 2: Agoritmo Bakery para 4 hilos.
 * Utilizamos variables volatile para eliminar los problemas del modelo de memoria de Java (JMM).
 */

public class BakeryCuatroHilos {

    // Para cada arreglo (flag y label), declaramos explicítamente todos sus elementos.
    // Esto con el fin de garantizar lo que se quiere lograr con volatile en cada uno de ellos (read & write).
    private volatile boolean f0, f1, f2, f3;    // Elementos flag: Indica que hilos que quieren un turno
    private volatile int l0, l1, l2, l3;        // Elementos label: El turno asiganado a cada hilo.

    public BakeryCuatroHilos() {
        f0 = false;
        f1 = false;
        f2 = false;
        f3 = false;
        l0 = 0;
        l1 = 0;
        l2 = 0;
        l3 = 0;
    }

    // Método aux para establecer la flag de cada hilo.
    private void setFlag(int id, boolean val) {
        if (id == 0) f0 = val;
        else if (id == 1) f1 = val;
        else if (id == 2) f2 = val;
        else if (id == 3) f3 = val;
    }

    // Método aux para obtener la flag de cada hilo.
    private boolean getFlag(int id) {
        if (id == 0) return f0;
        else if (id == 1) return f1;
        else if (id == 2) return f2;
        return f3;
    }

    // Método aux para establcer la label de cada hilo.
    private void setLabel(int id, int val) {
        if (id == 0) l0 = val;
        else if (id == 1) l1 = val;
        else if (id == 2) l2 = val;
        else if (id == 3) l3 = val;
    }

    // Método aux para obtener la label de cada hilo.
    private int getLabel(int id) {
        if (id == 0) return l0;
        if (id == 1) return l1;
        if (id == 2) return l2;
        return l3;
    }

    public void lock(int id) {
        // Doorway
        setFlag(id, true);
        
        // Calculamos el máximo ticket actual + 1
        int maxLabel = Math.max(Math.max(l0, l1), Math.max(l2, l3));
        setLabel(id, maxLabel + 1);
        
        // Waiting
        for (int k = 0; k < 4; k++) {
            while (k != id && getFlag(k) && 
                  (getLabel(k) < getLabel(id) || (getLabel(k) == getLabel(id) && k < id))) {
                // Espera
                // Como getFlag() y getLabel() acceden a variables individuales volatile,
                // es imposible que los hilos se queden leyendo basura de la caché.
            }
        }
    }

    public void unlock(int id) {
        setFlag(id, false);
    }
}