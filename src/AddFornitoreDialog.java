import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AddFornitoreDialog extends JDialog {
    private JTextField idField;
    private JTextField nomeField;
    private JTextField pIvaField;
    private JTextField telefonoField;

    public AddFornitoreDialog(JFrame parent) {
        super(parent, "Aggiungi Fornitore", true);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("P.IVA:"));
        pIvaField = new JTextField();
        panel.add(pIvaField);

        panel.add(new JLabel("Telefono:"));
        telefonoField = new JTextField();
        panel.add(telefonoField);

        JButton addButton = new JButton("Aggiungi");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String nome = nomeField.getText();
                String pIva = pIvaField.getText();
                String telefono = telefonoField.getText();

                if (!id.isEmpty() && !nome.isEmpty() && !pIva.isEmpty() && !telefono.isEmpty()) {
                    DatabaseManager.addFornitore(id, nome, pIva, telefono);
                    JOptionPane.showMessageDialog(AddFornitoreDialog.this, "Fornitore aggiunto con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddFornitoreDialog.this, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Annulla");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(addButton);
        panel.add(cancelButton);

        add(panel);
    }
}