package up201506196.com.firsttp;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Medication {


    private String name;
    private int quantity;

    public Medication() {

    }

    public Medication(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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



}