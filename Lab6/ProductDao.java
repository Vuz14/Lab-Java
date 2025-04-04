package Lab6;

import java.sql.*;

public class ProductDao {

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Load driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found.", e);
        }
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=OrderManagement;encrypt=true;trustServerCertificate=true", "sa", "123");
    }

    public void addProduct(String name, double price) {
        String insertProductSQL = "INSERT INTO products (name, price) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertProductSQL)) {

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(int productId) {
        String selectProductSQL = "SELECT * FROM products WHERE product_id = ?";
        Product product = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectProductSQL)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                product = new Product(productId, name, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }
}