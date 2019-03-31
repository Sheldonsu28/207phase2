package ui;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class SubMenu extends JDialog {
    Container container;

    SubMenu(String title) {
        super(MainFrame.mainFrame, title, true);

        container = getContentPane();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(SubMenu.this,
                        "Are you sure to end current action and go back to previous menu?",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                        JOptionPane.YES_OPTION) {
                    SubMenu.this.dispose();
                }
            }
        });
        setBounds(100, 100, 400, 400);
    }

    JTextField getPositiveIntegerOnlyField() {
        JTextField input = new JTextField(10);
        input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                inputCheck();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void inputCheck() {
                if (!input.getText().matches("\\d*")) {
                    MainFrame.showErrorMessage("Invalid input detected! Positive integers only!");
                    SwingUtilities.invokeLater(() -> input.setText(""));
                }
            }
        });

        return input;
    }

    void defaultRowsLayout(LinkedHashMap<JComponent, String> components) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);

        container.setLayout(new GridLayout(components.size(), 1));

        for (Map.Entry<JComponent, String> entry : components.entrySet()) {
            JComponent component = entry.getKey();
            String label = entry.getValue();

            JPanel sectionPanel = new JPanel(flowLayout);
            if (label != null && !label.equals("")) {
                sectionPanel.add(new JLabel(label));
            }
            sectionPanel.add(component);

            container.add(sectionPanel);
        }

    }



}
