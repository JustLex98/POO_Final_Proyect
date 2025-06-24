package vista;

import controlador.ControladorJuego;
import modelo.Comida;
import modelo.Punto;
import modelo.Serpiente;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PanelJuego extends JPanel {

    private static final int TAMANO_CELDA = 25;
    private ControladorJuego controlador;
    private final List<Point> estrellas;
    private static final int NUMERO_ESTRELLAS = 200;

    public PanelJuego(ControladorJuego controlador, int anchoTablero, int altoTablero) {
        this.controlador = controlador;
        this.estrellas = new ArrayList<>();
        Dimension tamanoPanel = new Dimension(anchoTablero * TAMANO_CELDA, altoTablero * TAMANO_CELDA);
        setPreferredSize(tamanoPanel);
        setBackground(Theme.COLOR_FONDO); // Usando el color de fondo de tu Theme
        generarEstrellas(tamanoPanel.width, tamanoPanel.height);
    }

    private void generarEstrellas(int ancho, int alto) {
        Random rand = new Random();
        for (int i = 0; i < NUMERO_ESTRELLAS; i++) {
            estrellas.add(new Point(rand.nextInt(ancho), rand.nextInt(alto)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        dibujarFondoEstrellado(g);
        dibujarCuadricula(g);

        if (controlador.isEnJuego() || controlador.isPaused()) {
            dibujarJuego(g);
            if (controlador.isPaused()) {
                mostrarMensajePausa(g);
            }
        } else {
            mostrarMensajeInicial(g);
        }
        
        dibujarPuntaje(g);
    }

    private void dibujarFondoEstrellado(Graphics g) {
        g.setColor(Color.WHITE);
        Random rand = new Random();
        for (Point estrella : estrellas) {
            int tamano = rand.nextInt(2) + 1;
            g.fillOval(estrella.x, estrella.y, tamano, tamano);
        }
    }

    private void dibujarCuadricula(Graphics g) {
        g.setColor(Theme.COLOR_CUADRICULA); // Usando el color de cuadrícula de tu Theme
        for (int x = 0; x < getWidth(); x += TAMANO_CELDA) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += TAMANO_CELDA) {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void dibujarJuego(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Serpiente serpiente = controlador.getSerpiente();
        Comida comida = controlador.getComida();
        
        int arc = 10;

        // Dibuja el cuerpo de la serpiente con un degradado usando los colores de tu Theme
        List<Punto> cuerpoSerpiente = serpiente.getCuerpo();
        for (int i = 1; i < cuerpoSerpiente.size(); i++) {
            Punto p = cuerpoSerpiente.get(i);
            int x = p.getX() * TAMANO_CELDA;
            int y = p.getY() * TAMANO_CELDA;
            
            GradientPaint gp = new GradientPaint(x, y, Theme.COLOR_SERPIENTE_VERDE, x + TAMANO_CELDA, y + TAMANO_CELDA, Theme.COLOR_SERPIENTE_VERDE_OSCURO);
            g2d.setPaint(gp);
            g2d.fillRoundRect(x, y, TAMANO_CELDA, TAMANO_CELDA, arc, arc);
        }

        // Dibuja la cabeza con el color sólido de tu Theme
        g2d.setColor(Theme.COLOR_CABEZA_VERDE);
        Punto cabeza = serpiente.getCabeza();
        g2d.fillRoundRect(cabeza.getX() * TAMANO_CELDA, cabeza.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA, arc, arc);

        // Dibuja la comida con el color sólido de tu Theme
        g2d.setColor(Theme.COLOR_COMIDA);
        Punto posComida = comida.getPosicion();
        g2d.fillRoundRect(posComida.getX() * TAMANO_CELDA, posComida.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA, arc, arc);
    }

    private void mostrarMensajePausa(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Theme.COLOR_TEXTO_PRINCIPAL);
        g.setFont(new Font("Impact", Font.BOLD, 60));
        String mensaje = "PAUSA";
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString(mensaje, (getWidth() - fm.stringWidth(mensaje)) / 2, getHeight() / 2);
    }

    private void mostrarMensajeInicial(Graphics g) {
        g.setColor(Theme.COLOR_TEXTO_PRINCIPAL);
        g.setFont(Theme.FUENTE_NORMAL);
        String mensaje = "Presiona cualquier flecha para iniciar";
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString(mensaje, (getWidth() - fm.stringWidth(mensaje)) / 2, getHeight() / 2);
    }

    private void dibujarPuntaje(Graphics g) {
        g.setColor(Theme.COLOR_TEXTO_PRINCIPAL);
        g.setFont(Theme.FUENTE_PUNTAJE);
        g.drawString("Puntaje: " + controlador.getPuntaje(), 10, 20);
    }
}