package up201506196.com.firsttp;

import java.util.Date;

public class App {

    private int id;
    private String title;
    private String description;
    private String date;
    private int user;

    public App() {

    }

    public App(String description,String date, int user) {
        this.description = description;
        this.date = date;
        this.user= user;
    }

    public void setTitle(String name) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }


    public void setDescription(String name) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDate(String date) {
        this.date =date;
    }

    public String getDate() {
        return this.date;
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
