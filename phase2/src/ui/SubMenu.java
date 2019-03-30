package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

}
