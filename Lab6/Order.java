package Lab6;

    public class Order {
        private int orderId;
        private int customerId;
        private double total;

        // Constructor
        public Order(int orderId, int customerId, double total) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.total = total;
        }

        // Getter methods
        public int getOrderId() {
            return orderId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public double getTotal() {
            return total;
        }

        // Optional: Setter methods if you need to update the fields
        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }