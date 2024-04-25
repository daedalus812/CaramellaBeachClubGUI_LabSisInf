import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEventoDialog extends JDialog {
    private JTextField idField;
    private JTextField nomeField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> timeComboBox; // Combobox per le fasce orarie

    public AddEventoDialog(MainApplicationWindow parent) {
        super(parent, "Aggiungi Evento", true);
        setLayout(new BorderLayout()); // Utilizziamo BorderLayout per organizzare i componenti

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // JPanel per i campi di input
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Aggiungiamo dei margini

        JLabel idLabel = new JLabel("ID:");
        JLabel nomeLabel = new JLabel("Nome:");
        JLabel dataLabel = new JLabel("Data:");
        JLabel oraLabel = new JLabel("Ora:");

        idField = new JTextField();
        nomeField = new JTextField();
        dayComboBox = new JComboBox<>(createIntegerArray(1, 31)); // Giorni da 1 a 31
        monthComboBox = new JComboBox<>(new String[]{"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"}); // Mesi
        yearComboBox = new JComboBox<>(createIntegerArray(2020, 2030)); // Anni da 2020 a 2030
        timeComboBox = createTimeComboBox(); // Creiamo la JComboBox per le fasce orarie

        fieldsPanel.add(idLabel);
        fieldsPanel.add(idField);
        fieldsPanel.add(nomeLabel);
        fieldsPanel.add(nomeField);
        fieldsPanel.add(dataLabel);
        fieldsPanel.add(createDatePanel());
        fieldsPanel.add(oraLabel);
        fieldsPanel.add(timeComboBox);

        add(fieldsPanel, BorderLayout.CENTER); // Aggiungiamo il pannello dei campi al centro della finestra

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // JPanel per il pulsante Conferma
        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idEvento = idField.getText();
                String nome = nomeField.getText();
                String data = getDateAsString(); // Ottieni la data selezionata come stringa
                String ora = (String) timeComboBox.getSelectedItem(); // Ottieni la fascia oraria selezionata

                DatabaseManager.addEvento(idEvento, nome, data, ora);
                parent.showEventiList();
                dispose();
            }
        });
        buttonPanel.add(confermaButton);

        add(buttonPanel, BorderLayout.SOUTH); // Aggiungiamo il pannello del pulsante alla parte inferiore della finestra

        setSize(400, 250); // Impostiamo le dimensioni della finestra
        setLocationRelativeTo(parent);
    }

    // Metodo per creare un pannello per la selezione della data
    private JPanel createDatePanel() {
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Utilizziamo FlowLayout per allineare i componenti a sinistra

        datePanel.add(dayComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(yearComboBox);

        return datePanel;
    }

    // Metodo per ottenere la data selezionata come stringa nel formato "YYYY-MM-DD"
    private String getDateAsString() {
        int day = (int) dayComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex() + 1; // Aggiungi 1 perché gli indici dei mesi partono da 0
        int year = (int) yearComboBox.getSelectedItem();
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    // Metodo di utilità per creare un array di interi da start a end inclusi
    private Integer[] createIntegerArray(int start, int end) {
        Integer[] array = new Integer[end - start + 1];
        for (int i = 0; i < array.length; i++) {
            array[i] = start++;
        }
        return array;
    }

    // Metodo per creare la JComboBox per le fasce orarie
    private JComboBox<String> createTimeComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                comboBox.addItem(time);
            }
        }
        return comboBox;
    }
}
