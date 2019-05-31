package up201506196.com.firsttp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "MedLook.db", null, 1);
    }

    // Stock Medication
    public static final String TABLE_MEDICATION = "medication";
    public static final String COLUMN_MID = "id";
    public static final String COLUMN_MUSER = "user";
    public static final String COLUMN_MNAME = "name";
    public static final String COLUMN_MQUANTITY = "quantity";

    // Register Cholesterol
    public static final String TABLE_CHOLESTEROL = "cholesterol";
    public static final String COLUMN_CID = "id";
    public static final String COLUMN_CUSER = "user";
    public static final String COLUMN_CVALUE = "name";
    public static final String COLUMN_CDATE = "date";




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

        String CREATE_CHOLESTEROL_TABLE = "CREATE TABLE " +
                TABLE_CHOLESTEROL + "("
                + COLUMN_CID + " integer primary key autoincrement,"
                + COLUMN_CVALUE + " INTEGER,"
                + COLUMN_CDATE + " DATE,"
                + COLUMN_CUSER + " TEXT,"
                + " FOREIGN KEY ("+COLUMN_CUSER+") REFERENCES "+"user"+"("+"email"+"));";
        db.execSQL(CREATE_CHOLESTEROL_TABLE);

    }

    @Override // here I am trying to enable the foreign keys
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("DROP TABLE IF EXISTS medication");
        db.execSQL("DROP TABLE IF EXISTS cholesterol");

        onCreate(db);
    }



    // User handling
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


    // Stock Medication handling
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

    //Register Cholesterol handling
    public void addCholesterol(Cholesterol cholesterol) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CVALUE, cholesterol.getValue());
        values.put(COLUMN_CDATE, date);
        values.put(COLUMN_CUSER, cholesterol.getUser());

        // insert row
        db.insert(TABLE_CHOLESTEROL, null, values);

        // close db connection
        db.close();
    }
}
