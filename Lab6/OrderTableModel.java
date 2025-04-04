package Lab6;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrderTableModel extends AbstractTableModel {
    private List<Order> orders;
    private String[] columns = {"Order ID", "Customer ID", "Total"};

    public OrderTableModel(List<Order> orders) {
        this.orders = (orders != null) ? orders : new ArrayList<>();

    }

    @Override
    public int getRowCount() {
        return orders.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = orders.get(rowIndex);
        switch (columnIndex) {
            case 0: return order.getOrderId();
            case 1: return order.getCustomerId();
            case 2: return order.getTotal();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
}