package Lab7.Bai1;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblResult;

    public UI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        lblResult = new JLabel("");

        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Password:"));
        add(txtPassword);
        add(new JLabel(""));
        add(btnLogin);
        add(lblResult);

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            try {
                String hash = HashUtil.hash(password, "SHA-256"); // hoặc "MD5"
                if (username.equals("admin") && hash.equals(HashUtil.hash("123456", "SHA-256"))) {
                    lblResult.setText("Login success!");
                } else {
                    lblResult.setText("Login failed.");
                }
            } catch (Exception ex) {
                lblResult.setText("Error!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new UI();
    }
}
