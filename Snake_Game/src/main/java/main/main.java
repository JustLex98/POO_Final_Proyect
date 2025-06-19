package main;

import controlador.ControladorJuego;
import vista.GameFrame;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorJuego controlador = new ControladorJuego();
            new GameFrame(controlador); 
        });
    }
}