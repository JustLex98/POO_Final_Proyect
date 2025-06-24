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
        registerPanel = new RegisterPanel(this, dbManager);
        reportsPanel = new ReportsPanel(this, dbManager);

        mainPanel.add(mainMenuPanel, "MENU");
        mainPanel.add(registerPanel, "REGISTRO");
        mainPanel.add(reportsPanel, "REPORTES");

        this.add(mainPanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }

    public void iniciarJuego(String pseudonimo, String escenario) {
        controlador.iniciarNuevaPartida(pseudonimo);
        
        panelJuego = new PanelJuego(controlador, 34, 23, escenario);
        controlador.setPanelJuego(panelJuego);
        mainPanel.add(panelJuego, "JUEGO");
        cardLayout.show(mainPanel, "JUEGO");
        panelJuego.requestFocusInWindow();
        
    }
}