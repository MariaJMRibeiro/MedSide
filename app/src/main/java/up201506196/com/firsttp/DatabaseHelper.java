package up201506196.com.firsttp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "MedSide.db", null, 1);
    }


    // User
    public static final String TABLE_USER = "user";
    public static final String COLUMN_UID = "id";
    public static final String COLUMN_UEMAIL = "email";
    public static final String COLUMN_UNAME = "name";
    public static final String COLUMN_UPASSWORD = "password";
    public static final String COLUMN_UDATE = "date_of_birth";
    public static final String COLUMN_UGENDER = "gender";
    public static final String COLUMN_UHEIGHT = "height";

    // Medication
    public static final String TABLE_MEDICATION = "medication";
    public static final String COLUMN_MID = "id";
    public static final String COLUMN_MNAME = "name";
    public static final String COLUMN_MQUANTITY = "quantity";
    public static final String COLUMN_MUSER = "user_id";

    // Record
    public static final String TABLE_RECORD = "record";
    public static final String COLUMN_RID = "id";
    public static final String COLUMN_RTYPE = "type";
    public static final String COLUMN_RVALUE = "value";
    public static final String COLUMN_RDATE = "r_date";
    public static final String COLUMN_RUSER = "user_id";


    //Appointment
    public static final String TABLE_APP = "appointment";
    public static final String COLUMN_AID = "id";
    public static final String COLUMN_ATITLE = "title";
    public static final String COLUMN_ADESCRIPTION = "description";
    public static final String COLUMN_ADATE = "a_date";
    public static final String COLUMN_AUSER = "user_id";

    //Location
    public static final String TABLE_LOC = "location";
    public static final String COLUMN_LID = "id";
    public static final String COLUMN_LTYPE = "type";
    public static final String COLUMN_LTITLE = "title";
    public static final String COLUMN_LLAT = "lat";
    public static final String COLUMN_LLNG = "lng";


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE ="CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_UID + " integer primary key autoincrement,"
                + COLUMN_UEMAIL + " TEXT unique,"
                + COLUMN_UNAME + " TEXT,"
                + COLUMN_UPASSWORD + " TEXT,"
                + COLUMN_UDATE + " DATE,"
                + COLUMN_UGENDER + " BOOLEAN," // 1 for F, 0 for M
                + COLUMN_UHEIGHT + " integer)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_MEDICATION_TABLE = "CREATE TABLE " +
                TABLE_MEDICATION + "("
                + COLUMN_MID + " integer primary key autoincrement,"
                + COLUMN_MNAME + " TEXT,"
                + COLUMN_MQUANTITY + " INTEGER,"
                + COLUMN_MUSER + " INTEGER,"
                + " FOREIGN KEY ("+COLUMN_MUSER+") REFERENCES "+ TABLE_USER +"("+ COLUMN_UID +")"+"ON DELETE CASCADE);";
        db.execSQL(CREATE_MEDICATION_TABLE);

        String CREATE_RECORD_TABLE = "CREATE TABLE " +
                TABLE_RECORD + "("
                + COLUMN_RID + " integer primary key autoincrement,"
                + COLUMN_RTYPE + " TEXT,"
                + COLUMN_RVALUE + " INTEGER,"
                + COLUMN_RDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_RUSER + " INTEGER,"
                + " FOREIGN KEY ("+COLUMN_RUSER+") REFERENCES "+TABLE_USER+"("+COLUMN_UID+")"+"ON DELETE CASCADE);";
        db.execSQL(CREATE_RECORD_TABLE);

        String CREATE_APP_TABLE = "CREATE TABLE " +
                TABLE_APP + "("
                + COLUMN_AID + " integer primary key autoincrement,"
                + COLUMN_ATITLE + " TEXT,"
                + COLUMN_ADESCRIPTION + " TEXT,"
                + COLUMN_ADATE + " DATE,"
                + COLUMN_AUSER + " INTEGER,"
                + " FOREIGN KEY ("+COLUMN_MUSER+") REFERENCES "+TABLE_USER+"("+COLUMN_UID+")"+"ON DELETE CASCADE);";
        db.execSQL(CREATE_APP_TABLE);

        String CREATE_LOC_TABLE = "CREATE TABLE " +
                TABLE_LOC + "("
                + COLUMN_LID + " integer primary key autoincrement,"
                + COLUMN_LTYPE + " TEXT,"
                + COLUMN_LTITLE + " TEXT,"
                + COLUMN_LLAT + " DOUBLE,"
                + COLUMN_LLNG + " DOUBLE)";
        db.execSQL(CREATE_LOC_TABLE);

    }

    @Override //enable the foreign keys
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MEDICATION + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_APP + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_LOC + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_RECORD + "'");

        onCreate(db);
    }

    // User handling
    public boolean insert(String email, String password){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UEMAIL, email);
        contentValues.put(COLUMN_UPASSWORD, password);
        long ins= db.insert(TABLE_USER, null, contentValues);
        if(ins==-1) return false;
        else return true;
    }
    public boolean chkemail(String email){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UEMAIL+"=?", new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }
    public boolean chkpass(String pass, int user){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UID+"=?", new String[]{String.valueOf(user)});
        cursor.moveToLast();
        String true_pass=cursor.getString(cursor.getColumnIndex(COLUMN_UPASSWORD));
        if (true_pass.equals(pass)) return true;
        else return false;
    }
    public boolean emailpassword(String email, String password){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+ COLUMN_UEMAIL +"=? and "+COLUMN_UPASSWORD+"=?", new String[]{email,password});
        if (cursor.getCount()>0) return true;
        else return false;

    }
    public int getUserId(String email){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UEMAIL+"=?", new String[]{email});
        cursor.moveToLast();
        return cursor.getInt(cursor.getColumnIndex(COLUMN_UID));
    }
    public int getHeight(int user){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UID+"=?", new String[]{String.valueOf(user)});
        cursor.moveToLast();
        return cursor.getInt(cursor.getColumnIndex(COLUMN_UHEIGHT));
    }
    public void CompleteRegistration(int id, String name, String gender,int height, String birthdate ){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UNAME, name);
        boolean real_gender = gender.equals("F");
        contentValues.put(COLUMN_UGENDER, real_gender);
        contentValues.put(COLUMN_UHEIGHT, height);
        contentValues.put(COLUMN_UDATE, birthdate);

        db.update(TABLE_USER,  contentValues, COLUMN_UID+"=?", new String[]{String.valueOf(id)});

    }
    public void changepass(String pass, int user){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UPASSWORD, pass);

        db.update(TABLE_USER,  contentValues, COLUMN_UID+"=?", new String[]{String.valueOf(user)});

    }
    public ArrayList<String> getPInformation(int user){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UID+"=?", new String[]{String.valueOf(user)});
        cursor.moveToLast();
        ArrayList array_list = new ArrayList();
        array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_UEMAIL)));
        array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_UGENDER)));
        array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_UHEIGHT)));
        array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_UDATE)));

        return array_list;
    }
    public boolean chkotheremail(String email,int user){
        SQLiteDatabase db=this.getReadableDatabase();
        String comp_email="DEACTIVATE "+email;
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UEMAIL+"=? AND "+COLUMN_UID+ "!=?", new String[]{email,String.valueOf(user)});
        Cursor cursor2 =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UEMAIL+"=?", new String[]{comp_email});
        if (cursor.getCount()>0 || cursor2.getCount()>0) return false;
        else return true;
    }
    public void ChangePersonal(int user, String email, String gender, int height,String birthdate ){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UEMAIL, email);
        boolean real_gender = gender.equals("F");
        contentValues.put(COLUMN_UGENDER, real_gender);
        contentValues.put(COLUMN_UHEIGHT, height);
        contentValues.put(COLUMN_UDATE, birthdate);

        db.update(TABLE_USER,  contentValues, COLUMN_UID+"=?", new String[]{String.valueOf(user)});

    }
    public boolean useremailpassword(int user,String email, String password){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+ COLUMN_UEMAIL +"=? and "+COLUMN_UPASSWORD+"=? and "+COLUMN_UID+"=?", new String[]{email,password, String.valueOf(user)});
        if (cursor.getCount()>0) return true;
        else return false;

    }
    public void DeleteUser(int user, String email, boolean save){

        SQLiteDatabase db = this.getWritableDatabase();
        if (save) {
            db.delete(TABLE_USER, COLUMN_UID + "=? ", new String[]{String.valueOf(user)});
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_UEMAIL, "DEACTIVATE "+email);
            db.update(TABLE_USER, contentValues, COLUMN_UID + "=?", new String[]{String.valueOf(user)});
        }
    }
    public boolean dectivateAccount(String email){
        SQLiteDatabase db=this.getReadableDatabase();
        String dea_email;
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER, null);
        if (cursor.moveToFirst()) {
            do {
                dea_email=cursor.getString(cursor.getColumnIndex(COLUMN_UEMAIL));
                if (dea_email.equals("DEACTIVATE "+email))
                    return true;
            } while (cursor.moveToNext());
        }
        return false;
    }
    public boolean chkDeativatePass(String email,String pass){
        SQLiteDatabase db=this.getReadableDatabase();
        String dea_email;
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+ " where "+ COLUMN_UPASSWORD+"=?", new String[]{pass});
        if (cursor.moveToFirst()) {
            do {
                dea_email=cursor.getString(cursor.getColumnIndex(COLUMN_UEMAIL));
                if (dea_email.equals("DEACTIVATE "+email))
                    return true;
            } while (cursor.moveToNext());
        }
        return false;
    }
    public void activateAccount(String email){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UEMAIL, email);

        db.update(TABLE_USER,  contentValues, COLUMN_UEMAIL+"=?", new String[]{"DEACTIVATE "+email});

    }

    // Medication handling
    public boolean chkmed(String med, int user){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_MEDICATION+" where "+COLUMN_MNAME+"=? and " +COLUMN_MUSER+"=?", new String[]{med,String.valueOf(user)});
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

    }
    public void deleteMedication(String name, int user){

        SQLiteDatabase db = this.getWritableDatabase();

        // delete raw
        db.delete(TABLE_MEDICATION, COLUMN_MNAME + "=? and " + COLUMN_MUSER + "=?", new String[]{name,String.valueOf(user)});
    }
    public void updateMedication(Medication medication){
        SQLiteDatabase db=this.getWritableDatabase();

        String name=medication.getName();
        int user=medication.getUser();

        Cursor cursor =db.rawQuery("Select * from "+TABLE_MEDICATION+" where "+COLUMN_MUSER+ "=? and " +COLUMN_MNAME+ "=?", new String[]{String.valueOf(user),name});
        cursor.moveToLast();
        int last_quantity= cursor.getInt(cursor.getColumnIndex(COLUMN_MQUANTITY));
        int new_quantity= last_quantity+medication.getQuantity();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MQUANTITY, new_quantity);

        db.update(TABLE_MEDICATION,  contentValues, COLUMN_MNAME + "=? and " + COLUMN_MUSER + "=?", new String[]{name,String.valueOf(user)});

    }
    public boolean deleteQuantityMedication(Medication medication) {

        SQLiteDatabase db = this.getWritableDatabase();

        String name=medication.getName();
        int user=medication.getUser();

        Cursor cursor =db.rawQuery("Select * from "+TABLE_MEDICATION+" where "+COLUMN_MUSER+ "=? and " +COLUMN_MNAME+ "=?", new String[]{String.valueOf(user),name});
        cursor.moveToLast();
        int last_quantity= cursor.getInt(cursor.getColumnIndex(COLUMN_MQUANTITY));
        if (last_quantity<=medication.getQuantity()){
            return false;
        }
        else{
            int new_quantity=last_quantity-medication.getQuantity();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MQUANTITY, new_quantity);
            db.update(TABLE_MEDICATION,  contentValues, COLUMN_MNAME + "=? and " + COLUMN_MUSER + "=?", new String[]{name,String.valueOf(user)});
            return true;

        }
    }

    //Record handling
    public void addRecord(Record record) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RVALUE, record.getValue());
        values.put(COLUMN_RTYPE, record.getType());
        values.put(COLUMN_RUSER, record.getUser());

        // insert row
        db.insert(TABLE_RECORD, null, values);

    }
    public void deleteRecord(String type, int user){

        SQLiteDatabase db = this.getWritableDatabase();

        // delete raw
        db.delete(TABLE_RECORD, COLUMN_RTYPE + "=? and " + COLUMN_RUSER + "=?", new String[]{type,String.valueOf(user)});
    }


    //Appointment Handling
    public void addApp(App app){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ATITLE, app.getTitle());
        values.put(COLUMN_ADESCRIPTION, app.getDescription());
        values.put(COLUMN_ADATE, app.getDate());
        values.put(COLUMN_AUSER, app.getUser());


        // insert row
        db.insert(TABLE_APP, null, values);


    }

}
