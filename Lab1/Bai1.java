package Lab1;

class lab1aa {
    private int n;
    private int count =1;
    private boolean turn = false;

    public  lab1aa( int n) {
        this.n = n;
    }
    public synchronized void odd() {
        while(count <=n) {
            while(turn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count<=n) {
                System.out.println("Lẻ: " +count);
                count++;
                turn=true;
                notify();
            }
        }
    }
    public synchronized void even() {
        while(count <=n) {
            while(!turn) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (count<=n) {
                System.out.println("Chẵn: "+count);
                count++;
                turn=false;
                notify();
            }
        }
    }
}
public class Bai1 {
    public static void main(String[] args) {
        // Bạn có thể thay đổi giá trị n tùy ý
        int n=10;
        lab1aa thread = new lab1aa(n);

        Thread odd= new Thread(thread::odd);
        Thread even= new Thread(thread::even);
        odd.start();
        even.start();
    }
}