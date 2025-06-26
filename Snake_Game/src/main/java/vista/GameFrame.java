package vista;

import controlador.ControladorJuego;
import persistencia.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import Musica.EfectosSonidos;

public class GameFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
   
    private MainMenuPanel mainMenuPanel;
    private PanelJuego panelJuego;
    private RegisterPanel registerPanel;
    private ReportsPanel reportsPanel;

    private ControladorJuego controlador;
    private DatabaseManager dbManager;
    private EfectosSonidos sonido;

    public GameFrame(ControladorJuego controlador, EfectosSonidos sonido) {
        this.controlador = controlador;
        this.dbManager = new DatabaseManager();
        this.sonido = sonido;
       

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainMenuPanel = new MainMenuPanel(this, dbManager, sonido);
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
        // Reproducir música de menú al iniciar
        sonido.playMusicLoop("menutheme.wav");
    }
    
    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(mainPanel, nombrePanel);
    }

    public void iniciarJuego(String pseudonimo, String escenario) {
        controlador.iniciarNuevaPartida(pseudonimo);
        
        panelJuego = new PanelJuego(controlador, 34, 23, escenario, sonido);
        controlador.setPanelJuego(panelJuego);
        mainPanel.add(panelJuego, "JUEGO");
        cardLayout.show(mainPanel, "JUEGO");
        sonido.stop(); //Detener musica del menu
        sonido.playMusicLoop("gametheme.wav"); //musica para la partida.
        panelJuego.requestFocusInWindow();
    }
    public void volverAlMenu(){
        sonido.stop();
        sonido.playMusicLoop("menutheme.wav");
        mostrarPanel("MENU");
    }
}