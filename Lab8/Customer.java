package Lab8;

public class Customer {
    private int id;
    private int tier;

    public Customer(int id, int tier) {
        this.id = id;
        this.tier = tier;
    }

    public int getId() {
        return id;
    }

    public int getTier() {
        return tier;
    }
}

