package ui;

import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SubMenu extends JDialog {
    Container container;
    private JDialog parent;

    SubMenu(BankManager manager, String title, JDialog parent) {
        super(MainFrame.mainFrame, title, true);

        this.parent = parent;
        container = getContentPane();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(SubMenu.this,
                        "Are you sure to end current action and go back to previous menu?",
                        "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                        JOptionPane.YES_OPTION) {
                    backToParent();
                }
            }
        });
        setBounds(100, 100, 400, 400);
    }

    void backToParent() {
        SubMenu.this.dispose();
        //parent.setVisible(true);
    }
}
