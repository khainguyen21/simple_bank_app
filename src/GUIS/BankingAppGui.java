package GUIS;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Performs banking functions such as depositing, withdrawing, seeing past transaction, and transferring
    This extends from BaseFrame which means we will need to define our own addGuiComponent

 */

public class BankingAppGui extends BaseFrame implements ActionListener {

    private JTextField currentBalanceField;
    public JTextField getCurrentBalance()
    {
        return currentBalanceField;
    }
    public BankingAppGui(User user)
    {
        super("Banking App", user);
    }

    @Override
    protected void addGuiComponents() {

        // create welcome message
        String welcomeMessage = "<html>" +
                "<body style = 'text-align: center'>" +
                "<b>Hello " + user.getUsername() + "</b><br>" +
                "What would you like to do today? </body></html>";

        // display welcome message
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        // create current balance label
        JLabel currentBalance = new JLabel("Current Balance");
        currentBalance.setBounds(0, 80, getWidth() - 10, 30);
        currentBalance.setFont(new Font("Dialog", Font.BOLD, 16));
        currentBalance.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalance);

        // create current balance field
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        // create deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.addActionListener(this);
        add(depositButton);

        // create withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        // create past transaction button
        JButton pastTransactionButton = new JButton("Past Transactions");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        // create transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 380, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        // create log out button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, getHeight()-100, getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);
        add(logoutButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Get string of a button that user click on
        String buttonPressed = e.getActionCommand();

        if (buttonPressed.equalsIgnoreCase("Logout"))
        {
            // dispose banking app gui
            BankingAppGui.this.dispose();

            // launch login gui
            new LoginGui().setVisible(true);
        }

        else {

            // other functions
            BankingAppDialog bankingAppDialog = new BankingAppDialog(this, user);
            bankingAppDialog.setTitle(buttonPressed);

            // if the button pressed is deposit, withdraw, or transfer
            if (buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                    || buttonPressed.equalsIgnoreCase("Transfer"))
            {
                // add in the current balance and amount gui components to the dialog
                bankingAppDialog.addCurrentBalanceAndAmount();

                // distinguish action between deposit, withdraw, and transfer
                bankingAppDialog.addActionButton(buttonPressed);

                // for the transfer action it will require more components
                if (buttonPressed.equalsIgnoreCase("Transfer"))
                {
                    bankingAppDialog.addUserField();
                }

                bankingAppDialog.setVisible(true);
            }
        }

    }
}
