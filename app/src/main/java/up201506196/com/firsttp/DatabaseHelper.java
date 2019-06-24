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

        PopulateHLocations(db);
        PopulatePhLocations(db);


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
    public String getName(int user){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where "+COLUMN_UID+"=?", new String[]{String.valueOf(user)});
        cursor.moveToLast();
        return cursor.getString(cursor.getColumnIndex(COLUMN_UNAME));
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
        if (last_quantity<medication.getQuantity()){
            return false;
        }else if(last_quantity==medication.getQuantity()){
            deleteMedication(medication.getName(),medication.getUser());
            return true;
        }else{
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

    //Locations Handling
    public void PopulateHLocations(SQLiteDatabase db){
        ContentValues h1 = new ContentValues();
        h1.put(COLUMN_LTYPE, "hospital");
        h1.put(COLUMN_LTITLE, "Hospital São João");
        h1.put(COLUMN_LLAT, 41.180837);
        h1.put(COLUMN_LLNG, -8.600454);
        db.insert(TABLE_LOC, null, h1);

        ContentValues h2 = new ContentValues();
        h2.put(COLUMN_LTYPE, "hospital");
        h2.put(COLUMN_LTITLE, "Hospital de Santo António");
        h2.put(COLUMN_LLAT, 41.147037);
        h2.put(COLUMN_LLNG, -8.619068);
        db.insert(TABLE_LOC, null, h2);

        ContentValues h3 = new ContentValues();
        h3.put(COLUMN_LTYPE, "hospital");
        h3.put(COLUMN_LTITLE, "Hospital de Braga");
        h3.put(COLUMN_LLAT, 41.568283);
        h3.put(COLUMN_LLNG, -8.399024);
        db.insert(TABLE_LOC, null, h3);

        ContentValues h4 = new ContentValues();
        h4.put(COLUMN_LTYPE, "hospital");
        h4.put(COLUMN_LTITLE, "Hospital Distrital de Bragança");
        h4.put(COLUMN_LLAT, 41.802821);
        h4.put(COLUMN_LLNG, -6.767725);
        db.insert(TABLE_LOC, null, h4);

        ContentValues h5 = new ContentValues();
        h5.put(COLUMN_LTYPE, "hospital");
        h5.put(COLUMN_LTITLE, "Hospital de Santa Luzia de Viana do Castelo");
        h5.put(COLUMN_LLAT, 41.696899);
        h5.put(COLUMN_LLNG, -8.832789);
        db.insert(TABLE_LOC, null, h5);

        ContentValues h6 = new ContentValues();
        h6.put(COLUMN_LTYPE, "hospital");
        h6.put(COLUMN_LTITLE, "Hospital São Pedro de Vila Real");
        h6.put(COLUMN_LLAT, 41.309902);
        h6.put(COLUMN_LLNG, -7.760265);
        db.insert(TABLE_LOC, null, h6);

        ContentValues h7 = new ContentValues();
        h7.put(COLUMN_LTYPE, "hospital");
        h7.put(COLUMN_LTITLE, "Hospital José Joaquim Fernandes");
        h7.put(COLUMN_LLAT, 38.013880);
        h7.put(COLUMN_LLNG, -7.869744);
        db.insert(TABLE_LOC, null, h7);

        ContentValues h8 = new ContentValues();
        h8.put(COLUMN_LTYPE, "hospital");
        h8.put(COLUMN_LTITLE, "Hospital Espírito Santo");
        h8.put(COLUMN_LLAT, 38.567947);
        h8.put(COLUMN_LLNG, -7.902445);
        db.insert(TABLE_LOC, null, h8);

        ContentValues h9 = new ContentValues();
        h9.put(COLUMN_LTYPE, "hospital");
        h9.put(COLUMN_LTITLE, "Hospital Doutor José Maria Grande");
        h9.put(COLUMN_LLAT, 39.300199);
        h9.put(COLUMN_LLNG, -7.428263);
        db.insert(TABLE_LOC, null, h9);

        ContentValues h10 = new ContentValues();
        h10.put(COLUMN_LTYPE, "hospital");
        h10.put(COLUMN_LTITLE, "Unidade Hospitalar de Faro");
        h10.put(COLUMN_LLAT, 37.024586);
        h10.put(COLUMN_LLNG, -7.929144);
        db.insert(TABLE_LOC, null, h10);

        ContentValues h11 = new ContentValues();
        h11.put(COLUMN_LTYPE, "hospital");
        h11.put(COLUMN_LTITLE, "Hospital Infante D.Pedro");
        h11.put(COLUMN_LLAT, 40.634366);
        h11.put(COLUMN_LLNG, -8.654766);
        db.insert(TABLE_LOC, null, h11);

        ContentValues h12 = new ContentValues();
        h12.put(COLUMN_LTYPE, "hospital");
        h12.put(COLUMN_LTITLE, "Hospital Amato Lusitano");
        h12.put(COLUMN_LLAT, 39.822272);
        h12.put(COLUMN_LLNG, -7.499922);
        db.insert(TABLE_LOC, null, h12);

        ContentValues h13 = new ContentValues();
        h13.put(COLUMN_LTYPE, "hospital");
        h13.put(COLUMN_LTITLE, "Hospitais da Universidade de Coimbra");
        h13.put(COLUMN_LLAT, 40.220444);
        h13.put(COLUMN_LLNG, -8.413084);
        db.insert(TABLE_LOC, null, h13);

        ContentValues h14 = new ContentValues();
        h14.put(COLUMN_LTYPE, "hospital");
        h14.put(COLUMN_LTITLE, "Hospital Geral de Coimbra");
        h14.put(COLUMN_LLAT, 40.195606);
        h14.put(COLUMN_LLNG, -8.459574);
        db.insert(TABLE_LOC, null, h14);

        ContentValues h15 = new ContentValues();
        h15.put(COLUMN_LTYPE, "hospital");
        h15.put(COLUMN_LTITLE, "Hospital Sousa Martins");
        h15.put(COLUMN_LLAT, 40.530580);
        h15.put(COLUMN_LLNG, -7.275576);
        db.insert(TABLE_LOC, null, h15);

        ContentValues h16 = new ContentValues();
        h16.put(COLUMN_LTYPE, "hospital");
        h16.put(COLUMN_LTITLE, "Hospital de Santo André");
        h16.put(COLUMN_LLAT, 39.743558);
        h16.put(COLUMN_LLNG, -8.795120);
        db.insert(TABLE_LOC, null, h16);

        ContentValues h17 = new ContentValues();
        h17.put(COLUMN_LTYPE, "hospital");
        h17.put(COLUMN_LTITLE, "Hospital São Teotónio");
        h17.put(COLUMN_LLAT, 40.650198);
        h17.put(COLUMN_LLNG, -7.906264);
        db.insert(TABLE_LOC, null, h17);

        ContentValues h18 = new ContentValues();
        h18.put(COLUMN_LTYPE, "hospital");
        h18.put(COLUMN_LTITLE, "Hospital Garcia de Horta");
        h18.put(COLUMN_LLAT, 38.673881);
        h18.put(COLUMN_LLNG, -9.176851);
        db.insert(TABLE_LOC, null, h18);

        ContentValues h19 = new ContentValues();
        h19.put(COLUMN_LTYPE, "hospital");
        h19.put(COLUMN_LTITLE, "Hospital São Bernardo");
        h19.put(COLUMN_LLAT, 38.529072);
        h19.put(COLUMN_LLNG, -8.880155);
        db.insert(TABLE_LOC, null, h19);

        ContentValues h20 = new ContentValues();
        h20.put(COLUMN_LTYPE, "hospital");
        h20.put(COLUMN_LTITLE, "Hospital Distrital de Santarém");
        h20.put(COLUMN_LLAT, 39.241201);
        h20.put(COLUMN_LLNG, -8.697036);
        db.insert(TABLE_LOC, null, h20);

        ContentValues h21 = new ContentValues();
        h21.put(COLUMN_LTYPE, "hospital");
        h21.put(COLUMN_LTITLE, "Hospital Santa Maria");
        h21.put(COLUMN_LLAT, 38.747988);
        h21.put(COLUMN_LLNG, -9.160658);
        db.insert(TABLE_LOC, null, h21);

        ContentValues h22 = new ContentValues();
        h22.put(COLUMN_LTYPE, "hospital");
        h22.put(COLUMN_LTITLE, "Hospital São Francisco Xavier");
        h22.put(COLUMN_LLAT, 38.707338);
        h22.put(COLUMN_LLNG, -9.217824);
        db.insert(TABLE_LOC, null, h22);

        ContentValues h23 = new ContentValues();
        h23.put(COLUMN_LTYPE, "hospital");
        h23.put(COLUMN_LTITLE, "Hospital Dona Estefânia");
        h23.put(COLUMN_LLAT, 38.728709);
        h23.put(COLUMN_LLNG, -9.139173);
        db.insert(TABLE_LOC, null, h23);

        ContentValues h24 = new ContentValues();
        h24.put(COLUMN_LTYPE, "hospital");
        h24.put(COLUMN_LTITLE, "Hospital do Divino Espírito Santo");
        h24.put(COLUMN_LLAT, 37.751661);
        h24.put(COLUMN_LLNG, -25.676221);
        db.insert(TABLE_LOC, null, h24);

        ContentValues h25 = new ContentValues();
        h25.put(COLUMN_LTYPE, "hospital");
        h25.put(COLUMN_LTITLE, "Hospital Dr. Nélio Mendonça");
        h25.put(COLUMN_LLAT, 32.648299);
        h25.put(COLUMN_LLNG, -16.924070);
        db.insert(TABLE_LOC, null, h25);
    }

    public void PopulatePhLocations(SQLiteDatabase db){
        ContentValues f1 = new ContentValues();
        f1.put(COLUMN_LTYPE, "pharmacy");
        f1.put(COLUMN_LTITLE, "Farmácia Pimentel");
        f1.put(COLUMN_LLAT, 41.557467);
        f1.put(COLUMN_LLNG, -8.408616);
        db.insert(TABLE_LOC, null, f1);

        ContentValues f2 = new ContentValues();
        f2.put(COLUMN_LTYPE, "pharmacy");
        f2.put(COLUMN_LTITLE, "Farmácia São João");
        f2.put(COLUMN_LLAT, 41.544978);
        f2.put(COLUMN_LLNG, -8.419484);
        db.insert(TABLE_LOC, null, f2);

        ContentValues f3 = new ContentValues();
        f3.put(COLUMN_LTYPE, "pharmacy");
        f3.put(COLUMN_LTITLE, "Farmácia Mariano");
        f3.put(COLUMN_LLAT, 41.806193);
        f3.put(COLUMN_LLNG, -6.756574);
        db.insert(TABLE_LOC, null, f3);

        ContentValues f4 = new ContentValues();
        f4.put(COLUMN_LTYPE, "pharmacy");
        f4.put(COLUMN_LTITLE, "Farmácia Central");
        f4.put(COLUMN_LLAT, 41.817940);
        f4.put(COLUMN_LLNG, -6.754964);
        db.insert(TABLE_LOC, null, f4);

        ContentValues f5 = new ContentValues();
        f5.put(COLUMN_LTYPE, "pharmacy");
        f5.put(COLUMN_LTITLE, "Farmácia Central do Porto");
        f5.put(COLUMN_LLAT, 41.157170);
        f5.put(COLUMN_LLNG, -8.642913);
        db.insert(TABLE_LOC, null, f5);

        ContentValues f6 = new ContentValues();
        f6.put(COLUMN_LTYPE, "pharmacy");
        f6.put(COLUMN_LTITLE, "Farmácia Boavista");
        f6.put(COLUMN_LLAT, 41.156382);
        f6.put(COLUMN_LLNG, -8.620384);
        db.insert(TABLE_LOC, null, f6);

        ContentValues f7 = new ContentValues();
        f7.put(COLUMN_LTYPE, "pharmacy");
        f7.put(COLUMN_LTITLE, "Farmácia São João");
        f7.put(COLUMN_LLAT, 41.183773);
        f7.put(COLUMN_LLNG, -8.600791);
        db.insert(TABLE_LOC, null, f7);

        ContentValues f8 = new ContentValues();
        f8.put(COLUMN_LTYPE, "pharmacy");
        f8.put(COLUMN_LTITLE, "Farmácia Costa Cabral");
        f8.put(COLUMN_LLAT, 41.172076);
        f8.put(COLUMN_LLNG, -8.587654);
        db.insert(TABLE_LOC, null, f8);

        ContentValues h9 = new ContentValues();
        h9.put(COLUMN_LTYPE, "pharmacy");
        h9.put(COLUMN_LTITLE, "Farmácia S.Vicente");
        h9.put(COLUMN_LLAT, 41.703989);
        h9.put(COLUMN_LLNG, -8.816906);
        db.insert(TABLE_LOC, null, h9);

        ContentValues h10 = new ContentValues();
        h10.put(COLUMN_LTYPE, "pharmacy");
        h10.put(COLUMN_LTITLE, "Farmácia S.Domingos");
        h10.put(COLUMN_LLAT, 41.691565);
        h10.put(COLUMN_LLNG, -8.833537);
        db.insert(TABLE_LOC, null, h10);

        ContentValues h11 = new ContentValues();
        h11.put(COLUMN_LTYPE, "pharmacy");
        h11.put(COLUMN_LTITLE, "Farmácia de Montezelos");
        h11.put(COLUMN_LLAT, 41.308321);
        h11.put(COLUMN_LLNG, -7.744253);
        db.insert(TABLE_LOC, null, h11);

        ContentValues h12 = new ContentValues();
        h12.put(COLUMN_LTYPE, "pharmacy");
        h12.put(COLUMN_LTITLE, "Farmácia Baptista Lda.");
        h12.put(COLUMN_LLAT, 41.297504);
        h12.put(COLUMN_LLNG, -7.744270);
        db.insert(TABLE_LOC, null, h12);

        ContentValues h13 = new ContentValues();
        h13.put(COLUMN_LTYPE, "pharmacy");
        h13.put(COLUMN_LTITLE, "Farmácia Moderna");
        h13.put(COLUMN_LLAT, 40.638966);
        h13.put(COLUMN_LLNG, -8.652484);
        db.insert(TABLE_LOC, null, h13);

        ContentValues h14 = new ContentValues();
        h14.put(COLUMN_LTYPE, "pharmacy");
        h14.put(COLUMN_LTITLE, "Farmácia Nova");
        h14.put(COLUMN_LLAT, 40.629225);
        h14.put(COLUMN_LLNG, -8.647812);
        db.insert(TABLE_LOC, null, h14);

        ContentValues h15 = new ContentValues();
        h15.put(COLUMN_LTYPE, "pharmacy");
        h15.put(COLUMN_LTITLE, "Farmácia Progresso");
        h15.put(COLUMN_LLAT, 39.807707);
        h15.put(COLUMN_LLNG, -7.514121);
        db.insert(TABLE_LOC, null, h15);

        ContentValues h16 = new ContentValues();
        h16.put(COLUMN_LTYPE, "pharmacy");
        h16.put(COLUMN_LTITLE, "Farmácia Rodrigues dos Santos");
        h16.put(COLUMN_LLAT, 39.818655);
        h16.put(COLUMN_LLNG, -7.496509);
        db.insert(TABLE_LOC, null, h16);

        ContentValues h17 = new ContentValues();
        h17.put(COLUMN_LTYPE, "pharmacy");
        h17.put(COLUMN_LTITLE, "Farmácia Coimbra");
        h17.put(COLUMN_LLAT, 40.194310);
        h17.put(COLUMN_LLNG, -8.408461);
        db.insert(TABLE_LOC, null, h17);

        ContentValues h18 = new ContentValues();
        h18.put(COLUMN_LTYPE, "pharmacy");
        h18.put(COLUMN_LTITLE, "Farmácia Baptista");
        h18.put(COLUMN_LLAT, 40.210011);
        h18.put(COLUMN_LLNG, -8.419929);
        db.insert(TABLE_LOC, null, h18);

        ContentValues h19 = new ContentValues();
        h19.put(COLUMN_LTYPE, "pharmacy");
        h19.put(COLUMN_LTITLE, "Farmácia dos Olivais");
        h19.put(COLUMN_LLAT, 40.216512);
        h19.put(COLUMN_LLNG, -8.408074);
        db.insert(TABLE_LOC, null, h19);

        ContentValues h20 = new ContentValues();
        h20.put(COLUMN_LTYPE, "pharmacy");
        h20.put(COLUMN_LTITLE, "Farmácia Rainha Santa");
        h20.put(COLUMN_LLAT, 40.213976);
        h20.put(COLUMN_LLNG, -8.434440);
        db.insert(TABLE_LOC, null, h20);

        ContentValues h21 = new ContentValues();
        h21.put(COLUMN_LTYPE, "pharmacy");
        h21.put(COLUMN_LTITLE, "Farmácia Moderna Guarda");
        h21.put(COLUMN_LLAT, 40.555062);
        h21.put(COLUMN_LLNG, -7.245718);
        db.insert(TABLE_LOC, null, h21);

        ContentValues h22 = new ContentValues();
        h22.put(COLUMN_LTYPE, "pharmacy");
        h22.put(COLUMN_LTITLE, "Farmácia Central");
        h22.put(COLUMN_LLAT, 40.538219);
        h22.put(COLUMN_LLNG, -7.267802);
        db.insert(TABLE_LOC, null, h22);

        ContentValues h23 = new ContentValues();
        h23.put(COLUMN_LTYPE, "pharmacy");
        h23.put(COLUMN_LTITLE, "Farmácia Sanches");
        h23.put(COLUMN_LLAT, 39.739727);
        h23.put(COLUMN_LLNG, -8.808222);
        db.insert(TABLE_LOC, null, h23);

        ContentValues h24 = new ContentValues();
        h24.put(COLUMN_LTYPE, "pharmacy");
        h24.put(COLUMN_LTITLE, "Farmáicia Lis");
        h24.put(COLUMN_LLAT, 39.762225);
        h24.put(COLUMN_LLNG, -8.822028);
        db.insert(TABLE_LOC, null, h24);

        ContentValues h25 = new ContentValues();
        h25.put(COLUMN_LTYPE, "pharmacy");
        h25.put(COLUMN_LTITLE, "Farmácia Portugal de Viseu");
        h25.put(COLUMN_LLAT, 40.657726);
        h25.put(COLUMN_LLNG, -7.916081);
        db.insert(TABLE_LOC, null, h25);

        ContentValues h26 = new ContentValues();
        h26.put(COLUMN_LTYPE, "pharmacy");
        h26.put(COLUMN_LTITLE, "Farmácia Viriato");
        h26.put(COLUMN_LLAT, 40.671699);
        h26.put(COLUMN_LLNG, -7.919858);
        db.insert(TABLE_LOC, null, h26);

        ContentValues f27 = new ContentValues();
        f27.put(COLUMN_LTYPE, "pharmacy");
        f27.put(COLUMN_LTITLE, "Farmácia Caminé");
        f27.put(COLUMN_LLAT, 37.0160448);
        f27.put(COLUMN_LLNG,  -7.934099);
        db.insert(TABLE_LOC, null, f27);

        ContentValues f28 = new ContentValues();
        f28.put(COLUMN_LTYPE, "pharmacy");
        f28.put(COLUMN_LTITLE, "Farmácia Amoreira");
        f28.put(COLUMN_LLAT,  37.027699);
        f28.put(COLUMN_LLNG,  -7.927460);
        db.insert(TABLE_LOC, null, f28);

        ContentValues f29 = new ContentValues();
        f29.put(COLUMN_LTYPE, "pharmacy");
        f29.put(COLUMN_LTITLE, "Farmácia Holon Beja");
        f29.put(COLUMN_LLAT, 38.013133);
        f29.put(COLUMN_LLNG, -7.864971);
        db.insert(TABLE_LOC, null, f29);

        ContentValues f30 = new ContentValues();
        f30.put(COLUMN_LTYPE, "pharmacy");
        f30.put(COLUMN_LTITLE, "Farmácia Oliveira");
        f30.put(COLUMN_LLAT, 38.006738);
        f30.put(COLUMN_LLNG, -7.869239);
        db.insert(TABLE_LOC, null, f30);

        ContentValues f31 = new ContentValues();
        f31.put(COLUMN_LTYPE, "pharmacy");
        f31.put(COLUMN_LTITLE, "Farmácia Branco");
        f31.put(COLUMN_LLAT, 38.565967);
        f31.put(COLUMN_LLNG, -7.902771);
        db.insert(TABLE_LOC, null, f31);

        ContentValues f32 = new ContentValues();
        f32.put(COLUMN_LTYPE, "pharmacy");
        f32.put(COLUMN_LTITLE, "Farmácia Paços");
        f32.put(COLUMN_LLAT, 38.575048);
        f32.put(COLUMN_LLNG, -7.915727);
        db.insert(TABLE_LOC, null, f32);

        ContentValues f33 = new ContentValues();
        f33.put(COLUMN_LTYPE, "pharmacy");
        f33.put(COLUMN_LTITLE, "Farmácia Elvas");
        f33.put(COLUMN_LLAT, 39.297549);
        f33.put(COLUMN_LLNG, -7.428534);
        db.insert(TABLE_LOC, null, f33);

        ContentValues h34 = new ContentValues();
        h34.put(COLUMN_LTYPE, "pharmacy");
        h34.put(COLUMN_LTITLE, "Farmácia Cunha Miranda");
        h34.put(COLUMN_LLAT, 39.280727);
        h34.put(COLUMN_LLNG, -7.430125);
        db.insert(TABLE_LOC, null, h34);

        ContentValues h35 = new ContentValues();
        h35.put(COLUMN_LTYPE, "pharmacy");
        h35.put(COLUMN_LTITLE, "Farmácia Batista");
        h35.put(COLUMN_LLAT, 41.691565);
        h35.put(COLUMN_LLNG, -8.683902);
        db.insert(TABLE_LOC, null, h35);

        ContentValues h36 = new ContentValues();
        h36.put(COLUMN_LTYPE, "pharmacy");
        h36.put(COLUMN_LTITLE, "Farmácia Confiança");
        h36.put(COLUMN_LLAT, 39.238065);
        h36.put(COLUMN_LLNG, -8.697549);
        db.insert(TABLE_LOC, null, h36);

        ContentValues h37 = new ContentValues();
        h37.put(COLUMN_LTYPE, "pharmacy");
        h37.put(COLUMN_LTITLE, "Farmácia Sado");
        h37.put(COLUMN_LLAT, 38.521838);
        h37.put(COLUMN_LLNG, -8.821236);
        db.insert(TABLE_LOC, null, h37);

        ContentValues h38 = new ContentValues();
        h38.put(COLUMN_LTYPE, "pharmacy");
        h38.put(COLUMN_LTITLE, "Farmácia Sália");
        h38.put(COLUMN_LLAT, 38.524732);
        h38.put(COLUMN_LLNG, -8.892689);
        db.insert(TABLE_LOC, null, h38);

        ContentValues h39 = new ContentValues();
        h39.put(COLUMN_LTYPE, "pharmacy");
        h39.put(COLUMN_LTITLE, "Farmácia Santiago");
        h39.put(COLUMN_LLAT, 38.533458);
        h39.put(COLUMN_LLNG, -8.897454);
        db.insert(TABLE_LOC, null, h39);

        ContentValues h40 = new ContentValues();
        h40.put(COLUMN_LTYPE, "pharmacy");
        h40.put(COLUMN_LTITLE, "Farmácia Brasil");
        h40.put(COLUMN_LLAT, 38.531066);
        h40.put(COLUMN_LLNG, -8.886639);
        db.insert(TABLE_LOC, null, h40);

        ContentValues h41 = new ContentValues();
        h41.put(COLUMN_LTYPE, "pharmacy");
        h41.put(COLUMN_LTITLE, "Farmácia Lisboa");
        h41.put(COLUMN_LLAT, 38.763435);
        h41.put(COLUMN_LLNG, -9.177550);
        db.insert(TABLE_LOC, null, h41);

        ContentValues h42 = new ContentValues();
        h42.put(COLUMN_LTYPE, "pharmacy");
        h42.put(COLUMN_LTITLE, "Farmácia Nova Iorque");
        h42.put(COLUMN_LLAT, 38.748673);
        h42.put(COLUMN_LLNG, -9.147355);
        db.insert(TABLE_LOC, null, h42);

        ContentValues h43 = new ContentValues();
        h43.put(COLUMN_LTYPE, "pharmacy");
        h43.put(COLUMN_LTITLE, "Farmácia Oriental Lisboa");
        h43.put(COLUMN_LLAT, 38.775205);
        h43.put(COLUMN_LLNG, -9.096769);
        db.insert(TABLE_LOC, null, h43);

        ContentValues h44 = new ContentValues();
        h44.put(COLUMN_LTYPE, "pharmacy");
        h44.put(COLUMN_LTITLE, "Farmácia União");
        h44.put(COLUMN_LLAT, 38.750798);
        h44.put(COLUMN_LLNG, -9.199557);
        db.insert(TABLE_LOC, null, h44);

        ContentValues h45 = new ContentValues();
        h45.put(COLUMN_LTYPE, "pharmacy");
        h45.put(COLUMN_LTITLE, "Farmácia Grijó");
        h45.put(COLUMN_LLAT, 38.695156);
        h45.put(COLUMN_LLNG, -9.214460);
        db.insert(TABLE_LOC, null, h45);

        ContentValues h46 = new ContentValues();
        h46.put(COLUMN_LTYPE, "pharmacy");
        h46.put(COLUMN_LTITLE, "Farmácia Gouveia");
        h46.put(COLUMN_LLAT, 38.711566);
        h46.put(COLUMN_LLNG, -9.137689);
        db.insert(TABLE_LOC, null, h46);

        ContentValues h47 = new ContentValues();
        h47.put(COLUMN_LTYPE, "pharmacy");
        h47.put(COLUMN_LTITLE, "Farmácia Varela");
        h47.put(COLUMN_LLAT, 32.730184);
        h47.put(COLUMN_LLNG, -17.177497);
        db.insert(TABLE_LOC, null, h47);

        ContentValues h48 = new ContentValues();
        h48.put(COLUMN_LTYPE, "pharmacy");
        h48.put(COLUMN_LTITLE, "Farmácia Nacional");
        h48.put(COLUMN_LLAT, 32.649216);
        h48.put(COLUMN_LLNG, -16.907770);
        db.insert(TABLE_LOC, null, h48);

        ContentValues h49 = new ContentValues();
        h49.put(COLUMN_LTYPE, "pharmacy");
        h49.put(COLUMN_LTITLE, "Farmáicia Central");
        h49.put(COLUMN_LLAT, 37.740104);
        h49.put(COLUMN_LLNG, -25.670551);
        db.insert(TABLE_LOC, null, h49);

        ContentValues h50 = new ContentValues();
        h50.put(COLUMN_LTYPE, "pharmacy");
        h50.put(COLUMN_LTITLE, "Farmácia Moderna");
        h50.put(COLUMN_LLAT, 37.741034);
        h50.put(COLUMN_LLNG, -25.6648441);
        db.insert(TABLE_LOC, null, h50);


    }
}
