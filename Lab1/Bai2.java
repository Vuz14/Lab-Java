package Lab1;

import java.util.concurrent.atomic.AtomicInteger;

class TicketCounter {
     private final boolean[] customer;
     private final AtomicInteger nextCustomer;

     public TicketCounter(int n) {
         customer = new boolean[n];
         nextCustomer= new AtomicInteger(0);
     }

     public synchronized int sellTicket(String agent) {
         int customerID = 1;
          customerID= nextCustomer.getAndIncrement();

         if (customerID >= customer.length) {
             return-1;
         }
         customer[customerID] = true;
         System.out.println(agent+ " đã bán vé cho khách hàng mua ghế số " +customerID);
         return customerID;
     }
}

class TicketAgent extends Thread {
    private final TicketCounter counter;
    private final String agent;

    public TicketAgent(TicketCounter counter, String agent) {
        this.counter= counter;
        this.agent= agent;
    }
    @Override
    public void run() {
        while(true) {
            int customers = counter.sellTicket(agent);
            if(customers == -1) break;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
public class Bai2 {
    public static void main(String[] args) {
        int number = 20;
        TicketCounter counter = new TicketCounter(number);


        TicketAgent agentA = new TicketAgent(counter, "Đại lý 1");
        TicketAgent agentB = new TicketAgent(counter, "Đại lý 2");
        TicketAgent agentC = new TicketAgent(counter, "Đại lý 3");

        agentA.start();
        agentB.start();
        agentC.start();
    }
}
