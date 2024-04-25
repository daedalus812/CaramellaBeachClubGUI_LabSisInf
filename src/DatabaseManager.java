import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager extends Component {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root";

    private static MainApplicationWindow mainWindowInstance;

    public static void setMainWindowInstance(MainApplicationWindow instance) {
        mainWindowInstance = instance;
    }

    public static void addEvento(String idEvento, String nome, String data, String ora) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO eventi (id_evento, nome, data, ora) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, idEvento);
                preparedStatement.setString(2, nome);
                preparedStatement.setString(3, data);
                preparedStatement.setString(4, ora);
                preparedStatement.executeUpdate();
                System.out.println("Evento aggiunto con successo!");
                MainApplicationWindow.getInstance().showEventiList();
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiunta dell'evento: " + e.getMessage());
        }
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

    public static ArrayList<String[]> getEventi() {
        ArrayList<String[]> eventi = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT id_evento, nome, data, ora FROM eventi";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String idEvento = resultSet.getString("id_evento");
                    String nome = resultSet.getString("nome");
                    String data = resultSet.getString("data");
                    String ora = resultSet.getString("ora");
                    String[] evento = {idEvento, nome, data, ora};
                    eventi.add(evento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero degli eventi: " + e.getMessage());
        }
        return eventi;
    }


    public static void removeEvento(String idEvento) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM eventi WHERE id_evento = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, idEvento);
                preparedStatement.executeUpdate();
                System.out.println("Evento rimosso con successo!");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la rimozione dell'evento: " + e.getMessage());
        }
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