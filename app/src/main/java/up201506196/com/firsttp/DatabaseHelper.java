package up201506196.com.firsttp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "MedLook.db", null, 1);
    }

    public static final String TABLE_MEDICATION = "medication";

    public static final String COLUMN_MID = "id";
    public static final String COLUMN_MUSER = "user";
    public static final String COLUMN_MNAME = "name";
    public static final String COLUMN_MQUANTITY = "quantity";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user( email text primary key, password text)");
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_MEDICATION + "("
                + COLUMN_MID + " integer primary key autoincrement,"
                + COLUMN_MNAME + " TEXT,"
                + COLUMN_MQUANTITY + " INTEGER,"
                + COLUMN_MUSER + " TEXT,"
                + " FOREIGN KEY ("+COLUMN_MUSER+") REFERENCES "+"user"+"("+"email"+"));";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override // here I am trying to enable the foreign keys
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("DROP TABLE IF EXISTS medication");
        onCreate(db);
    }

    //inserting in database
    public boolean insert(String email, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins= db.insert("user", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    // check if email exists
    public boolean chkemail(String email){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from user where email=?", new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }

    //check email and password
    public boolean emailpassword(String email, String password){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from user where email=? and password=?", new String[]{email,password});
        if (cursor.getCount()>0) return true;
        else return false;

    }

    public void addMedication(Medication medication) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MNAME, medication.getName());
        values.put(COLUMN_MQUANTITY, medication.getQuantity());
        values.put(COLUMN_MUSER, medication.getUser());

        // insert row
        db.insert(TABLE_MEDICATION, null, values);

        // close db connection
        db.close();
    }

    public void deleteMedication(String name, String user){

        SQLiteDatabase db = this.getWritableDatabase();

        // delete raw
        db.delete(TABLE_MEDICATION, COLUMN_MNAME + "=? AND " + COLUMN_MUSER + "=?",  new String[] {name,user});


    }


}
