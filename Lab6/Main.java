package Lab6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private JFrame frame;
    private JTextField customerIdField, productIdField, quantityField;
    private JTable orderTable;
    private List<Order> orders = getOrders();
    private JTextArea orderHistoryTextArea;

    public Main() {
        frame = new JFrame("Order Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // UI components
        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField(20);
        JLabel productIdLabel = new JLabel("Product ID:");
        productIdField = new JTextField(20);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(20);

        JButton addOrderButton = new JButton("Add Order");
        JButton viewHistoryButton = new JButton("View Order History");
        JButton calculateTotalButton = new JButton("Calculate Total");

        // JTextArea để hiển thị lịch sử đơn hàng
        orderHistoryTextArea = new JTextArea(10, 40);
        orderHistoryTextArea.setEditable(false);  // Đặt thành không chỉnh sửa
        JScrollPane scrollPane1 = new JScrollPane(orderHistoryTextArea);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.add(customerIdLabel);
        panel.add(customerIdField);
        panel.add(productIdLabel);
        panel.add(productIdField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(addOrderButton);
        panel.add(viewHistoryButton);
        panel.add(calculateTotalButton);


        // Order table
        orderTable = new JTable(new OrderTableModel(orders));
        JScrollPane scrollPane = new JScrollPane(orderTable);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(scrollPane1, BorderLayout.SOUTH);

        // Button actions
        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        viewHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewOrderHistory();
            }
        });

        calculateTotalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        frame.setVisible(true);
    }

    public List<Order> getOrders() {
        List<Order> orders = OrderDao.getAllOrders(); // Giả sử đây là truy vấn DB
        return (orders != null) ? orders : new ArrayList<>();
    }


    private void addOrder() {
        try {
            // Kiểm tra xem các trường nhập có rỗng không
            if (customerIdField.getText().trim().isEmpty() ||
                    productIdField.getText().trim().isEmpty() ||
                    quantityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chuyển đổi dữ liệu
            int customerId = Integer.parseInt(customerIdField.getText().trim());
            int productId = Integer.parseInt(productIdField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            // Thêm đơn hàng vào database
            OrderDao orderDAO = new OrderDao();
            orderDAO.addOrder(customerId, productId, quantity);

            JOptionPane.showMessageDialog(frame, "Order added successfully!");

            // Xóa nội dung các trường nhập sau khi thêm đơn hàng thành công
            customerIdField.setText("");
            productIdField.setText("");
            quantityField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewOrderHistory() {
        String customerIdText = customerIdField.getText(); // Lấy giá trị từ trường nhập liệu customerId
        if (customerIdText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid customer ID.");
            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdText); // Chuyển đổi thành số nguyên
            // Tiếp tục xử lý nếu customerId hợp lệ
            OrderDao orderDAO = new OrderDao();
            String orderHistory = orderDAO.getOrderHistory(customerId);
            orderHistoryTextArea.setText(orderHistory);  // Hiển thị lịch sử đơn hàng trong JTextArea
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid customer ID. Please enter a valid number.");
        }
    }
    
    private void calculateTotal() {
        // Calculate total price for an order
        int orderId = Integer.parseInt(JOptionPane.showInputDialog("Enter Order ID:"));
        OrderDao orderDAO = new OrderDao();
        double total = orderDAO.calculateOrderTotal(orderId);

        // Display total price
        JOptionPane.showMessageDialog(frame, "Total Order Amount: " + total);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
