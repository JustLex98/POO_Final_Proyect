package vista;

import javax.swing.*;
import java.awt.*;

public class StrokedLabel extends JLabel {

    private Color outlineColor = Color.BLACK;
    private int strokeWidth = 2;

    public StrokedLabel(String text) {
        super(text);
    }

    public void setOutlineColor(Color color) {
        this.outlineColor = color;
    }

    public void setStrokeWidth(int width) {
        this.strokeWidth = width;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Creamos una copia para no modificar el objeto Graphics original
        Graphics2D g2 = (Graphics2D) g.create();

        // Activamos el antialiasing para texto suave
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Obtenemos el texto y la fuente del propio JLabel
        String text = getText();
        Font font = getFont();
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        // 1. Dibujamos el contorno negro
        g2.setColor(outlineColor);
        for (int i = -strokeWidth; i <= strokeWidth; i++) {
            for (int j = -strokeWidth; j <= strokeWidth; j++) {
                if (i != 0 || j != 0) {
                    g2.drawString(text, x + i, y + j);
                }
            }
        }

        // 2. Dibujamos el texto principal encima
        g2.setColor(getForeground()); // Usamos el color de texto definido para el JLabel
        g2.drawString(text, x, y);

        // Liberamos los recursos de la copia de Graphics
        g2.dispose();
    }
}