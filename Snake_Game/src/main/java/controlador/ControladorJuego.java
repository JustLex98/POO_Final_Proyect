package controlador;

import modelo.*;
import persistencia.DatabaseManager; // <-- NUEVO: Importamos el gestor de la BD
import vista.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime; // <-- NUEVO: Para manejar fechas y horas

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
    private boolean enJuego = false;
    private int puntaje = 0;

    private DatabaseManager dbManager; // Instancia del gestor de BD
    private String pseudonimoJugadorActual; // Para saber quién está jugando
    private LocalDateTime horaInicioJuego; // Para registrar cuándo empezó la partida

    public ControladorJuego() {
        this.dbManager = new DatabaseManager(); 

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
        

        LocalDateTime horaFinJuego = LocalDateTime.now(); 

        if (pseudonimoJugadorActual != null && !pseudonimoJugadorActual.isEmpty()) {
            System.out.println("Guardando partida para el jugador: " + pseudonimoJugadorActual);
            dbManager.guardarPartida(pseudonimoJugadorActual, horaInicioJuego, horaFinJuego, puntaje);

            System.out.println("--- Reportes Post-Partida ---");
            System.out.println(dbManager.getReporteMaxPuntaje());
            System.out.println(dbManager.getReporteMasJuegos());
        } else {
            System.out.println("No se guardó la partida porque no se definió un jugador.");
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (!enJuego && timer.isRunning()) {
            enJuego = true;

            this.pseudonimoJugadorActual = "PlayerTest";
            this.horaInicioJuego = LocalDateTime.now();

            dbManager.registrarJugador("Jugador de Prueba", this.pseudonimoJugadorActual);
            
            System.out.println("Iniciando partida para: " + this.pseudonimoJugadorActual);
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