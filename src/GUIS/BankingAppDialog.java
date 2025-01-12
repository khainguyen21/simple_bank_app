package GUIS;


import db_objs.User;

import javax.swing.*;
import java.awt.*;

/*
    Displays a custom dialog for our BankingAppGui
 */
public class BankingAppDialog extends JDialog {
    private User user;
    private BankingAppGui bankingAppGui;

    private JLabel balanceLabel, enterAmountLabel;
    private JTextField enterAmountField;


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

    }
}
