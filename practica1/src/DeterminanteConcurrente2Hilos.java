/*  Ejercicio 6: Determinante Concurrente con 2 hilos en vez de 6.
 *  Un hilo calcula la suma de los 3 productos positivos (diagonales principales),
 *  el otro calcula la suma de los 3 productos negativos (diagonales secundarias).
*/

public class DeterminanteConcurrente2Hilos extends Thread {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 } };
    
    /*
    Como solo tenemos dos hilos tendremos dos triadas que justo serían num1, num2, num3
    */
    int[][] triadas;
    int partial;

    public DeterminanteConcurrente2Hilos(int[][] triadas) {
        this.triadas = triadas;
    }

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        int result = 0;

        /*
        El primer hilo tomará únicamente los positivos
        */
        int[][] positivos = {
            { matriz[0][0], matriz[1][1], matriz[2][2] },
            { matriz[1][0], matriz[2][1], matriz[0][2] },
            { matriz[2][0], matriz[0][1], matriz[1][2] }
        };

        /*
        El segundo hilo tomará únicamente los negativos
        */
        int[][] negativos = {
            { matriz[2][0], matriz[1][1], matriz[0][2] },
            { matriz[1][0], matriz[0][1], matriz[2][2] },
            { matriz[0][0], matriz[2][1], matriz[1][2] }
        };

        DeterminanteConcurrente2Hilos thrPos = new DeterminanteConcurrente2Hilos(positivos);
        DeterminanteConcurrente2Hilos thrNeg = new DeterminanteConcurrente2Hilos(negativos);

        thrPos.start();
        thrNeg.start();

        try {
            thrPos.join();
            thrNeg.join();
        } catch (InterruptedException e) {}

        result = thrPos.partial - thrNeg.partial;

        return result;
    }

    public void run() {
        int suma = 0;
        for (int[] triada : this.triadas) {
            suma += triada[0] * triada[1] * triada[2];
        }
        this.partial = suma;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}