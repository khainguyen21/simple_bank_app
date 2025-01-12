package GUIS;

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame{
    public RegisterGui()
    {
        super("Bank App Register");
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

        // create username text field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 22));
        add(usernameField);

        // create password label
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setBounds(20, 220, getWidth() - 30, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // create password text field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, getWidth() - 50 , 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // create re-type password label
        JLabel repasswordLabel = new JLabel("Re-type password: ");
        repasswordLabel.setBounds(20, 320, getWidth() - 30, 24);
        repasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(repasswordLabel);

        // create re-type password text field
        JPasswordField repasswordField = new JPasswordField();
        repasswordField.setBounds(20, 360, getWidth() - 50 , 40);
        repasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(repasswordField);


        // create login button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 460, getWidth() - 50, 40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get username
                String username = usernameField.getText();

                // get password
                String password = String.valueOf(passwordField.getPassword());

                // get re-type password
                String rePassword = String.valueOf(repasswordField.getPassword());


                // we will need to validate the user input
                if (!validateUserInput(username, password, rePassword))
                {
                    // create a fail dialog
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: Username must be at least 6 characters\n" +
                            " (and/or) Password must match");

                    // empty all text fields
                    usernameField.setText("");
                    passwordField.setText("");
                    repasswordField.setText("");
                }
                else {
                    if (MyJDBC.register(username, password))
                    {
                        // register success
                        // dispose of this gui
                        RegisterGui.this.dispose();

                        // launch login gui
                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);

                        // create a success dialog
                        JOptionPane.showMessageDialog(loginGui, "Registered Account Successfully");
                    }
                    else
                    {
                        // display dialog username is already taken
                        JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username already taken");

                        // empty all the fields
                        usernameField.setText("");
                        passwordField.setText("");
                        repasswordField.setText("");
                    }
                }
            }
        });
        add(registerButton);

        // create register label
        JLabel loginLabel = new JLabel("<html> <a href = \"#\">Have an account? Sign-in here</a> </html>");
        loginLabel.setBounds(0, 510, getWidth() -10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // dispose register gui
                RegisterGui.this.dispose();

                // launch the login gui
                LoginGui loginGui = new LoginGui();
                loginGui.setVisible(true);
            }
        });
        add(loginLabel);
    }

    private boolean validateUserInput (String username, String password, String rePassword)
    {
        // all fields must have a value
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            return false;
        }

        // username has at least 6 characters long
        if (username.length() < 6)
        {
            return false;
        }

        // password and repassword has to be the same
        if (!password.equals(rePassword))
        {
            return false;
        }

        return true;
    }
}
