package vista;

import controlador.ControladorJuego;
import persistencia.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private MainMenuPanel mainMenuPanel;
    private PanelJuego panelJuego;
    private RegisterPanel registerPanel;
    private ReportsPanel reportsPanel;

    private ControladorJuego controlador;
    private DatabaseManager dbManager;

    public GameFrame(ControladorJuego controlador) {
        this.controlador = controlador;
        this.dbManager = new DatabaseManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this, dbManager);
        panelJuego = new PanelJuego(controlador, 34, 23);
        registerPanel = new RegisterPanel(this, dbManager);
        reportsPanel = new ReportsPanel(this, dbManager);

        controlador.setPanelJuego(panelJuego);

        mainPanel.add(mainMenuPanel, "MENU");
        mainPanel.add(panelJuego, "JUEGO");
        mainPanel.add(registerPanel, "REGISTRO");
        mainPanel.add(reportsPanel, "REPORTES");

        this.add(mainPanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(34 * 25, 23 * 25 + 38);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }

    public void iniciarJuego(String pseudonimo) {
        controlador.iniciarNuevaPartida(pseudonimo);
        cardLayout.show(mainPanel, "JUEGO");
        panelJuego.requestFocusInWindow();
    }
}