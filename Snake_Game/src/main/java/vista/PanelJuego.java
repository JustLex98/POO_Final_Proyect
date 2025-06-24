package vista;

import controlador.ControladorJuego;
import modelo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PanelJuego extends JPanel {

    private static final int TAMANO_CELDA = 25;
    private ControladorJuego controlador;
    private BufferedImage fondoEscenario;

    

    public PanelJuego(ControladorJuego controlador, int anchoTablero, int altoTablero, String nombreEscenario) {
        this.controlador = controlador;
        Dimension tamanoPanel = new Dimension(anchoTablero * TAMANO_CELDA, altoTablero * TAMANO_CELDA);
        setPreferredSize(tamanoPanel);
        setBackground(Theme.COLOR_FONDO);
        fondoEscenario = cargarEscenario(nombreEscenario);
    }
    
    private BufferedImage cargarEscenario(String nombreEscenario){
        if (nombreEscenario == null || nombreEscenario.isEmpty()) return null;
        try{
            return ImageIO.read(getClass().getClassLoader().getResource("images/" + nombreEscenario));
        }
        catch(IOException | IllegalArgumentException e){
            System.err.println("No se pudo cargar la imagen de fondo: " + nombreEscenario);
            return null;
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (fondoEscenario != null){
            g2d.drawImage(fondoEscenario, 0, 0, getWidth(), getHeight(), this);
        }
        
        dibujarCuadricula(g2d);
        dibujarJuego(g2d);
        
        if (!controlador.isEnJuego()) {
            dibujarMensajeInicio(g2d);
        }
        
        dibujarPuntaje(g2d);
    }
    
    private void dibujarCuadricula(Graphics2D g) {
        g.setColor(Theme.COLOR_CUADRICULA);
        for (int x = 0; x < getWidth(); x += TAMANO_CELDA) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += TAMANO_CELDA) {
            g.drawLine(0, y, getWidth(), y);
        }
    }

    private void dibujarJuego(Graphics2D g) {
        Serpiente serpiente = controlador.getSerpiente();
        Comida comida = controlador.getComida();
        
        // DIBUJO DEL CUERPO (Color brillante)
        List<Punto> cuerpoSerpiente = serpiente.getCuerpo();
        for (int i = 1; i < cuerpoSerpiente.size(); i++) {
            Punto p = cuerpoSerpiente.get(i);
            int x = p.getX() * TAMANO_CELDA;
            int y = p.getY() * TAMANO_CELDA;
            GradientPaint gp = new GradientPaint(x, y, Theme.COLOR_SERPIENTE_VERDE, x + TAMANO_CELDA, y + TAMANO_CELDA, Theme.COLOR_SERPIENTE_VERDE_OSCURO);
            g.setPaint(gp);
            g.fillRoundRect(x, y, TAMANO_CELDA, TAMANO_CELDA, 10, 10);
        }

        // --- DIBUJO DE LA CABEZA (Rectángulo redondeado y color oscuro) ---
        g.setColor(Theme.COLOR_CABEZA_VERDE);
        Punto cabeza = serpiente.getCabeza();
        g.fillRoundRect(cabeza.getX() * TAMANO_CELDA, cabeza.getY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA, 15, 15);

        // DIBUJO DE LA COMIDA
        Punto posComida = comida.getPosicion();
        int comidaX = posComida.getX() * TAMANO_CELDA;
        int comidaY = posComida.getY() * TAMANO_CELDA;
        Point2D center = new Point2D.Float(comidaX + TAMANO_CELDA / 2.0f, comidaY + TAMANO_CELDA / 2.0f);
        float radius = TAMANO_CELDA / 2.0f;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {Color.WHITE, Theme.COLOR_COMIDA};
        RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);
        g.setPaint(rgp);
        g.fillOval(comidaX, comidaY, TAMANO_CELDA, TAMANO_CELDA);
    }

    private void dibujarPuntaje(Graphics2D g) {
        g.setFont(Theme.FUENTE_PUNTAJE);
        String textoPuntaje = "Puntaje: ";
        String puntaje = "" + controlador.getPuntaje();
        
        g.setColor(Theme.COLOR_TEXTO_PRINCIPAL);
        g.drawString(textoPuntaje, 10, 25);
        
        g.setColor(Theme.COLOR_TEXTO_ACENTO);
        g.drawString(puntaje, 10 + g.getFontMetrics().stringWidth(textoPuntaje), 25);
    }
    
    private void dibujarMensajeInicio(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setFont(Theme.FUENTE_TITULO_MEDIANO);
        g.setColor(Theme.COLOR_TEXTO_PRINCIPAL);
        
        String mensaje = "Presiona una flecha para iniciar";
        
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        
        g.drawString(mensaje, x, y);
    }
}