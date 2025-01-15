package GUIS;


import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/*
    Displays a custom dialog for our BankingAppGui
 */
public class BankingAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGui bankingAppGui;

    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;

    public BankingAppDialog (BankingAppGui bankingAppGui, User user)
    {
        // set the size
        setSize(400, 400);

        // add focus to the dialog (can't interact with anything else until dialog is closed)
        setModal(true);

        // loads in the center of our banking app gui
        setLocationRelativeTo(bankingAppGui);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // prevents dialog from being resized
        setResizable(false);

        // allows us to manually specify the size and position of each component
        setLayout(null);

        // we will need to reference to our gui so that we can update the current balance
        this.bankingAppGui = bankingAppGui;

        // we will need to access to the user info to make updates to our database or retrieve data about the user
        this.user = user;
        //addCurrentBalanceAndAmount();
    }

    public void addCurrentBalanceAndAmount()
    {
        // balance label
        balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // enter amount label
        enterAmountLabel = new JLabel("Enter Amount: ");
        enterAmountLabel.setBounds(0, 60, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        // enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.RIGHT);
        add(enterAmountField);

    }

    // display actions between deposit, withdraw, and transfer
    public void addActionButton (String action)
    {
        actionButton = new JButton(action);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.setHorizontalAlignment(SwingConstants.CENTER);
        actionButton.addActionListener(this);
        add(actionButton);

    }

    public void addUserField()
    {
        // enter user label
        enterUserLabel = new JLabel("Enter username: ");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        // enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 180, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }

    public void handleTransaction (String transactionType, float amountVal)
    {
        Transaction transaction;

        if (transactionType.equalsIgnoreCase("Deposit")) {
            // deposit transaction type
            // add to current balance
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));

            // create transaction
            // we leave date null because we are going to be using the NOW() in sql which will get the current date
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
        }
        else {
            // withdraw transaction type
            // subtract to current balance
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));

            // we want to show a negative sign for the amount val when withdrawing
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(-amountVal), null);
        }

        // update database
        if(MyJDBC.addTransactionToDataBase(transaction) && MyJDBC.updateCurrentBalance(user)){
            // show success dialog
            JOptionPane.showMessageDialog(this, transactionType + " Successfully");

            // reset the fields
            resetFieldsAndUpdateCurrentBalance();
        }
        else{
            // show failure dialog
            JOptionPane.showMessageDialog(this, transactionType + " Failed");

        }
    }

    private void resetFieldsAndUpdateCurrentBalance()
    {
        // reset all the fields
        enterAmountField.setText("");

        // only appears when transfer is clicked
        if (enterUserField != null)
        {
            enterUserField.setText("");
        }

        // update current balance on the dialog
        balanceLabel.setText("Balance: $" + user.getCurrentBalance());

        bankingAppGui.getCurrentBalance().setText("$ " + user.getCurrentBalance());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // get amount val
        float amountVal = Float.parseFloat(enterAmountField.getText());
        // pressed deposit
        if (buttonPressed.equalsIgnoreCase("Deposit"))
        {
            // we want to handle the deposit transaction
            handleTransaction(buttonPressed, amountVal);
        }
    }
}
