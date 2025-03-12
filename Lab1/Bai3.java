package Lab1;


import java.util.LinkedList;
import java.util.Queue;

class Shop {
    private final int capacity = 20; // Sức chứa tối đa
    private final Queue<Integer> products = new LinkedList<>();

    // Thêm sản phẩm vào shop
    public synchronized void produce() throws InterruptedException {
        while (products.size() == capacity) {
            System.out.println(" Shop đầy, xưởng tạm dừng sản xuất...");
            wait();
        }
        int product = (int) (Math.random() * 20); // Tạo sản phẩm ngẫu nhiên
        products.add(product);
        System.out.println(" Xưởng sản xuất sản phẩm: " + product + " | Sản phẩm hiện có: " + products.size());
        notifyAll();
    }


    public synchronized int consume() throws InterruptedException {
        while (products.isEmpty()) {
            System.out.println("️ Hết hàng, khách hàng chờ...");
            wait();
        }
        int product = products.poll();
        System.out.println(" Khách hàng mua sản phẩm: " + product + " | Sản phẩm còn lại: " + products.size());
        notifyAll();
        return product;
    }
}


class Producer implements Runnable {
    private final Shop shop;

    public Producer(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        try {
            while (true) {
                shop.produce();
                Thread.sleep(500); // Giả lập thời gian sản xuất
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Khách hàng mua sản phẩm
class Consumer implements Runnable {
    private final Shop shop;
    private static int customersServed = 0;
    private static final int MAX_CUSTOMERS = 10;

    public Consumer(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (customersServed >= MAX_CUSTOMERS) {
                    System.out.println(" Đã phục vụ đủ " + MAX_CUSTOMERS + " khách hàng. Dừng hệ thống.");
                    System.exit(0);
                }
                shop.consume();
                customersServed++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Chạy chương trình
public class Bai3 {
    public static void main(String[] args) {
        Shop shop = new Shop();

        // Tạo và chạy luồng xưởng sản xuất
        Thread producerThread = new Thread(new Producer(shop));
        producerThread.start();

        // Tạo và chạy nhiều khách hàng
        for (int i = 0; i < 5; i++) {
            Thread consumerThread = new Thread(new Consumer(shop));
            consumerThread.start();
        }
    }
}
