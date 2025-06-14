package vista;

import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(PanelJuego panelJuego) {

        add(panelJuego);

        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        pack();

        setLocationRelativeTo(null);

        setVisible(true);
    }
}
