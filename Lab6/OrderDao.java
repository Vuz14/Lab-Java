package Lab6;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private  Connection getConnection() throws SQLException {
        // Implement connection to SQL Server
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Load driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found.", e);
        }
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=OrderManagement;encrypt=true;trustServerCertificate=true", "sa", "123");
    }

    public void addOrder(int customerId, int productId, int quantity) {
        String insertOrderSQL = "INSERT INTO orders (customer_id) VALUES (?)";
        String insertOrderItemSQL = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {

            orderStmt.setInt(1, customerId);
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                // Lấy giá của sản phẩm từ bảng products
                double price = getProductPrice(productId);

                // Chèn dữ liệu vào bảng order_items
                try (PreparedStatement orderItemStmt = conn.prepareStatement(insertOrderItemSQL)) {
                    orderItemStmt.setInt(1, orderId);
                    orderItemStmt.setInt(2, productId);
                    orderItemStmt.setInt(3, quantity);
                    orderItemStmt.setDouble(4, price);  // Chèn giá sản phẩm
                    orderItemStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private double getProductPrice(int productId) {
        String sql = "SELECT price FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu không tìm thấy sản phẩm
    }


    public String getOrderHistory(int customerId) {
        String selectOrdersSQL = "SELECT o.order_id, oi.product_id, oi.quantity FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id WHERE o.customer_id = ?";
        StringBuilder orderHistory = new StringBuilder();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectOrdersSQL)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                orderHistory.append("Order ID: ").append(orderId).append(", Product ID: ").append(productId)
                        .append(", Quantity: ").append(quantity).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderHistory.toString();
    }

    public double calculateOrderTotal(int orderId) {
        String selectOrderItemsSQL = "SELECT oi.quantity, p.price FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id WHERE oi.order_id = ?";
        double total = 0;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectOrderItemsSQL)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                total += quantity * price;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, customer_id, total FROM orders";

        OrderDao orderDao = new OrderDao(); // Tạo một instance của OrderDao

        try (Connection conn = orderDao.getConnection(); // Gọi phương thức getConnection từ instance
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("total")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


}