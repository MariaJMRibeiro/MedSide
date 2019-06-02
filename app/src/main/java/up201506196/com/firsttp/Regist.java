package up201506196.com.firsttp;

public class Regist {

    private int id;
    private int value;
    private String user;
    private String type;

    public Regist() {

    }

    public Regist(int value, String user, String type) {
        this.value = value;
        this.user = user;
        this.type = type;
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

    public void setType(int value) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }


    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

