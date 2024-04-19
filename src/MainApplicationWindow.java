import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainApplicationWindow extends JFrame {
    private JTree menuTree;

    public MainApplicationWindow() {
        setTitle("Caramella Beach Club - DB GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setAppIcon();
        // Imposta il Look and Feel di Windows
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        // Creiamo il menu ad albero
        createMenuTree();

        // Aggiungiamo il menu ad albero al lato sinistro
        mainPanel.add(new JScrollPane(menuTree), BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setAppIcon() {
        // Carica l'immagine dall'url specificato (assicurati di sostituire "path/all_icon.png" con il percorso corretto del file)
        BufferedImage iconImage = null;
        try {
            iconImage = ImageIO.read(new File("media/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Imposta l'icona dell'applicazione
        if (iconImage != null) {
            setIconImage(iconImage);
        }
    }

    private void createMenuTree() {
        // Creiamo il nodo radice del menu ad albero
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Home");

        DefaultMutableTreeNode fornitoriMagazzinoNode = new DefaultMutableTreeNode("Fornitori e Magazzino");
        rootNode.add(fornitoriMagazzinoNode);
        // Creiamo il nodo "Spiaggia e Ombrelloni" come sotto-sezione di "Home"
        DefaultMutableTreeNode spiaggiaOmbrelloniNode = new DefaultMutableTreeNode("Spiaggia e Ombrelloni");
        rootNode.add(spiaggiaOmbrelloniNode);



        // Creiamo i nodi figlio di "Fornitori e Magazzino"
        DefaultMutableTreeNode fornitoriNode = new DefaultMutableTreeNode("Fornitori");
        DefaultMutableTreeNode ordiniNode = new DefaultMutableTreeNode("Ordini");
        DefaultMutableTreeNode prodottiNode = new DefaultMutableTreeNode("Prodotti");

        // Aggiungiamo i nodi figlio a "Fornitori e Magazzino"
        fornitoriMagazzinoNode.add(fornitoriNode);
        fornitoriMagazzinoNode.add(ordiniNode);
        fornitoriMagazzinoNode.add(prodottiNode);

        // Creiamo le sottosezioni di "Spiaggia e Ombrelloni"
        DefaultMutableTreeNode prenotazioniNode = new DefaultMutableTreeNode("Prenotazioni");
        DefaultMutableTreeNode ombrelloniNode = new DefaultMutableTreeNode("Ombrelloni");
        DefaultMutableTreeNode servizioSpiaggiaNode = new DefaultMutableTreeNode("Servizio Spiaggia");

        // Aggiungiamo le sottosezioni a "Spiaggia e Ombrelloni"
        spiaggiaOmbrelloniNode.add(prenotazioniNode);
        spiaggiaOmbrelloniNode.add(ombrelloniNode);
        spiaggiaOmbrelloniNode.add(servizioSpiaggiaNode);

        // Creiamo il nodo "Eventi e Catering" come sotto-sezione di "Home"
        DefaultMutableTreeNode eventiCateringNode = new DefaultMutableTreeNode("Eventi e Catering");
        rootNode.add(eventiCateringNode);

        // Creiamo le sottosezioni di "Eventi e Catering"
        DefaultMutableTreeNode eventiNode = new DefaultMutableTreeNode("Eventi");
        DefaultMutableTreeNode prenotazioniEventiNode = new DefaultMutableTreeNode("Prenotazioni");
        DefaultMutableTreeNode serviziEventiNode = new DefaultMutableTreeNode("Servizi");

        // Aggiungiamo le sottosezioni a "Eventi e Catering"
        eventiCateringNode.add(eventiNode);
        eventiCateringNode.add(prenotazioniEventiNode);
        eventiCateringNode.add(serviziEventiNode);

        // Creiamo il menu ad albero con il nodo radice
        menuTree = new JTree(rootNode);

        // Aggiungiamo uno spazio intorno al testo e alle icone dei nodi
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) menuTree.getCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // Aggiungi spazio solo a sinistra e a destra
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}
