import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

import java.util.ArrayList;

public class MainApplicationWindow extends JFrame {
    private JTree menuTree;
    private JPanel leftPanel;
    private JList<String> fornitoriList;

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

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        createMenuTree();

        mainPanel.add(new JScrollPane(menuTree), BorderLayout.WEST);

        leftPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
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


    private void showFornitoriList() {
        ArrayList<String[]> fornitori = DatabaseManager.getFornitori();

        // Definizione delle colonne
        String[] columnNames = {"ID Fornitore", "Nome", "P.IVA", "Telefono"};

        // Creazione del modello di tabella personalizzato
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Popolamento della tabella con i dati dei fornitori
        for (String[] fornitore : fornitori) {
            tableModel.addRow(fornitore);
        }

        // Creazione della tabella con il modello personalizzato
        JTable fornitoriTable = new JTable(tableModel);
        fornitoriTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disabilita il ridimensionamento automatico delle colonne

        // Impostazione della larghezza delle colonne
        int[] columnWidths = {100, 200, 150, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            fornitoriTable.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Creazione del pannello per la tabella
        JPanel scrollMenuPanel = new JPanel(new BorderLayout());
        scrollMenuPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Aggiungi bordi al pannello

        // Aggiunta della tabella a un pannello scorrevole
        JScrollPane scrollPane = new JScrollPane(fornitoriTable);
        scrollMenuPanel.add(scrollPane, BorderLayout.CENTER);

        // Aggiunta del pannello scorrevole al pannello sinistro
        leftPanel.removeAll();
        leftPanel.add(scrollMenuPanel, BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
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
