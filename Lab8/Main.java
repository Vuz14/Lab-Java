package Lab8;

import Lab8.Order;
import Lab8.Product;
import Lab8.Customer;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Order> orders = getOrders(); // lấy dữ liệu mẫu

        LocalDate startDate = LocalDate.of(2021, 2, 1);
        LocalDate endDate = LocalDate.of(2021, 4, 1);

        Set<Product> discountedProducts = orders.stream()
                .filter(order -> order.getCustomer().getTier() == 2)
                .filter(order -> !order.getOrderDate().isBefore(startDate) && !order.getOrderDate().isAfter(endDate))
                .peek(order -> System.out.println("Processing Order: " + order.getId()))
                .flatMap(order -> order.getProducts().stream())
                .map(product -> {
                    if ("Toys".equalsIgnoreCase(product.getCategory())) {
                        return new Product(
                                product.getId(),
                                product.getName(),
                                product.getCategory(),
                                product.getPrice() * 0.9
                        );
                    }
                    return product;
                })
                .collect(Collectors.toSet());

        // In kết quả
        System.out.println("\nDanh sách sản phẩm sau khi giảm giá:");
        discountedProducts.forEach(p -> System.out.println(
                "Product: " + p.getName() + ", Category: " + p.getCategory() + ", Price: " + p.getPrice()
        ));
    }

    private static List<Order> getOrders() {
        Customer customer1 = new Customer(1, 1);
        Customer customer2 = new Customer(2, 2);
        Customer customer3 = new Customer(3, 2);

        Product toy1 = new Product(1, "Lego", "Toys", 100.0);
        Product toy2 = new Product(2, "Doll", "Toys", 80.0);
        Product book = new Product(3, "Book A", "Books", 50.0);

        Order order1 = new Order(1,
                LocalDate.of(2021, 2, 10),
                LocalDate.of(2021, 2, 15),
                "Delivered",
                customer2,
                List.of(toy1, book)
        );

        Order order2 = new Order(2,
                LocalDate.of(2021, 3, 5),
                LocalDate.of(2021, 3, 10),
                "Delivered",
                customer3,
                List.of(toy2)
        );

        Order order3 = new Order(3,
                LocalDate.of(2021, 5, 1),
                LocalDate.of(2021, 5, 5),
                "Delivered",
                customer2,
                List.of(book)
        );

        Order order4 = new Order(4,
                LocalDate.of(2021, 2, 20),
                LocalDate.of(2021, 2, 25),
                "Delivered",
                customer1,
                List.of(toy1)
        );

        return List.of(order1, order2, order3, order4);
    }
}
