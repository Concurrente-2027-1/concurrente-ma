/*
Programa para obtener el número de hilos de nuestra computadora. Ejercicio 2
*/

public class Ejercicio2 {
    public static void main(String[] args) {
        System.out.println("Hilos disponibles: " + Runtime.getRuntime().availableProcessors());
    }
}