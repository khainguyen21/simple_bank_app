package GUIS;

/*
    This gui will allow user to log in or launch the register gui
    This extends from the BaseFrame which means we will need to define our own addGuiComponent()
 */

import db_objs.MyJDBC;
import db_objs.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGui extends BaseFrame {

    public LoginGui()
    {
        super("Banking App Login");
    }
    @Override
    protected void addGuiComponents() {

        // create banking app label
        JLabel bankingAppLabel = new JLabel("Banking Application");

        // set the location for banking app label, super.getWidth
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);

        // change the font style
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // center text in JLabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add label to GUI
        add(bankingAppLabel);

        // create username label
        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(20, 120, super.getWidth()-30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        // create a username text field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(usernameField);

        // create a password label
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(20, 280, getWidth() - 30, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create the password text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 320, getWidth() - 50 , 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460, getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get username
                String username = usernameField.getText();

                // get password String.valueOf - use to cast to String
                String password = String.valueOf(passwordField.getPassword());

                // validate login
                User user = MyJDBC.validateLogin(username, password);

                // if user is null it means invalid otherwise it is a valid account
                if (user != null) {
                    // means valid login
                    // dispose this gui
                    LoginGui.this.dispose();

                    // launch bank app gui
                    BankingAppGui bankingAppGui = new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    // show success dialog message
                    JOptionPane.showMessageDialog(bankingAppGui, "Login Successfully" );
                }
                else {
                    JOptionPane.showMessageDialog(LoginGui.this, "Login failed... ");

                    // empty all the fields
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });
        add(loginButton);

        // create register label
        JLabel registerLabel = new JLabel("<html> <a href = \"#\"> Don't have an account? Register Here </a> </html>");
        registerLabel.setBounds(0, 510, getWidth() -10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // adds an event listener so when the mouse is clicked, it will launch the register gui
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose the login gui
                LoginGui.this.dispose();

                // launch the register gui
                RegisterGui registerGui = new RegisterGui();
                registerGui.setVisible(true);
            }
        });
        add(registerLabel);
    }
}
