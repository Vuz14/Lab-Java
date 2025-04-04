package Lab6;

import java.sql.*;

public class CustomerDao {

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Load driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found.", e);
        }
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=OrderManagement;encrypt=true;trustServerCertificate=true", "sa", "123");    }

    public void addCustomer(String name) {
        String insertCustomerSQL = "INSERT INTO customers (name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertCustomerSQL)) {

            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getCustomerName(int customerId) {
        String selectCustomerSQL = "SELECT name FROM customers WHERE customer_id = ?";
        String customerName = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectCustomerSQL)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customerName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerName;
    }
}