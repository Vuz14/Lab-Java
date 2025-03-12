package Lab2;

class Chopstick {
    private final int id;
    private boolean isUsed = false;

    public Chopstick(int id) {
        this.id = id;
    }

    public synchronized void pickUp(String philosopher) throws InterruptedException {
        while (isUsed) {
            wait();
        }
        isUsed = true;
        System.out.println(philosopher + " picked up chopstick " + id);
    }

    public synchronized void putDown(String philosopher) {
        isUsed = false;
        System.out.println(philosopher + " put down chopstick " + id);
        notifyAll();
    }

    public int getId() {
        return id;
    }
}

class Philosopher implements Runnable {
    private final String name;
    private final Chopstick leftChopstick;
    private final Chopstick rightChopstick;

    public Philosopher(String name, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.name = name;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                // Pick up chopsticks in specific order to prevent circular wait (avoid deadlock)
                if (leftChopstick.getId() < rightChopstick.getId()) {
                    pickUpChopsticks();
                } else {
                    // Reverse order for the last philosopher
                    pickUpChopsticksReverse();
                }
                eat();
                putDownChopsticks();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println(name + " is thinking...");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void eat() throws InterruptedException {
        System.out.println(name + " is eating...");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void pickUpChopsticks() throws InterruptedException {
        leftChopstick.pickUp(name);
        rightChopstick.pickUp(name);
    }

    private void pickUpChopsticksReverse() throws InterruptedException {
        rightChopstick.pickUp(name);
        leftChopstick.pickUp(name);
    }

    private void putDownChopsticks() {
        leftChopstick.putDown(name);
        rightChopstick.putDown(name);
    }
}

public class Dining_Philosophy {
    public static void main(String[] args) {
        int numPhilosophers = 5;

        Chopstick[] chopsticks = new Chopstick[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            chopsticks[i] = new Chopstick(i);
        }

        Philosopher[] philosophers = new Philosopher[numPhilosophers];
        Thread[] threads = new Thread[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            Chopstick leftChopstick = chopsticks[i];
            Chopstick rightChopstick = chopsticks[(i + 1) % numPhilosophers];

            // Triết gia cuối cùng sẽ đổi thứ tự nhặt đũa để tránh deadlock
            if (i == numPhilosophers - 1) {
                philosophers[i] = new Philosopher("Philosopher " + i, rightChopstick, leftChopstick);
            } else {
                philosophers[i] = new Philosopher("Philosopher " + i, leftChopstick, rightChopstick);
            }

            threads[i] = new Thread(philosophers[i]);
            threads[i].start();
        }
    }
}
