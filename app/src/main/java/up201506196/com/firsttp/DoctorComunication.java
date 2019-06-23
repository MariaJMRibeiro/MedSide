package up201506196.com.firsttp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

public class DoctorComunication extends AppCompatActivity {

    Button send;
    EditText e1;
    Uri uri = null;
    String name;
    String nametxt;
    String doctor_email;
    int user;
    SQLiteDatabase sqLiteDatabase;
    DatabaseHelper db;
    protected String mDir = Environment.DIRECTORY_DOCUMENTS;
    protected File mPath = Environment.getExternalStoragePublicDirectory(mDir);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_comunication);
        db = new DatabaseHelper(this);
        sqLiteDatabase = db.getReadableDatabase();


        user=getIntent().getIntExtra("key_email",0);
        name=db.getName(user);
        nametxt = name.replace(" ", "");
        StorageFile(name);




        e1 = (EditText)findViewById(R.id.Doctor_Email);
        send = (Button)findViewById(R.id.bsend_email);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctor_email=e1.getText().toString();
                if (isValidEmail(doctor_email))
                    sendEmail(doctor_email,name);
                else
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendEmail(String doctor_email, String name) {
        try
        {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[]{doctor_email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Medical Records");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From " +name+ ", via MedSide. \n Best Regards");

            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File file = new File(Environment.getExternalStorageDirectory(),"records");
            File newFile = new File(file, "Records_"+nametxt+".txt");
            uri = (Uri) FileProvider.getUriForFile(this, "com.up201506196.fileprovider", newFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));

        }
        catch (Throwable t)
        {
            Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void StorageFile(String name){
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {


        } else {
            File externalDir = Environment.getExternalStorageDirectory();
            File dir = new File(externalDir.getAbsolutePath() + "/records");
            dir.mkdir();
            if(!dir.exists()) {
                Log.i(null, "Does not exist");
            }
            File textFile = new File(dir, "Records_"+nametxt+".txt");
            try {
                writeTextFile(textFile);
            } catch (IOException ex){
                Toast.makeText(getApplicationContext(), "Something went wrong!" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    String[] type = new String[]{
            "cholesterol",
            "high_blood_pressure",
            "low_blood_pressure",
            "heart_beat",
            "weight",
    };

    String[] record = new String[]{
            "Cholesterol-LDL (mg/dl)",
            "Systolic Blood Pressure (mmHg)",
            "Diastolic Blood Pressure (mmHg)",
            "Heart Rate (bpm)",
            "Weight (kg)",
    };
    int value;
    String date;

    private void writeTextFile(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("Patient Name: "+name+"\n\n");

        for (int i=0;i<record.length;i++) {
            writer.write("\nLast Records of "+ record[i]+":\n");
            Cursor cursor;
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + db.TABLE_RECORD + " WHERE " + db.COLUMN_MUSER + " = ? " + " AND " + db.COLUMN_RTYPE + "=?", new String[]{String.valueOf(user), type[i]});
            if (cursor.moveToFirst()) {
                do {
                    value = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.COLUMN_RVALUE)));
                    date = cursor.getString(cursor.getColumnIndex(db.COLUMN_RDATE));
                    writer.write(" "+value+" on "+ date+"\n");
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        writer.close();
    }


}
