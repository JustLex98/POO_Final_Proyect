// Paquete: com.juego.snake.modelo
package modelo;

import java.util.LinkedList;
import java.util.List;

public class Serpiente {
    private LinkedList<Punto> cuerpo; // Usamos LinkedList porque es eficiente para añadir/quitar del principio y del final.
    private Direccion direccionActual;

    public Serpiente(Punto posicionInicial) {
        cuerpo = new LinkedList<>();
        cuerpo.add(posicionInicial); // La cabeza
        cuerpo.add(new Punto(posicionInicial.getX() - 1, posicionInicial.getY())); // Un segmento del cuerpo
        cuerpo.add(new Punto(posicionInicial.getX() - 2, posicionInicial.getY())); // Otro segmento

        this.direccionActual = Direccion.DERECHA; // La serpiente empieza moviéndose a la derecha
    }

    public void mover() {
        Punto cabezaActual = cuerpo.getFirst();
        Punto nuevaCabeza;

        switch (direccionActual) {
            case ARRIBA:
                nuevaCabeza = new Punto(cabezaActual.getX(), cabezaActual.getY() - 1);
                break;
            case ABAJO:
                nuevaCabeza = new Punto(cabezaActual.getX(), cabezaActual.getY() + 1);
                break;
            case IZQUIERDA:
                nuevaCabeza = new Punto(cabezaActual.getX() - 1, cabezaActual.getY());
                break;
            case DERECHA:
            default:
                nuevaCabeza = new Punto(cabezaActual.getX() + 1, cabezaActual.getY());
                break;
        }

        cuerpo.addFirst(nuevaCabeza); // Añadimos la nueva cabeza
        cuerpo.removeLast();         // Quitamos el último segmento de la cola
    }

    public void crecer() {
        // Para crecer, simplemente no quitamos el último segmento de la cola en el próximo movimiento.
        // Una forma sencilla es añadir un duplicado de la cola, que se posicionará correctamente en el siguiente `mover()`.
        cuerpo.addLast(cuerpo.getLast());
    }
    
    public boolean verificarColisionPropia() {
        Punto cabeza = getCabeza();
        // Empezamos en 1 para no comparar la cabeza consigo misma
        for (int i = 1; i < cuerpo.size(); i++) {
            if (cabeza.equals(cuerpo.get(i))) {
                return true; // Ha chocado
            }
        }
        return false;
    }

    // --- Getters y Setters ---

    public Punto getCabeza() {
        return cuerpo.getFirst();
    }

    public List<Punto> getCuerpo() {
        return cuerpo;
    }

    public void setDireccion(Direccion nuevaDireccion) {
        // Regla: No se puede invertir la dirección
        if (direccionActual == Direccion.ARRIBA && nuevaDireccion == Direccion.ABAJO) return;
        if (direccionActual == Direccion.ABAJO && nuevaDireccion == Direccion.ARRIBA) return;
        if (direccionActual == Direccion.IZQUIERDA && nuevaDireccion == Direccion.DERECHA) return;
        if (direccionActual == Direccion.DERECHA && nuevaDireccion == Direccion.IZQUIERDA) return;
        
        this.direccionActual = nuevaDireccion;
    }
}