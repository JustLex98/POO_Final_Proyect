package controlador;

import modelo.*;
import persistencia.DatabaseManager;
import vista.GameFrame;
import vista.PanelJuego;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;

public class ControladorJuego implements ActionListener, KeyListener {

    private static final int ANCHO_TABLERO = 34;
    private static final int ALTO_TABLERO = 23;
    private static final int DELAY = 150;
    private static final int DELAY_MINIMO = 60;
    private static final int DECREMENTO_POR_PUNTO = 2;

    private Tablero tablero;
    private Serpiente serpiente;
    private Comida comida;
    
    private PanelJuego panelJuego;

    private Timer timer;
    private boolean enJuego = false;
    private boolean isPaused = false;
    private boolean haCambiadoDireccion = false;
    private int puntaje = 0;

    private DatabaseManager dbManager;
    private String pseudonimoJugadorActual;
    private LocalDateTime horaInicioJuego;

    public ControladorJuego() {
        this.dbManager = new DatabaseManager();
        this.tablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO);
        this.serpiente = new Serpiente(new Punto(0, 0));
        this.comida = new Comida(new Punto(0, 0));
        this.timer = new Timer(DELAY, this);
    }

    public void iniciarNuevaPartida(String pseudonimo) {
        this.pseudonimoJugadorActual = pseudonimo;
        this.horaInicioJuego = LocalDateTime.now();
        this.puntaje = 0;
        this.isPaused = false;
        this.haCambiadoDireccion = false;
        
        timer.setDelay(DELAY);

        this.serpiente = new Serpiente(new Punto(ANCHO_TABLERO / 2, ALTO_TABLERO / 2));
        generarNuevaComida();

        enJuego = false;
        timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (enJuego) {
            serpiente.mover();
            this.haCambiadoDireccion = false;
            verificarColisiones();
            verificarComida();
        }
        if (panelJuego != null) {
            panelJuego.repaint();
        }
    }

    private void verificarComida() {
        if (serpiente.getCabeza().equals(comida.getPosicion())) {
            puntaje++;
            serpiente.crecer();
            generarNuevaComida();
            panelJuego.reproducirSonidoComer();
            ajustarVelocidad();
        }
    }

    private void ajustarVelocidad() {
        int nuevoDelay = DELAY - (puntaje * DECREMENTO_POR_PUNTO);
        timer.setDelay(Math.max(nuevoDelay, DELAY_MINIMO));
    }

    private void generarNuevaComida() {
        do {
            comida.generarNuevaPosicion(tablero.getAncho(), tablero.getAlto());
        } while (serpiente.getCuerpo().contains(comida.getPosicion()));
    }

    private void verificarColisiones() {
        if (serpiente.verificarColisionPropia() || tablero.verificarColisionPared(serpiente.getCabeza())) {
            finDelJuego();
        }
    }

    private void finDelJuego() {
        enJuego = false;
        timer.stop();
        panelJuego.reproducirSonidoChocar();
        
        LocalDateTime horaFinJuego = LocalDateTime.now();
        if (pseudonimoJugadorActual != null && !pseudonimoJugadorActual.isEmpty()) {
            dbManager.guardarPartida(pseudonimoJugadorActual, horaInicioJuego, horaFinJuego, puntaje);
        }

        JOptionPane.showMessageDialog(panelJuego, "¡Game Over! Puntaje final: " + puntaje, "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        
        GameFrame frame = (GameFrame) SwingUtilities.getWindowAncestor(panelJuego);
        if (frame != null) {
            frame.volverAlMenu();
        }
    }

    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) { timer.stop(); } else { timer.start(); }
        if (panelJuego != null) { panelJuego.repaint(); }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_P) {
            if (enJuego) { togglePause(); }
            return;
        }

        if (isPaused) { return; }

        if (!enJuego && timer.isRunning()) {
            enJuego = true;
        }

        if (!haCambiadoDireccion) {
            switch (key) {
                case KeyEvent.VK_LEFT:
                    if (serpiente.setDireccion(Direccion.IZQUIERDA)) haCambiadoDireccion = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (serpiente.setDireccion(Direccion.DERECHA)) haCambiadoDireccion = true;
                    break;
                case KeyEvent.VK_UP:
                    if (serpiente.setDireccion(Direccion.ARRIBA)) haCambiadoDireccion = true;
                    break;
                case KeyEvent.VK_DOWN:
                    if (serpiente.setDireccion(Direccion.ABAJO)) haCambiadoDireccion = true;
                    break;
            }
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    public void setPanelJuego(PanelJuego panel) {
        this.panelJuego = panel;
        this.panelJuego.addKeyListener(this);
        this.panelJuego.setFocusable(true);
    }
    
    public boolean isEnJuego() { return enJuego; }
    public boolean isPaused() { return isPaused; }
    public int getPuntaje() { return puntaje; }
    public Serpiente getSerpiente() { return this.serpiente; }
    public Comida getComida() { return this.comida; }
}