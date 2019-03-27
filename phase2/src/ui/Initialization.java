package ui;

import atm.BankManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Initialization extends JDialog {
    Initialization() {
        super();

        new LoginValidation(this, LoginType.MANAGER, null);

        setTitle("System Initialization");
        setBounds(10, 10, 300, 300);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(Initialization.this,
                        "Are you sure to exit the program?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        setVisible(true);
    }

    BankManager getManager() {
        return null;
    }
}
