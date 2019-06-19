package up201506196.com.firsttp;

public class Record {

    private int id;
    private String type;
    private int value;
    private int user;


    public Record() {

    }

    public Record(int value, int user, String type) {
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

    public void setUser(int user) {
        this.user = user;
    }

    public int getUser() {
        return this.user;
    }



}

