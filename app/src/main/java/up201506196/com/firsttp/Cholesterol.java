package up201506196.com.firsttp;

public class Cholesterol {

    private int id;
    private int value;
    private String user;

    public Cholesterol() {

    }

    public Cholesterol(int value, String user) {
        this.value = value;
        this.user = user;
    }


    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

