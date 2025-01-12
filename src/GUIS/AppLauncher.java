package GUIS;

import db_objs.User;

import javax.swing.*;
import java.math.BigDecimal;

public class AppLauncher {
    public static void main(String[] args) {
        // Use invokeLater to make updates to the GUI more thread-safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginGui loginGui = new LoginGui();
                loginGui.setVisible(true);

//                RegisterGui registerGui = new RegisterGui();
//                registerGui.setVisible(true);

//                User user = new User(1, "username", "password", new BigDecimal("20.00"));
//                BankingAppGui bankingApGui = new BankingAppGui(user);

                //bankingApGui.setVisible(true);
            }
        });
    }
}
