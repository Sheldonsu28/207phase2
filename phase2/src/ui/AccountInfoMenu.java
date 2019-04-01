package ui;

import account.Account;
import account.Depositable;
import account.Indebtable;
import account.Withdrawable;
import atm.User;
import transaction.Transaction;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

class AccountInfoMenu extends SubMenu {

    private final User user;
    private final JButton showOverallBrief;
    private final ArrayList<Account> accounts;
    private final JTable infoTable;
    private Object[][] data;
    private String[] columnName;

    AccountInfoMenu(User user) {
        super("Account Information");

        this.user = user;
        accounts = user.getAllAccounts();

        setData();

        showOverallBrief = new JButton("Show Overall Brief");
        showOverallBrief.addActionListener(e -> showOverallBrief());

        infoTable = new JTable(new TableManager());
        infoTable.setFillsViewportHeight(true);
        infoTable.setRowHeight(20);
        infoTable.getColumn("Transactions").setCellRenderer(
                (table, value, isSelected, hasFocus, row, column) -> (JButton) value);
        infoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = infoTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = infoTable.getSelectedRow();

                Object object = infoTable.getValueAt(row, column);
                if (object instanceof JButton) {
                    ((JButton) object).doClick();
                }
            }
        });
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        infoTable.setDefaultRenderer(String.class, centerRenderer);

        initializeLayout();

        setBounds(100, 100, 800, 400);
        setMinimumSize(new Dimension(800, 400));
        setVisible(true);
    }

    private void initializeLayout() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        JPanel briefPanel = new JPanel(flowLayout);
        briefPanel.add(showOverallBrief);

        container.setLayout(new BorderLayout());
        container.add(infoTable.getTableHeader(), BorderLayout.PAGE_START);
        container.add(infoTable, BorderLayout.CENTER);
        container.add(briefPanel, BorderLayout.SOUTH);
    }

    private void setData() {
        columnName = new String[]{"ID", "TYPE", "Owners", "Creation Date", "Balance", "Net Balance", "Debt Limit",
                "Depositable", "Withdrawable", "Transactions"};

        data = new Object[accounts.size()][columnName.length];

        for (int acc = 0; acc < accounts.size(); acc++) {
            Account account = accounts.get(acc);

            data[acc][0] = account.getId();
            data[acc][1] = account.getClass().getSimpleName();
            data[acc][2] = account.getOwners();
            data[acc][3] = account.getTimeCreated();
            data[acc][4] = account.getBalance();
            data[acc][5] = account.getNetBalance();
            data[acc][6] = getDebtLimit(account);
            data[acc][7] = account instanceof Depositable;
            data[acc][8] = account instanceof Withdrawable;
            data[acc][9] = "check";
        }

    }

    private String getDebtLimit(Account account) {
        if (account instanceof Indebtable) {
            return Double.toString(((Indebtable) account).getDebtLimit());
        } else {
            return "N/A";
        }
    }

    private void showOverallBrief() {
        StringBuilder info = new StringBuilder();

        for (Account account : user.getAllAccounts()) {
            info.append(account).append("\n");
        }

        MainFrame.showInfoMessage(info.toString(), "Accounts Brief");
    }

    private final class TableManager extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnName.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return data[rowIndex][columnIndex].toString();

                case 7:
                case 8:
                    return data[rowIndex][columnIndex];

                case 9:
                    JButton button = new JButton(data[rowIndex][columnIndex].toString());
                    button.addActionListener(e -> {
                        Account targetAccount = accounts.get(rowIndex);

                        StringBuilder transactionText = new StringBuilder();

                        for (Transaction transaction : targetAccount.getTransactions()) {
                            transactionText.append(transaction).append("\n");
                        }

                        if (transactionText.toString().equals(""))
                            transactionText.append("No recent transaction!");

                        MainFrame.showInfoMessage(transactionText.toString(),
                                "Transaction for " + targetAccount.getId());
                    });
                    return button;

                default:
                    return "N/A";

            }
        }

        @Override
        public String getColumnName(int col) {
            return columnName[col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }
}
