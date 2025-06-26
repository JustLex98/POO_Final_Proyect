package modelo;

public class Punto {
    private int x;
    private int y;

    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters para poder leer las coordenadas desde otras clases
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Método útil para comparar dos puntos. Lo usaremos mucho.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Punto punto = (Punto) obj;
        return x == punto.x && y == punto.y;
    }
}