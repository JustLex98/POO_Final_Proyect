package modelo;

import java.util.Random;

public class Comida {
    private Punto posicion;
    private Random random;

    public Comida(Punto posicionInicial) {
        this.posicion = posicionInicial;
        this.random = new Random();
    }

    public Punto getPosicion() {
        return posicion;
    }

    // Este método es clave para que la comida reaparezca en otro lugar.
    // Recibe el ancho y el alto del tablero para no salirse de los límites.
    public void generarNuevaPosicion(int anchoTablero, int altoTablero) {
        int x = random.nextInt(anchoTablero);
        int y = random.nextInt(altoTablero);
        this.posicion = new Punto(x, y);
    }
}