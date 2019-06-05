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

    // Register Regists
    public static final String TABLE_REGISTS = "regists";
    public static final String COLUMN_RID = "id";
    public static final String COLUMN_RTYPE = "type";
    public static final String COLUMN_RUSER = "user";
    public static final String COLUMN_RVALUE = "name";
    public static final String COLUMN_RDATE = "date";

    //Appointments
    public static final String TABLE_App = "appointment";
    public static final String COLUMN_AID = "id";
    public static final String COLUMN_AUSER = "user";
    public static final String COLUMN_ADESCRIPTION = "description";
    public static final String COLUMN_ADATE = "date";



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

        String CREATE_REGISTS_TABLE = "CREATE TABLE " +
                TABLE_REGISTS + "("
                + COLUMN_RID + " integer primary key autoincrement,"
                + COLUMN_RVALUE + " INTEGER,"
                + COLUMN_RDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_RTYPE + " TEXT,"
                + COLUMN_RUSER + " TEXT,"
                + " FOREIGN KEY ("+COLUMN_RUSER+") REFERENCES "+"user"+"("+"email"+"));";
        db.execSQL(CREATE_REGISTS_TABLE);

        String CREATE_APP_TABLE = "CREATE TABLE " +
                TABLE_App + "("
                + COLUMN_AID + " integer primary key autoincrement,"
                + COLUMN_ADESCRIPTION + " TEXT,"
                + COLUMN_ADATE + " DATE,"
                + COLUMN_AUSER + " TEXT,"
                + " FOREIGN KEY ("+COLUMN_MUSER+") REFERENCES "+"user"+"("+"email"+"));";
        db.execSQL(CREATE_APP_TABLE);

    }

    @Override // here I am trying to enable the foreign keys
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("DROP TABLE IF EXISTS medication");
        db.execSQL("DROP TABLE IF EXISTS regists");
        db.execSQL("DROP TABLE IF EXISTS appointment");

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

    //Register Regist handling
    public void addRegist(Regist regist) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RVALUE, regist.getValue());
        values.put(COLUMN_RTYPE, regist.getType());
        values.put(COLUMN_RUSER, regist.getUser());

        // insert row
        db.insert(TABLE_REGISTS, null, values);

        // close db connection
        db.close();
    }

    public void addApp(App app){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADESCRIPTION, app.getDescription());
        values.put(COLUMN_AUSER, app.getUser());
        values.put(COLUMN_ADATE, app.getDate());

        // insert row
        db.insert(TABLE_App, null, values);

        // close db connection
        db.close();

    }
}
