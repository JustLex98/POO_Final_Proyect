package main;

import controlador.ControladorJuego;
import vista.GameFrame;
import javax.swing.SwingUtilities;
import Musica.EfectosSonidos;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorJuego controlador = new ControladorJuego();
            EfectosSonidos sonido = new EfectosSonidos();
            new GameFrame(controlador, sonido); 
        });
    }
}