package GUIS;

import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

/*
    Displays a custom dialog for our BankingAppGui
 */
public class BankingAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGui bankingAppGui;
    private BankingAppDialog bankingAppDialog;

    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;


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

    // this gui is deposit, withdraw and transfer
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
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);

    }

    // this gui button display is for deposit, withdraw, and transfer
    public void addActionButton (String action)
    {
        actionButton = new JButton(action);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.setHorizontalAlignment(SwingConstants.CENTER);
        actionButton.addActionListener(this);
        add(actionButton);

    }

    // this gui is for transfer
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

    public void addPastTransactionComponents()
    {
        // container where we will still store each transaction
        pastTransactionPanel = new JPanel();

        // make layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // add scroll ability to the container
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // displays the vertical scroll only when it is required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 80);

        // perform db call to retrieve all the past transaction and store into array list
        pastTransactions = MyJDBC.getPastTransaction(user);

        // iterate through the list and add to the gui
        for (Transaction pastTransaction : pastTransactions) {

            // create a container to store an individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // create transaction type label
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction amount label
            JLabel transactionAmountLabel = new JLabel(pastTransaction.getTransactionAmount().toString());
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction date label
            JLabel transactionDateLabel = new JLabel(pastTransaction.getTransactionDate().toString());
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));
            transactionDateLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // add to the container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST);
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST);
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.NORTH);

            // give a white background to each container
            pastTransactionContainer.setBackground(Color.WHITE);

            // give a black border to each transaction container with 3px thickness
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

            // add transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // add to the dialog
        add(scrollPane);
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

            // close dialog
            this.dispose();
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

    private void handleTransfer (User user, String transferredUser, float amountValue)
    {
        // make sure user cannot transfer themselves
        if(!user.getUsername().equals(transferredUser))
        {
            // attempt to perform transfer
            if (MyJDBC.transfer(user, transferredUser, amountValue))
            {
                // show success dialog
                JOptionPane.showMessageDialog(this, "Transfer Success!");

                // reset the text field
                resetFieldsAndUpdateCurrentBalance();

                // close banking app dialog
                this.dispose();
            }
            else
            {
                // show failed dialog
                JOptionPane.showMessageDialog(this, "Transfer Failed! Please make username is correct!");

                // reset all text field
                resetFieldsAndUpdateCurrentBalance();
            }
        }

        // otherwise display dialog with error message
        else {
            JOptionPane.showMessageDialog(this, "Error: Cannot transfer money to yourself!");
            resetFieldsAndUpdateCurrentBalance();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // get amount val
        float amountVal = Float.parseFloat(enterAmountField.getText());

        // make sure input amount is positive
        if (amountVal >= 0)
        {
            // pressed deposit
            if (buttonPressed.equalsIgnoreCase("Deposit")) {
                // we want to handle the deposit transaction
                handleTransaction(buttonPressed, amountVal);
            }

            // pressed transfer and withdraw
            else {

                // 0: if the two BigDecimal objects are equal.
                // -1: if the first BigDecimal object is less than the second.
                // 1: if the first BigDecimal object is greater than the second.
                int result = user.getCurrentBalance().compareTo(new BigDecimal(amountVal));

                // validate input amount by check if withdraw or transfer amount is less than current balance
                if (result >= 0) {

                    // implement withdraw
                    if (buttonPressed.equalsIgnoreCase("Withdraw"))
                    {
                        // handle withdraw
                        handleTransaction(buttonPressed, amountVal);
                    }

                    // implement transfer
                    else
                    {
                        // get username to transfer
                        String transferredUser = enterUserField.getText();

                        // handle transfer
                        handleTransfer(user, transferredUser, amountVal);
                    }
                }

                // otherwise display dialog with error message
                else {
                    JOptionPane.showMessageDialog(this, "Error: Input value has to be less than current balance");
                }
            }
        }

        // otherwise display dialog with error message
        else {
            JOptionPane.showMessageDialog(this, "Error: Cannot input negative amount.");
        }
    }
}
