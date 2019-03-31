package ui;

import account.Withdrawable;
import atm.BankManager;
import atm.User;
import transaction.WithdrawTransaction;

import javax.swing.*;
import java.awt.*;


public class WithdrawMenu extends SubMenu {
    private JComboBox<Withdrawable> accountSelection;
    private JButton confirmationButton;
    private JComboBox<Integer> UserInputAmount;
    private WithdrawTransaction transaction;

    WithdrawMenu(User user, BankManager manager) {
        super("Account Selection");


        accountSelection = new JComboBox<>(user.getAccountListOfType(Withdrawable.class).toArray(new Withdrawable[0]));

        UserInputAmount = new JComboBox<>(new Integer[]{5, 10, 20, 50, 100, 150, 200, 250});
        int amount =(int) UserInputAmount.getSelectedItem();

        confirmationButton = new JButton("Confirm Withdraw");
        confirmationButton.addActionListener(e ->{
            Withdrawable selectedAccount = (Withdrawable) accountSelection.getSelectedItem();
            this.transaction = new WithdrawTransaction(user, manager.getMachineList().get(0), selectedAccount,amount);
            if(selectedAccount != null && amount != 0){
                if (transaction.perform()) {
                    JOptionPane.showMessageDialog(this,
                            "Withdraw Performed!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this,
                            "The Action is not perform because something went wrong", "Withdraw Failed",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });


    initializeLayout();
    setVisible(true);

    }

    private void initializeLayout(){
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        flowLayout.setHgap(10);


        JPanel accountSelectionPanel = new JPanel(flowLayout);
        accountSelectionPanel.add(new JLabel("Select Account: "));
        accountSelectionPanel.add(accountSelection);

        JPanel withdrawAmount = new JPanel(flowLayout);
        withdrawAmount.add(new JLabel("Withdraw Amount: "));
        withdrawAmount.add(UserInputAmount);

        JPanel confirmPanel = new JPanel(flowLayout);
        confirmPanel.add(confirmationButton);

        JPanel infoPanel = new JPanel(new GridLayout(12, 1));
        infoPanel.add(accountSelectionPanel);
        infoPanel.add(withdrawAmount);
        infoPanel.add(confirmPanel);

        Box box = Box.createHorizontalBox();
        box.add(accountSelectionPanel);
        box.add(Box.createHorizontalStrut(10));
        box.add(infoPanel);

        container.add(box);
    }
}
