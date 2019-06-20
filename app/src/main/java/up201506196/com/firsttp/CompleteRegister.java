package up201506196.com.firsttp;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class CompleteRegister extends AppCompatActivity {
    DatabaseHelper db;
    Button b1;
    EditText e1,e2,e3,e4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_register);
        db = new DatabaseHelper(this);
        e1=findViewById(R.id.name);
        e2=findViewById(R.id.gender);
        e3=findViewById(R.id.height);
        e4=findViewById(R.id.birthdate);
        b1=findViewById(R.id.final_register_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                String name=e1.getText().toString();
                String gender=e2.getText().toString();
                String s3=e3.getText().toString();
                String s4=e4.getText().toString();

                if (name.equals("")||gender.equals("")||s3.equals("")||s4.equals(""))
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                else {
                    if (gender.equals("F")||gender.equals("M")){
                        int height=Integer.parseInt(s3);
                        if (height>120 && height<230) {
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Make sure user insert date into edittext in this format.
                            Date dateObject;

                            try{
                                dateObject = formatter.parse(s4);
                                String date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);
                                db.CompleteRegistration(user, name, gender, height, date);
                                Intent i= new Intent(CompleteRegister.this,InitialPage.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                            }
                            catch (java.text.ParseException e)
                            {
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), "Please field a valid Birthdate (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Height should be between 120 and 230 cm", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Invalid Gender (F or M)", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
