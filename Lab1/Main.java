package Lab1;
class NumberPrinter {
    private int n;
    private int count = 1; // Bắt đầu từ 1 (số lẻ)
    private boolean evenTurn = false; // Lượt in số chẵn

    public NumberPrinter(int n) {
        this.n = n;
    }

    public synchronized void printOdd() {
        while (count <= n) {
            while (evenTurn) { // Chờ đến lượt số lẻ
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= n) {
                System.out.println("Lẻ: " + count);
                count++;
                evenTurn = true; // Chuyển sang lượt số chẵn
                notify(); // Đánh thức thread in số chẵn
            }
        }
    }

    public synchronized void printEven() {
        while (count <= n) {
            while (!evenTurn) { // Chờ đến lượt số chẵn
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count <= n) {
                System.out.println("Chẵn: " + count);
                count++;
                evenTurn = false; // Chuyển sang lượt số lẻ
                notify(); // Đánh thức thread in số lẻ
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        int n = 10; // In từ 1 đến 10
        NumberPrinter printer = new NumberPrinter(n);

        Thread oddThread = new Thread(printer::printOdd);
        Thread evenThread = new Thread(printer::printEven);

        oddThread.start();
        evenThread.start();
    }
}
