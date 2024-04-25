import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";


    public static ArrayList<String[]> getFornitori() {
        ArrayList<String[]> fornitori = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT id_fornitore, nome, p_iva, telefono FROM fornitore";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String idFornitore = resultSet.getString("id_fornitore");
                    String nome = resultSet.getString("nome");
                    String p_iva = resultSet.getString("p_iva");
                    String telefono = resultSet.getString("telefono");
                    String[] fornitore = {idFornitore, nome, p_iva, telefono};
                    fornitori.add(fornitore);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dei fornitori: " + e.getMessage());
        }
        return fornitori;
    }
    public static void addFornitore(String idFornitore, String nome, String p_iva, String telefono) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO fornitore (id_fornitore, nome, p_iva, telefono) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, idFornitore);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, p_iva);
                preparedStatement.setString(4, telefono);
                preparedStatement.executeUpdate();
                System.out.println("Fornitore aggiunto con successo!");
                MainApplicationWindow.getInstance().showFornitoriList();
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta del fornitore: " + e.getMessage());
        }

    }

    public static void removeFornitore(String idFornitore) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM fornitore WHERE id_fornitore = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, idFornitore);
                preparedStatement.executeUpdate();
                System.out.println("Fornitore rimosso con successo!");
                MainApplicationWindow.getInstance().showFornitoriList();
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione del fornitore: " + e.getMessage());
        }
    }
}
