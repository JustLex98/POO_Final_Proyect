package persistencia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlserver://localhost:49766;databaseName=Snake_Game;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "molina98fuentes";

    public DatabaseManager() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se pudo encontrar la clase del driver de SQL Server. Asegúrate de que el JAR está en el proyecto.");
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void registrarJugador(String nombre, String pseudonimo) {
        if (getJugadorId(pseudonimo) != -1) {
            System.out.println("Error: El pseudónimo '" + pseudonimo + "' ya está registrado.");
            return;
        }
        String sql = "INSERT INTO Jugadores (nombre, pseudonimo) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, pseudonimo);
            pstmt.executeUpdate();
            System.out.println("Jugador '" + nombre + "' registrado exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getJugadorId(String pseudonimo) {
        String sql = "SELECT id FROM Jugadores WHERE pseudonimo = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pseudonimo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void guardarPartida(String pseudonimo, LocalDateTime inicio, LocalDateTime fin, int puntaje) {
        int jugadorId = getJugadorId(pseudonimo);
        if (jugadorId == -1) {
            System.err.println("Error al guardar partida: El jugador con pseudónimo '" + pseudonimo + "' no existe.");
            return;
        }

        String sql = "INSERT INTO Partidas (id_jugador, fecha_inicio, fecha_fin, puntaje) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jugadorId);
            pstmt.setTimestamp(2, Timestamp.valueOf(inicio));
            pstmt.setTimestamp(3, Timestamp.valueOf(fin));
            pstmt.setInt(4, puntaje);
            pstmt.executeUpdate();
            System.out.println("Partida guardada para el jugador " + pseudonimo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getReporteMaxPuntaje() {
        String sql = "SELECT TOP 1 j.pseudonimo, p.puntaje FROM Partidas p JOIN Jugadores j ON p.id_jugador = j.id ORDER BY p.puntaje DESC";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return "Puntaje Más Alto: " + rs.getInt("puntaje") + " (Jugador: " + rs.getString("pseudonimo") + ")";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No hay partidas registradas.";
    }

    public String getReporteMasJuegos() {
        String sql = "SELECT TOP 1 j.pseudonimo, COUNT(p.id) as total_juegos FROM Partidas p JOIN Jugadores j ON p.id_jugador = j.id GROUP BY j.pseudonimo ORDER BY total_juegos DESC";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return "Jugador con Más Partidas: " + rs.getString("pseudonimo") + " (" + rs.getInt("total_juegos") + " juegos)";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No hay partidas registradas.";
    }

    public String getReporteTiempoTotal(String pseudonimo) {
        String sql = "SELECT SUM(DATEDIFF(second, p.fecha_inicio, p.fecha_fin)) as segundos_totales FROM Partidas p JOIN Jugadores j ON p.id_jugador = j.id WHERE j.pseudonimo = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pseudonimo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int segundos = rs.getInt("segundos_totales");
                return "Tiempo total de juego para '" + pseudonimo + "': " + segundos + " segundos.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No se encontraron partidas para el jugador '" + pseudonimo + "'.";
    }
}