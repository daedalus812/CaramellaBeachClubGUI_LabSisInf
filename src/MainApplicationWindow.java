import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainApplicationWindow extends JFrame {
    private JTree menuTree;

    public MainApplicationWindow() {
        setTitle("Main Application Window");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        getContentPane().add(mainPanel);

        // Creiamo il menu ad albero
        createMenuTree();

        // Aggiungiamo il menu ad albero al lato sinistro
        mainPanel.add(new JScrollPane(menuTree));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenuTree() {
        // Creiamo il nodo radice del menu ad albero
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Menu");

        // Creiamo i nodi figlio
        DefaultMutableTreeNode fornitoriNode = new DefaultMutableTreeNode("Fornitori");
        DefaultMutableTreeNode ordiniNode = new DefaultMutableTreeNode("Ordini");
        // Aggiungiamo altri nodi figlio secondo necessità...

        // Aggiungiamo i nodi figlio al nodo radice
        rootNode.add(fornitoriNode);
        rootNode.add(ordiniNode);
        // Aggiungiamo altri nodi figlio secondo necessità...

        // Creiamo il menu ad albero con il nodo radice
        menuTree = new JTree(rootNode);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApplicationWindow::new);
    }
}
