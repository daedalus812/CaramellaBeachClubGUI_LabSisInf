import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class MainApplicationWindow extends JFrame {
    private JTable fornitoriTable;
    public static JTable eventiTable;
    private JTree menuTree;
    private JPanel leftPanel;
    private Connection connection;
    private static MainApplicationWindow instance;

    public static MainApplicationWindow getInstance() {
        return instance;
    }

    public MainApplicationWindow() {
        DatabaseManager.setMainWindowInstance(this);
        instance = this;
        setTitle("Caramella Beach Club - DB GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("media/logo.png");
        setIconImage(icon.getImage());
        createMenuBar();

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

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0)); // Azione per uscire dal programma
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(e -> showAboutDialog()); // Azione per mostrare le informazioni sull'applicazione
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "Caramella Beach Club - DB GUI\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
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
                if (selectedNode != null && selectedNode.toString().equals("Eventi")) {
                    showEventiList();
                } else {
                    hideEventiList();
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

        // Aggiungi il listener per la ricerca dei fornitori
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }

            private void updateFilter() {
                String searchText = searchField.getText().trim();
                TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) fornitoriTable.getRowSorter();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText)); // Ignora la distinzione tra maiuscole e minuscole
            }
        });


        fornitoriTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = fornitoriTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < fornitoriTable.getRowCount()) {
                        fornitoriTable.setRowSelectionInterval(row, row);
                        showPopup(e);
                    }
                }
            }
        });

        leftPanel.removeAll();
        leftPanel.add(mainPanel, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    void showEventiList() {
        ArrayList<String[]> eventi = DatabaseManager.getEventi();

        String[] columnNames = {"ID Evento", "Nome", "Data", "Ora"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (String[] evento : eventi) {
            tableModel.addRow(evento);
        }


        eventiTable = new JTable(tableModel);
        eventiTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Codice per la creazione della tabella, dei pulsanti e del pannello di controllo

        JPanel scrollMenuPanel = new JPanel(new BorderLayout());
        scrollMenuPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Aggiungi bordi al pannello


        JScrollPane scrollPane = new JScrollPane(eventiTable);
        scrollMenuPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);


        JButton addButton = new JButton("Aggiungi Evento");
        addButton.addActionListener(e -> showAddEventoDialog());

        JButton removeButton = new JButton("Rimuovi Evento");
        removeButton.addActionListener(e -> removeSelectedEvento());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(searchPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollMenuPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        eventiTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        eventiTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        leftPanel.removeAll();
        leftPanel.add(mainPanel, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void hideEventiList() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void hideFornitoriTable() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void showAddEventoDialog() {
        AddEventoDialog dialog = new AddEventoDialog(this);
        dialog.setVisible(true);
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

    private void removeSelectedEvento() {
        int selectedRow = eventiTable.getSelectedRow();
        if (selectedRow != -1) {
            String idEvento = (String) eventiTable.getValueAt(selectedRow, 0);
            DatabaseManager.removeEvento(idEvento);
            ((DefaultTableModel) eventiTable.getModel()).removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un evento da rimuovere", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hideFornitoriList() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void showPopup(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem modificaMenuItem = new JMenuItem("Modifica");
        modificaMenuItem.addActionListener(actionEvent -> {
            int selectedRow = fornitoriTable.getSelectedRow();
            String idFornitore = (String) fornitoriTable.getValueAt(selectedRow, 0);
            showEditFornitoreDialog(idFornitore);
        });
        popupMenu.add(modificaMenuItem);
        popupMenu.show(fornitoriTable, e.getX(), e.getY());
    }


    private void showEditFornitoreDialog(String idFornitore) {

        String[] datiFornitore = DatabaseManager.getFornitoreById(idFornitore);


        JDialog dialog = new JDialog(this, "Modifica Fornitore", true);
        dialog.setLayout(new GridLayout(4, 2));


        JTextField nomeField = new JTextField(datiFornitore[0]);
        JTextField partitaIVAField = new JTextField(datiFornitore[1]);
        JTextField telefonoField = new JTextField(datiFornitore[2]);


        JLabel nomeLabel = new JLabel("Nome:");
        JLabel partitaIVALabel = new JLabel("Partita IVA:");
        JLabel telefonoLabel = new JLabel("Telefono:");


        dialog.add(nomeLabel);
        dialog.add(nomeField);
        dialog.add(partitaIVALabel);
        dialog.add(partitaIVAField);
        dialog.add(telefonoLabel);
        dialog.add(telefonoField);

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> {

            String nuovoNome = nomeField.getText();
            String nuovaPartitaIVA = partitaIVAField.getText();
            String nuovoTelefono = telefonoField.getText();

            DatabaseManager.updateFornitore(idFornitore, nuovoNome, nuovaPartitaIVA, nuovoTelefono);

            dialog.dispose();

            showFornitoriList();
        });


        dialog.add(confermaButton);

        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Classe interna per l'editing del pulsante "Info" nella tabella degli eventi
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Quando il pulsante "Info" viene premuto, mostra la dialog con l'immagine dell'evento
                    showEventInfoDialog(selectedRow);
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            selectedRow = row;
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Azione quando il pulsante "Info" viene premuto
                // Qui puoi gestire l'apertura della dialog con l'immagine dell'evento
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    // Metodo per mostrare la dialog con l'immagine dell'evento
    private void showEventInfoDialog(int row) {
        // Qui puoi implementare la logica per mostrare l'immagine dell'evento nella dialog
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}
