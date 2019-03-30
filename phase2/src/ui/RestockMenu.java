package ui;

import atm.AtmMachine;
import atm.BankManager;

import javax.swing.*;
import java.awt.*;
import java.util.SortedMap;
import java.util.TreeMap;

public class RestockMenu extends SubMenu {
    private final AtmMachine machine;
    private JTextField stockInfo;
    private JComboBox<Integer> cashTypeSelection, amountSelection;
    private JButton restockButton;

    RestockMenu(BankManager manager) {
        super("Restock Menu");

        machine = manager.getMachineList().get(0);

        cashTypeSelection = new JComboBox<>(machine.getValidCashTypes().toArray(new Integer[0]));
        amountSelection = new JComboBox<>(new Integer[]{1, 5, 10, 50, 100, 200, 500});

        stockInfo = new JTextField(20);
        stockInfo.setEditable(false);
        stockInfo.setFont(new Font("Serif", Font.PLAIN, 18));
        stockInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        restockButton = new JButton("Restock");
        restockButton.addActionListener(e -> {
            Integer cashType = (Integer) cashTypeSelection.getSelectedItem();
            Integer amount = (Integer) amountSelection.getSelectedItem();

            if (cashType != null && amount != null) {
                TreeMap<Integer, Integer> restockInfo = new TreeMap<>();

                restockInfo.put(cashType, amount);

                manager.restockMachine(restockInfo);

                JOptionPane.showMessageDialog(RestockMenu.this, "Restock successful!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(RestockMenu.this,
                        "Cash type or amount not selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            updateStockInfo();
        });

        updateStockInfo();
        initializeLayout();

        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setHgap(5);
        flowLayout.setVgap(5);

        JPanel stockInfoPanel = new JPanel(flowLayout);
        stockInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        stockInfoPanel.add(stockInfo);

        JPanel cashTypePanel = new JPanel(flowLayout);
        cashTypePanel.add(new JLabel("Select cash type: "));
        cashTypePanel.add(cashTypeSelection);

        JPanel amountPanel = new JPanel(flowLayout);
        amountPanel.add(new JLabel("Select restock amount: "));
        amountPanel.add(amountSelection);

        JPanel buttonPanel = new JPanel(flowLayout);
        buttonPanel.add(restockButton);

        container.setLayout(new GridLayout(4, 1));
        container.add(stockInfoPanel);
        container.add(cashTypePanel);
        container.add(amountPanel);
        container.add(buttonPanel);
    }

    private void updateStockInfo() {
        cashTypeSelection.removeAllItems();
        StringBuilder stockText = new StringBuilder();

        SortedMap<Integer, Integer> stock = machine.getStock();

        for (int cashType : stock.keySet()) {
            cashTypeSelection.addItem(cashType);

            stockText.append(String.format("$%d-%d ", cashType, stock.get(cashType)));
        }

        stockInfo.setText(stockText.toString());
    }
}
