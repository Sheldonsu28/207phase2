package ui;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

class SubMenu extends JDialog {
    final Container container;

    SubMenu(String title) {
        super(MainFrame.mainFrame, title, true);

        container = getContentPane();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setClosingAction();

        setBounds(100, 100, 400, 400);
    }

    void setClosingAction() {
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
    }

    JTextField getPositiveIntegerOnlyField() {
        JTextField input = new JTextField(10);
        input.setDocument(new LimitedDocument(input, 8, "\\d*"));
        return input;
    }

    JTextField getPositiveTwoDecimalOnlyField() {
        JTextField input = new JTextField(10);
        input.setDocument(new LimitedDocument(input, 8, "\\d*\\.?((\\d\\d)?|(\\d?))"));
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

    private final class LimitedDocument extends PlainDocument {
        private final int limit;

        private LimitedDocument(JTextField parent, int limit, String regexStr) {
            super();
            this.limit = limit;
            addDocumentListener(new DocumentListener() {
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
                    if (!parent.getText().matches(regexStr)) {
                        MainFrame.showErrorMessage("Invalid input detected! Positive integers only!");
                        SwingUtilities.invokeLater(() -> parent.setText(""));
                    }
                }
            });
        }

        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;

            if (getLength() + str.length() <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
