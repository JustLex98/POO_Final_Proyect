package vista;

import persistencia.DatabaseManager;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(GameFrame frame, DatabaseManager manager) {
        setLayout(new GridBagLayout());
        setBackground(Theme.COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("Registrar Nuevo Jugador");
        titleLabel.setFont(Theme.FUENTE_TITULO_MEDIANO);
        titleLabel.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 30, 10);
        add(titleLabel, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLabel = new JLabel("Nombre Completo:");
        nameLabel.setFont(Theme.FUENTE_NORMAL);
        nameLabel.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20);
        nameField.setFont(Theme.FUENTE_NORMAL);
        nameField.setBackground(Theme.COLOR_PANEL_SECUNDARIO);
        nameField.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        nameField.setBorder(BorderFactory.createLineBorder(Theme.COLOR_BORDE, 1));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel pseudoLabel = new JLabel("Pseudónimo:");
        pseudoLabel.setFont(Theme.FUENTE_NORMAL);
        pseudoLabel.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        add(pseudoLabel, gbc);

        JTextField pseudoField = new JTextField(20);
        pseudoField.setFont(Theme.FUENTE_NORMAL);
        pseudoField.setBackground(Theme.COLOR_PANEL_SECUNDARIO);
        pseudoField.setForeground(Theme.COLOR_TEXTO_PRINCIPAL);
        pseudoField.setBorder(BorderFactory.createLineBorder(Theme.COLOR_BORDE, 1));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(pseudoField, gbc);

        JButton registerButton = new JButton("Registrar");
        JButton backButton = new JButton("Volver al Menú");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(buttonPanel, gbc);

        registerButton.addActionListener(e -> {
            String nombre = nameField.getText().trim();
            String pseudonimo = pseudoField.getText().trim();
            if (nombre.isEmpty() || pseudonimo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ambos campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            manager.registrarJugador(nombre, pseudonimo);
            JOptionPane.showMessageDialog(this, "¡Jugador registrado con éxito!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
            pseudoField.setText("");
        });

        backButton.addActionListener(e -> frame.mostrarPanel("MENU"));
    }
}