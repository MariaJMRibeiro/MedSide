package up201506196.com.firsttp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "MedLook.db", null, 1);
    }

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MEDICATION = "medication";

    public static final String COLUMN_ID = "m_id";
    public static final String COLUMN_MNAME = "m_name";
    public static final String COLUMN_MQUANTITY = "m_quantity";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text)");
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_MEDICATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_MNAME
                + " TEXT," + COLUMN_MQUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
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

        ContentValues values = new ContentValues();
        values.put(COLUMN_MNAME, medication.getMedicationName());
        values.put(COLUMN_MQUANTITY, medication.getQuantity());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_MEDICATION, null, values);
        db.close();
    }

    public Medication findMedication(String mname) {
        String query = "Select * FROM " + TABLE_MEDICATION + " WHERE " + COLUMN_MNAME + " =  \"" + mname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Medication medication = new Medication();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            medication.setID(Integer.parseInt(cursor.getString(0)));
            medication.setMedicationName(cursor.getString(1));
            medication.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            medication = null;
        }
        db.close();
        return medication;
    }

    public boolean deleteMedication(String mname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_MEDICATION + " WHERE " + COLUMN_MNAME + " =  \"" + mname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Medication medication = new Medication();

        if (cursor.moveToFirst()) {
            medication.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_MEDICATION, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(medication.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


}
