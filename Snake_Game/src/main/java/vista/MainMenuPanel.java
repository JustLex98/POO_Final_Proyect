package vista;

import persistencia.DatabaseManager;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MainMenuPanel extends JPanel {

    private GameFrame gameFrame;
    private BufferedImage backgroundImage;
    private Image logoImage;

    public MainMenuPanel(GameFrame frame, DatabaseManager manager) {
        this.gameFrame = frame;
        
        cargarRecursos();
        
        setPreferredSize(new Dimension(850, 575));        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- Título del Juego (usando nuestro nuevo componente) ---
        StrokedLabel gameTitleLabel = new StrokedLabel("Snake Game");
        gameTitleLabel.setFont(new Font("Impact", Font.BOLD, 80));
        gameTitleLabel.setForeground(new Color(210, 255, 210));
        gameTitleLabel.setOutlineColor(Color.BLACK);
        gameTitleLabel.setStrokeWidth(2); // Ajusta el grosor del borde si quieres

        // --- Logo como JLabel ---
        JLabel logoLabel = new JLabel();
        if (logoImage != null) {
            logoLabel.setIcon(new ImageIcon(logoImage));
        }

        // --- Panel de Controles (sin cambios en su interior) ---
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setBackground(new Color(0, 0, 0, 180));
        controlsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        GridBagConstraints gbcControls = new GridBagConstraints();
        gbcControls.gridwidth = GridBagConstraints.REMAINDER;
        gbcControls.fill = GridBagConstraints.HORIZONTAL;
        gbcControls.insets = new Insets(8, 0, 8, 0);
        
        JLabel pseudoLabel = new JLabel("Pseudónimo");
        pseudoLabel.setForeground(Color.WHITE);
        pseudoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pseudoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField pseudoField = new JTextField(15);
        JButton playButton = new JButton("Jugar");
        JButton registerButton = new JButton("Registrar Jugador");
        JButton reportsButton = new JButton("Ver Reportes");
        JButton exitButton = new JButton("Salir");
        
        controlsPanel.add(pseudoLabel, gbcControls);
        controlsPanel.add(pseudoField, gbcControls);
        controlsPanel.add(playButton, gbcControls);
        controlsPanel.add(registerButton, gbcControls);
        controlsPanel.add(reportsButton, gbcControls);
        controlsPanel.add(exitButton, gbcControls);

        // --- Posicionamiento con GridBagConstraints ---
        gbc.gridy = 0; gbc.weighty = 0.3; gbc.anchor = GridBagConstraints.PAGE_END;
        add(gameTitleLabel, gbc);

        gbc.gridy = 1; gbc.weighty = 0.4; gbc.anchor = GridBagConstraints.CENTER;
        add(logoLabel, gbc);
        
        gbc.gridy = 2; gbc.weighty = 0.3; gbc.anchor = GridBagConstraints.PAGE_START;
        add(controlsPanel, gbc);

        // --- Lógica de los botones... (sin cambios) ---
        playButton.addActionListener(e -> {
            String pseudonimo = pseudoField.getText().trim();
            
            if (pseudonimo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa un pseudónimo.", "Error", JOptionPane.ERROR_MESSAGE);
            } 
            else{ 
                // Nuevo: Selección de escenario con JOptionPane
                String[] opciones = { "Bosque.png", "Cueva.png", "Espacio.png" };
                String seleccion = (String) JOptionPane.showInputDialog(
                this, "Selecciona un escenario:", "Escenario", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
                );
            if (seleccion != null){
                gameFrame.iniciarJuego(pseudonimo, seleccion);
            }
            }
            });
        registerButton.addActionListener(e -> gameFrame.mostrarPanel("REGISTRO"));
        reportsButton.addActionListener(e -> gameFrame.mostrarPanel("REPORTES"));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void cargarRecursos() {
        try {
            InputStream bgStream = getClass().getResourceAsStream("/menu_background.png");
            InputStream logoStream = getClass().getResourceAsStream("/snake_logo.png");
            if (bgStream != null) {
                backgroundImage = ImageIO.read(bgStream); bgStream.close();
            } else { System.err.println("ERROR: No se pudo encontrar 'menu_background.png'."); }
            if (logoStream != null) {
                BufferedImage originalLogo = ImageIO.read(logoStream);
                logoImage = originalLogo.getScaledInstance(250, 250, Image.SCALE_SMOOTH); logoStream.close();
            } else { System.err.println("ERROR: No se pudo encontrar 'snake_logo.png'."); }
        } catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}