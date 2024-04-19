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

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        createMenuTree();

        mainPanel.add(new JScrollPane(menuTree), BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setAppIcon() {
        BufferedImage iconImage = null;
        try {
            iconImage = ImageIO.read(new File("media/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (iconImage != null) {
            setIconImage(iconImage);
        }
    }

    private void createMenuTree() {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Home");

        DefaultMutableTreeNode fornitoriMagazzinoNode = new DefaultMutableTreeNode("Fornitori e Magazzino");
        rootNode.add(fornitoriMagazzinoNode);

        DefaultMutableTreeNode spiaggiaOmbrelloniNode = new DefaultMutableTreeNode("Spiaggia e Ombrelloni");
        rootNode.add(spiaggiaOmbrelloniNode);

        DefaultMutableTreeNode eventiCateringNode = new DefaultMutableTreeNode("Eventi e Catering");
        rootNode.add(eventiCateringNode);


        DefaultMutableTreeNode fornitoriNode = new DefaultMutableTreeNode("Fornitori");
        DefaultMutableTreeNode ordiniNode = new DefaultMutableTreeNode("Ordini");
        DefaultMutableTreeNode prodottiNode = new DefaultMutableTreeNode("Prodotti");


        fornitoriMagazzinoNode.add(fornitoriNode);
        fornitoriMagazzinoNode.add(ordiniNode);
        fornitoriMagazzinoNode.add(prodottiNode);


        DefaultMutableTreeNode prenotazioniNode = new DefaultMutableTreeNode("Prenotazioni");
        DefaultMutableTreeNode ombrelloniNode = new DefaultMutableTreeNode("Ombrelloni");
        DefaultMutableTreeNode servizioSpiaggiaNode = new DefaultMutableTreeNode("Servizio Spiaggia");

        spiaggiaOmbrelloniNode.add(prenotazioniNode);
        spiaggiaOmbrelloniNode.add(ombrelloniNode);
        spiaggiaOmbrelloniNode.add(servizioSpiaggiaNode);

        DefaultMutableTreeNode eventiNode = new DefaultMutableTreeNode("Eventi");
        DefaultMutableTreeNode prenotazioniEventiNode = new DefaultMutableTreeNode("Prenotazioni");
        DefaultMutableTreeNode serviziEventiNode = new DefaultMutableTreeNode("Servizi");

        eventiCateringNode.add(eventiNode);
        eventiCateringNode.add(prenotazioniEventiNode);
        eventiCateringNode.add(serviziEventiNode);

        menuTree = new JTree(rootNode);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) menuTree.getCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}
