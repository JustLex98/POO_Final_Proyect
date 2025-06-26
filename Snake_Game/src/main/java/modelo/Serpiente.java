package modelo;

import java.util.LinkedList;
import java.util.List;

public class Serpiente {
    private LinkedList<Punto> cuerpo;
    private Direccion direccionActual;

    public Serpiente(Punto posicionInicial) {
        cuerpo = new LinkedList<>();
        cuerpo.add(posicionInicial);
        cuerpo.add(new Punto(posicionInicial.getX() - 1, posicionInicial.getY()));
        cuerpo.add(new Punto(posicionInicial.getX() - 2, posicionInicial.getY()));

        this.direccionActual = Direccion.DERECHA;
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

        cuerpo.addFirst(nuevaCabeza);
        cuerpo.removeLast();
    }

    public void crecer() {
        cuerpo.addLast(cuerpo.getLast());
    }
    
    public boolean verificarColisionPropia() {
        Punto cabeza = getCabeza();
        for (int i = 1; i < cuerpo.size(); i++) {
            if (cabeza.equals(cuerpo.get(i))) {
                return true;
            }
        }
        return false;
    }

    public Punto getCabeza() {
        return cuerpo.getFirst();
    }

    public List<Punto> getCuerpo() {
        return cuerpo;
    }

    public boolean setDireccion(Direccion nuevaDireccion) {
        if (direccionActual == Direccion.ARRIBA && nuevaDireccion == Direccion.ABAJO) return false;
        if (direccionActual == Direccion.ABAJO && nuevaDireccion == Direccion.ARRIBA) return false;
        if (direccionActual == Direccion.IZQUIERDA && nuevaDireccion == Direccion.DERECHA) return false;
        if (direccionActual == Direccion.DERECHA && nuevaDireccion == Direccion.IZQUIERDA) return false;
        
        this.direccionActual = nuevaDireccion;
        return true;
    }
}