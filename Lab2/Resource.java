package Lab2;

class Resource1 {}

public class Resource {
    private final Resource1 resource1 = new Resource1();
    private final Resource1 resource2 = new Resource1();

    public void processA() {
        synchronized (resource1) {
            System.out.println("Thread A locked resource1");

            synchronized (resource2) {
                System.out.println("Thread A locked resource2");
                // Do work
            }
        }
    }

    public void processB() {
        synchronized (resource1) {  // Cũng khóa theo thứ tự resource1 -> resource2
            System.out.println("Thread B locked resource1");

            synchronized (resource2) {
                System.out.println("Thread B locked resource2");
                // Do work
            }
        }
    }

    public static void main(String[] args) {
        Resource ad = new Resource();

        Thread threadA = new Thread(ad::processA);
        Thread threadB = new Thread(ad::processB);

        threadA.start();
        threadB.start();
    }
}

