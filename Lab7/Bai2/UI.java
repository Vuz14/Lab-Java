package Lab7.Bai2;

import javax.swing.*;
import java.awt.*;

public class UI extends JFrame {
    private JTextField txtInput;
    private JComboBox<String> cboAlgorithm;
    private JTextArea txtResult;
    private Encryptable aes = new AESEncryptor();
    private Encryptable rsa;

    public UI() {
        try {
            rsa = new RSAEncryptor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Mã hóa AES/RSA");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtInput = new JTextField();
        cboAlgorithm = new JComboBox<>(new String[]{"AES", "RSA"});
        txtResult = new JTextArea();
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);

        JButton btnEncrypt = new JButton("Encrypt");
        JButton btnDecrypt = new JButton("Decrypt");

        btnEncrypt.addActionListener(e -> {
            try {
                Encryptable enc = getEncryptor();
                String result = enc.encrypt(txtInput.getText());
                txtResult.setText(result);
            } catch (Exception ex) {
                txtResult.setText("Lỗi mã hóa: " + ex.getMessage());
            }
        });

        btnDecrypt.addActionListener(e -> {
            try {
                Encryptable enc = getEncryptor();
                String result = enc.decrypt(txtResult.getText());
                txtResult.setText(result);
            } catch (Exception ex) {
                txtResult.setText("Lỗi giải mã: " + ex.getMessage());
            }
        });

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(new JLabel("Nhập xâu:"));
        topPanel.add(txtInput);
        topPanel.add(cboAlgorithm);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEncrypt);
        buttonPanel.add(btnDecrypt);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtResult), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private Encryptable getEncryptor() {
        return cboAlgorithm.getSelectedItem().equals("AES") ? aes : rsa;
    }

    public static void main(String[] args) {
        new UI();
    }
}
