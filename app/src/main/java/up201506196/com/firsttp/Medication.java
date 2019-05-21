package up201506196.com.firsttp;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Medication {

    private int _id;
    private String _name;
    private int _quantity;

    public Medication() {

    }

    public Medication(int id, String name, int quantity) {
        this._id = id;
        this._name = name;
        this._quantity = quantity;
    }

    public Medication(String name, int quantity) {
        this._name = name;
        this._quantity = quantity;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setMedicationName(String name) {
        this._name = name;
    }

    public String getMedicationName() {
        return this._name;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }

}