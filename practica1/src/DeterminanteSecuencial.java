//package unam.fc.concurrent.practica1;
/*	Ejercicio 5: Determinante Secuencual removiendo la herencia de Threads y la implementación
    de Runnable, con el fin de realizar cada paso de manera scuencial sin romper la lógica de
    del modelado de clases y objetos visto en las implementaciones Concurrentes.
*/

public class DeterminanteSecuencial {
    static int determinante;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    int num1, num2, num3, partial;
    
    public DeterminanteSecuencial(int num1, int num2, int num3) {
		this.num1 = num1;
		this.num2 = num2;
		this.num3 = num3;
	}
    
    public static int determinanteMatriz3x3(int matriz[][]) {
        int result = 0;
        DeterminanteSecuencial p1 = new DeterminanteSecuencial(matriz[0][0], matriz[1][1], matriz[2][2]);
        DeterminanteSecuencial p2 = new DeterminanteSecuencial(matriz[1][0], matriz[2][1], matriz[0][2]);
        DeterminanteSecuencial p3 = new DeterminanteSecuencial(matriz[2][0], matriz[0][1], matriz[1][2]);
        DeterminanteSecuencial p4 = new DeterminanteSecuencial(matriz[2][0], matriz[1][1], matriz[0][2]);
        DeterminanteSecuencial p5 = new DeterminanteSecuencial(matriz[1][0], matriz[0][1], matriz[2][2]);
        DeterminanteSecuencial p6 = new DeterminanteSecuencial(matriz[0][0], matriz[2][1], matriz[1][2]);
        
        p1.calculatePartial();
        p2.calculatePartial();
        p3.calculatePartial();
        p4.calculatePartial();
        p5.calculatePartial();
        p6.calculatePartial();

        result = p1.partial + p2.partial + p3.partial - p4.partial - p5.partial - p6.partial;
       
        return result;
    }
    
    public void calculatePartial(){
    	this.partial = this.num1 * this.num2 * this.num3;
    }

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		determinante = determinanteMatriz3x3(matriz_prueba);
		long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante) ;
	}

}
