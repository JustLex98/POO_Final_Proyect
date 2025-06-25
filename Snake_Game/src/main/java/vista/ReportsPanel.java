package vista;

import persistencia.DatabaseManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {

    private GameFrame gameFrame;
    private DatabaseManager dbManager; 
    private JComboBox<String> playerComboBox;

    public ReportsPanel(GameFrame frame, DatabaseManager manager) {
        this.gameFrame = frame;
        this.dbManager = manager; 
        setPreferredSize(new Dimension(850, 575));

        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea reportArea = new JTextArea("Selecciona un reporte para comenzar...");
        reportArea.setEditable(false);
        reportArea.setFont(Theme.FUENTE_REPORTE);
        reportArea.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        reportArea.setBackground(Theme.COLOR_PANEL_SECUNDARIO);
        reportArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.COLOR_BORDE));
        scrollPane.getViewport().setBackground(Theme.COLOR_PANEL_SECUNDARIO);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setOpaque(false);

        JButton maxScoreButton = new JButton("Puntaje Más Alto");
        JButton mostGamesButton = new JButton("Jugador con Más Partidas");
        
        maxScoreButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mostGamesButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        maxScoreButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxScoreButton.getPreferredSize().height));
        mostGamesButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, mostGamesButton.getPreferredSize().height));

        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setOpaque(false);
        timePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.COLOR_BORDE), "Tiempo por Jugador",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12), Color.WHITE
        ));
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        playerComboBox = new JComboBox<>();
        playerComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, playerComboBox.getPreferredSize().height));
        
        JButton timePlayedButton = new JButton("Consultar Tiempo");

        timePanel.add(playerComboBox);
        timePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        timePanel.add(timePlayedButton);

        JButton backButton = new JButton("<< Volver al Menú");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        controlsPanel.add(maxScoreButton);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlsPanel.add(mostGamesButton);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlsPanel.add(timePanel);
        controlsPanel.add(Box.createVerticalGlue());
        controlsPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);
        
        maxScoreButton.addActionListener(e -> reportArea.setText(this.dbManager.getReporteMaxPuntaje()));
        mostGamesButton.addActionListener(e -> reportArea.setText(this.dbManager.getReporteMasJuegos()));
        
        timePlayedButton.addActionListener(e -> {
            String selectedPlayer = (String) playerComboBox.getSelectedItem();
            if (selectedPlayer != null && !selectedPlayer.equals("No hay jugadores")) {
                reportArea.setText(this.dbManager.getReporteTiempoTotal(selectedPlayer));
            } else {
                reportArea.setText("No hay jugadores registrados para consultar.");
            }
        });
        
        backButton.addActionListener(e -> frame.mostrarPanel("MENU"));
        
        this.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                actualizarListaDeJugadores();
                reportArea.setText("Selecciona un reporte para comenzar...");
            }
            @Override public void ancestorRemoved(AncestorEvent event) {}
            @Override public void ancestorMoved(AncestorEvent event) {}
        });
    }

    private void actualizarListaDeJugadores() {
        playerComboBox.removeAllItems();
        List<String> players = this.dbManager.getTodosLosPseudonimos(); 
        if (players.isEmpty()) {
            playerComboBox.addItem("No hay jugadores");
            playerComboBox.setEnabled(false);
        } else {
            for (String player : players) {
                playerComboBox.addItem(player);
            }
            playerComboBox.setEnabled(true);
        }
    }
}