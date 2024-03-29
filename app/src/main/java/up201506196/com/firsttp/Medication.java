package up201506196.com.firsttp;


public class Medication {

    private int id;
    private String name;
    private int quantity;
    private int user;

    public Medication() {

    }

    public Medication(String name, int quantity, int user) {
        this.name = name;
        this.quantity = quantity;
        this.user= user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public int getUser() {
        return this.user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}