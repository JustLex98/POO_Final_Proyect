package vista;

import controlador.ControladorJuego;
import modelo.Comida;
import modelo.Punto;
import modelo.Serpiente;

import javax.swing.*;
import java.awt.*;

public class PanelJuego extends JPanel {

    private static final int TAMANO_CELDA = 25;

    private Serpiente serpiente;
    private Comida comida;
    private ControladorJuego controlador;

    public PanelJuego(Serpiente serpiente, Comida comida, int anchoTablero, int altoTablero, ControladorJuego controlador) {
        this.serpiente = serpiente;
        this.comida = comida;
        this.controlador = controlador;

        Dimension tamanoPanel = new Dimension(anchoTablero * TAMANO_CELDA, altoTablero * TAMANO_CELDA);
        setPreferredSize(tamanoPanel);
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (controlador.isEnJuego()) {
            dibujarJuego(g);
        } else {
            mostrarMensaje(g);
        }
    }

    private void dibujarJuego(Graphics g) {
        System.out.println("[dibujarJuego] Empezando a dibujar...");
        if (serpiente == null) {
            System.out.println("[dibujarJuego] ¡ERROR! La serpiente es nula.");
            return;
        }
        java.util.List<Punto> cuerpoSerpiente = serpiente.getCuerpo();
        if (cuerpoSerpiente.isEmpty()) {
            System.out.println("[dibujarJuego] ¡ADVERTENCIA! El cuerpo de la serpiente está vacío.");
        } else {
            System.out.println("[dibujarJuego] La serpiente tiene " + cuerpoSerpiente.size() + " segmentos.");
        }

        g.setColor(Color.GREEN);
        for (Punto p : cuerpoSerpiente) {
            if (p == null) {
                continue;
            }
            int x_pixel = p.getX() * TAMANO_CELDA;
            int y_pixel = p.getY() * TAMANO_CELDA;
            g.fillRect(x_pixel, y_pixel, TAMANO_CELDA, TAMANO_CELDA);
        }

        g.setColor(new Color(0, 150, 0));
        Punto cabeza = serpiente.getCabeza();
        g.fillRect(cabeza.getX() * TAMANO_CELDA, cabeza.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);

        if (comida == null) {
            return;
        }
        Punto posComida = comida.getPosicion();
        if (posComida == null) {
            return;
        }

        g.setColor(Color.RED);
        int comida_x_pixel = posComida.getX() * TAMANO_CELDA;
        int comida_y_pixel = posComida.getY() * TAMANO_CELDA;
        g.fillOval(comida_x_pixel, comida_y_pixel, TAMANO_CELDA, TAMANO_CELDA);

    }

    private void mostrarMensaje(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics fm = getFontMetrics(g.getFont());
        String mensaje;

        if (controlador.getPuntaje() > 0) {
            mensaje = "Game Over";
        } else {
            mensaje = "Press any arrow key to start";
        }

        int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y = getHeight() / 2;

        g.drawString(mensaje, x, y);
    }
}
