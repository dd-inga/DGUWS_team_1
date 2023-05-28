package gui;

import common.PasswordStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginDialog extends JDialog {
    private JPasswordField passwordField;   // 비밀번호 입력
    private JButton loginButton;

    private final String adminPassword 
            = PasswordStorage.getAdminPassword();   // admin 비밀번호

    public AdminLoginDialog(Frame owner) {
        super(owner, "Admin Login", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(200, 160);
        setLocationRelativeTo(owner);
        setLayout(null);

//        JPanel panel = new JPanel(new GridLayout(2, 2, 3, 3));
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(5);
        loginButton = new JButton("login");

        passwordLabel.setBounds(20, 20, 80, 30);
        passwordField.setBounds(100, 20, 80, 30);
        loginButton.setBounds(50, 80, 80, 30);

        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());

                if (password.equals(adminPassword)) {
                    dispose();
                    CustomerMainFrame.setAdminLoggedIn(true);
                } else {
                    JOptionPane.showMessageDialog(AdminLoginDialog.this,
                            "Invalid password", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

}
