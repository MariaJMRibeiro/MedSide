package up201506196.com.firsttp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    EditText e1,e2,e3;
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        e1=(EditText) findViewById(R.id.email);
        e2=(EditText)findViewById(R.id.pass);
        e3=(EditText)findViewById(R.id.cpass);
        b1=(Button)findViewById(R.id.register);
        t1=(TextView) findViewById(R.id.button2);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                String s3 = e3.getText().toString();
                if (s1.equals("")||s2.equals("")||s3.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (s2.equals(s3)) {
                        boolean chkemail = db.chkemail(s1);
                        if (chkemail == true) {
                            Boolean insert = db.insert(s1, s2);
                            if (insert == true) {
                                Intent i= new Intent(MainActivity.this,CompleteRegister.class);

                                sqLiteDatabase = db.getReadableDatabase();
                                Cursor cursor = sqLiteDatabase.rawQuery("SELECT  * FROM "+db.TABLE_USER, null);
                                cursor.moveToLast();
                                int user =cursor.getInt(cursor.getColumnIndex(db.COLUMN_UID));
                                i.putExtra("key_email", user);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
