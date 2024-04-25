import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private static MainApplicationWindow mainWindowInstance;

    public static void setMainWindowInstance(MainApplicationWindow instance) {
        mainWindowInstance = instance;
    }

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

    public static String[] getFornitoreById(String idFornitore) {
        String[] fornitore = null;
        String query = "SELECT nome, p_iva, telefono FROM fornitore WHERE id_fornitore = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, idFornitore);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String partitaIVA = resultSet.getString("p_iva");
                    String telefono = resultSet.getString("telefono");
                    fornitore = new String[]{nome, partitaIVA, telefono};
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del fornitore: " + e.getMessage());
        }
        return fornitore;
    }

    public static void updateFornitore(String idFornitore, String nuovoNome, String nuovaPartitaIVA, String nuovoTelefono) {
        String query = "UPDATE fornitore SET nome = ?, p_iva = ?, telefono = ? WHERE id_fornitore = ?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nuovoNome);
            preparedStatement.setString(2, nuovaPartitaIVA);
            preparedStatement.setString(3, nuovoTelefono);
            preparedStatement.setString(4, idFornitore);
            preparedStatement.executeUpdate();
            System.out.println("Dati del fornitore aggiornati con successo!");
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento dei dati del fornitore: " + e.getMessage());
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
