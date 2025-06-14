package controlador;

import modelo.*;
import vista.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControladorJuego implements ActionListener, KeyListener {

    private static final int ANCHO_TABLERO = 34;
    private static final int ALTO_TABLERO = 23;
    private static final int DELAY = 150;

    // --- ATRIBUTOS ---
    
    // Modelo
    private Tablero tablero;
    private Serpiente serpiente;
    private Comida comida;

    // Vista
    private VentanaPrincipal ventana;
    private PanelJuego panelJuego;

    // Controlador
    private Timer timer;
    private boolean enJuego = false; // El juego no empieza hasta que se presione una tecla
    private int puntaje = 0;

    public ControladorJuego() {
        this.tablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO);
        this.serpiente = new Serpiente(new Punto(ANCHO_TABLERO / 2, ALTO_TABLERO / 2));
        this.comida = new Comida(new Punto(5, 5));
        generarNuevaComida(); 

        this.panelJuego = new PanelJuego(serpiente, comida, tablero.getAncho(), tablero.getAlto(), this);
        this.ventana = new VentanaPrincipal(panelJuego);

        this.panelJuego.addKeyListener(this); 
        this.panelJuego.setFocusable(true);
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (enJuego) {
            serpiente.mover();
            verificarColisiones();
            verificarComida();
        }
        panelJuego.repaint();
    }

    private void verificarComida() {
        if (serpiente.getCabeza().equals(comida.getPosicion())) {
            puntaje++;
            serpiente.crecer();
            generarNuevaComida();
            ventana.setTitle("Snake Game | Puntaje: " + puntaje);
        }
    }

    private void generarNuevaComida() {
        do {
            comida.generarNuevaPosicion(tablero.getAncho(), tablero.getAlto());
        } while (serpiente.getCuerpo().contains(comida.getPosicion()));
    }

    private void verificarColisiones() {
        if (serpiente.verificarColisionPropia()) {
            finDelJuego();
        }
        if (tablero.verificarColisionPared(serpiente.getCabeza())) {
            finDelJuego();
        }
    }

    private void finDelJuego() {
        enJuego = false;
        timer.stop();
        // Aquí es donde, en el futuro, llamarías al método para guardar en la BD.
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!enJuego && timer.isRunning()) {
            enJuego = true;
        }

        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                serpiente.setDireccion(Direccion.IZQUIERDA);
                break;
            case KeyEvent.VK_RIGHT:
                serpiente.setDireccion(Direccion.DERECHA);
                break;
            case KeyEvent.VK_UP:
                serpiente.setDireccion(Direccion.ARRIBA);
                break;
            case KeyEvent.VK_DOWN:
                serpiente.setDireccion(Direccion.ABAJO);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public boolean isEnJuego() {
        return enJuego;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
