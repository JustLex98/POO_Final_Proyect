package vista;

import controlador.ControladorJuego;
import modelo.Comida;
import modelo.Punto;
import modelo.Serpiente;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        setBackground(new Color(30, 30, 30));
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarCuadricula(g);

        if (controlador.isEnJuego()) {
            dibujarJuego(g);
        } else {
            mostrarMensaje(g);
        }

        dibujarPuntaje(g);
    }

    private void dibujarCuadricula(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        for (int x = 0; x < getWidth(); x += TAMANO_CELDA) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += TAMANO_CELDA) {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void dibujarJuego(Graphics g) {
        List<Punto> cuerpoSerpiente = serpiente.getCuerpo();
        
        g.setColor(Color.GREEN);
        for (int i = 1; i < cuerpoSerpiente.size(); i++) {
            Punto p = cuerpoSerpiente.get(i);
            g.fillRect(p.getX() * TAMANO_CELDA, p.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
        }

        g.setColor(new Color(0, 128, 0));
        Punto cabeza = serpiente.getCabeza();
        g.fillRect(cabeza.getX() * TAMANO_CELDA, cabeza.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);

        g.setColor(Color.RED);
        Punto posComida = comida.getPosicion();
        g.fillOval(posComida.getX() * TAMANO_CELDA, posComida.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
    }

    private void dibujarPuntaje(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 20));

        String textoPuntaje = "Puntaje: " + controlador.getPuntaje();

        g.drawString(textoPuntaje, 10, 20);
    }

    private void mostrarMensaje(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics fm = getFontMetrics(g.getFont());
        String mensaje;

        if (controlador.getPuntaje() > 0 && !controlador.isEnJuego()) {
            mensaje = "Game Over";
        } else {
            mensaje = "Presione cualquier flecha para iniciar";
        }

        int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y = getHeight() / 2;
        g.drawString(mensaje, x, y);
    }
}