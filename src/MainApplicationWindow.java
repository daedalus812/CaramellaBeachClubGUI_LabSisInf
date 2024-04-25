import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class MainApplicationWindow extends JFrame {
    private JTable fornitoriTable;
    private JTree menuTree;
    private JPanel leftPanel;
    private Connection connection;
    private static MainApplicationWindow instance;
    public static MainApplicationWindow getInstance() {
        if (instance == null) {
            instance = new MainApplicationWindow();
        }
        return instance;
    }
    public MainApplicationWindow() {
        setTitle("Caramella Beach Club - DB GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("media/logo.png");
        setIconImage(icon.getImage());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectToDatabase();

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        createMenuTree();

        mainPanel.add(new JScrollPane(menuTree), BorderLayout.WEST);

        leftPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "root";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore durante la connessione al database", "Errore", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void createMenuTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Home");

        DefaultMutableTreeNode fornitoriMagazzinoNode = new DefaultMutableTreeNode("Fornitori e Magazzino");
        rootNode.add(fornitoriMagazzinoNode);

        DefaultMutableTreeNode fornitoriNode = new DefaultMutableTreeNode("Fornitori");
        fornitoriMagazzinoNode.add(fornitoriNode);

        DefaultMutableTreeNode spiaggiaOmbrelloniNode = new DefaultMutableTreeNode("Spiaggia e Ombrelloni");
        rootNode.add(spiaggiaOmbrelloniNode);

        DefaultMutableTreeNode eventiCateringNode = new DefaultMutableTreeNode("Eventi e Catering");
        rootNode.add(eventiCateringNode);

        DefaultMutableTreeNode ordiniNode = new DefaultMutableTreeNode("Ordini");
        fornitoriMagazzinoNode.add(ordiniNode);

        DefaultMutableTreeNode prodottiNode = new DefaultMutableTreeNode("Prodotti");
        fornitoriMagazzinoNode.add(prodottiNode);

        DefaultMutableTreeNode prenotazioniNode = new DefaultMutableTreeNode("Prenotazioni");
        spiaggiaOmbrelloniNode.add(prenotazioniNode);

        DefaultMutableTreeNode ombrelloniNode = new DefaultMutableTreeNode("Ombrelloni");
        spiaggiaOmbrelloniNode.add(ombrelloniNode);

        DefaultMutableTreeNode servizioSpiaggiaNode = new DefaultMutableTreeNode("Servizio Spiaggia");
        spiaggiaOmbrelloniNode.add(servizioSpiaggiaNode);

        DefaultMutableTreeNode eventiNode = new DefaultMutableTreeNode("Eventi");
        eventiCateringNode.add(eventiNode);

        DefaultMutableTreeNode prenotazioniEventiNode = new DefaultMutableTreeNode("Prenotazioni");
        eventiCateringNode.add(prenotazioniEventiNode);

        DefaultMutableTreeNode serviziEventiNode = new DefaultMutableTreeNode("Servizi");
        eventiCateringNode.add(serviziEventiNode);

        menuTree = new JTree(rootNode);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) menuTree.getCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));


        menuTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();

                if (selectedNode != null && selectedNode.toString().equals("Fornitori")) {
                    showFornitoriList();
                } else {
                    hideFornitoriList();
                }
            }
        });
    }


    void showFornitoriList() {
        ArrayList<String[]> fornitori = DatabaseManager.getFornitori();

        String[] columnNames = {"ID Fornitore", "Nome", "P.IVA", "Telefono"};


        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);


        for (String[] fornitore : fornitori) {
            tableModel.addRow(fornitore);
        }


        fornitoriTable = new JTable(tableModel);
        fornitoriTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        int[] columnWidths = {100, 200, 150, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            fornitoriTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }


        JPanel scrollMenuPanel = new JPanel(new BorderLayout());
        scrollMenuPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Aggiungi bordi al pannello


        JScrollPane scrollPane = new JScrollPane(fornitoriTable);
        scrollMenuPanel.add(scrollPane, BorderLayout.CENTER);


        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        fornitoriTable.setRowSorter(sorter);


        Comparator<String> numericComparator = Comparator.comparingInt(Integer::parseInt);
        sorter.setComparator(0, numericComparator);
        sorter.setComparator(1, Comparator.naturalOrder());

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);


        JButton addButton = new JButton("Aggiungi Fornitore");
        addButton.addActionListener(e -> showAddFornitoreDialog());

        JButton removeButton = new JButton("Rimuovi Fornitore");
        removeButton.addActionListener(e -> removeSelectedFornitore());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollMenuPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        leftPanel.removeAll();
        leftPanel.add(mainPanel, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void hideFornitoriTable() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void showAddFornitoreDialog() {
        AddFornitoreDialog dialog = new AddFornitoreDialog(this);
        dialog.setVisible(true);
    }

    private void removeSelectedFornitore() {
        int selectedRow = fornitoriTable.getSelectedRow();
        if (selectedRow != -1) {
            String idFornitore = (String) fornitoriTable.getValueAt(selectedRow, 0);
            DatabaseManager.removeFornitore(idFornitore);
            ((DefaultTableModel) fornitoriTable.getModel()).removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un fornitore da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideFornitoriList() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}