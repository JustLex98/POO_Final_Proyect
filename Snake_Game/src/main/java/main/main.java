package main;

import controlador.ControladorJuego;
import javax.swing.SwingUtilities;

public class main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorJuego controladorJuego = new ControladorJuego();
        });
    }
}
