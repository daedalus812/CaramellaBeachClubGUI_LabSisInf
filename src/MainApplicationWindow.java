import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class MainApplicationWindow extends JFrame {
    private JTree menuTree;
    private JPanel leftPanel;
    private JList<String> fornitoriList;

    public MainApplicationWindow() {
        setTitle("Caramella Beach Club - DB GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setAppIcon();

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

        // Aggiungi un listener per gestire gli eventi di selezione nell'albero del menu
        menuTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();

                // Se l'opzione "Fornitori" è stata selezionata, visualizza l'elenco dei fornitori
                if (selectedNode != null && selectedNode.toString().equals("Fornitori")) {
                    showFornitoriList();
                } else {
                    // Altrimenti, nascondi il pannello dei fornitori
                    hideFornitoriList();
                }
            }
        });
    }

    // Metodo per mostrare l'elenco dei fornitori
    private void showFornitoriList() {
        // Recupera i dati dei fornitori dal database
        ArrayList<String> fornitori = DatabaseManager.getFornitori();

        // Popola la lista dei fornitori
        fornitoriList = new JList<>(fornitori.toArray(new String[0]));

        // Aggiungi la lista dei fornitori al pannello sinistro
        leftPanel.removeAll();
        leftPanel.add(new JScrollPane(fornitoriList), BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Metodo per nascondere l'elenco dei fornitori
    private void hideFornitoriList() {
        leftPanel.removeAll();
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}
