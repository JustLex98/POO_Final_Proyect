// Paquete: com.snakegame.modelo
package modelo;

public class Tablero {
    // Las dimensiones están en "unidades" o "celdas", no en píxeles.
    private final int ancho;
    private final int alto;

    public Tablero(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public boolean verificarColisionPared(Punto punto) {
        int x = punto.getX();
        int y = punto.getY();

        return x < 0 || x >= ancho || y < 0 || y >= alto;
    }
}