package ui;

import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class ChoiceMenu extends JDialog {
    private Container container;
    private JButton toLogout;

    ChoiceMenu(BankManager manager, String title) {
        super(MainFrame.mainFrame, title, true);

        container = getContentPane();
        toLogout = new JButton("Logout");
        toLogout.addActionListener(e ->
                ChoiceMenu.this.dispatchEvent(new WindowEvent(ChoiceMenu.this, WindowEvent.WINDOW_CLOSING)));

        initializeButtons();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(ChoiceMenu.this,
                        "Are you sure to logout & quit?", "Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    ChoiceMenu.this.dispose();
                    new LoginTypeSelection(manager);
                }
            }
        });
        setBounds(100, 100, 400, 400);
    }

    abstract void initializeButtons();

    void initializeLayout(JButton[] options) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        container.setLayout(new GridLayout(options.length + 1, 1));

        for (JButton option : options) {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(flowLayout);
            wrapper.add(option);
            container.add(wrapper);
        }

        container.add(toLogout);
    }

}
