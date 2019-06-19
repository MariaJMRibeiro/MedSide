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
import java.util.Calendar;
import java.util.Date;

public class CompleteRegister extends AppCompatActivity {

    Button b1;
    EditText e1,e2,e3,e4;
    String name,gender,s3,s4;
    DatabaseHelper db;
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
                name=e1.getText().toString();
                gender=e2.getText().toString();
                s3=e3.getText().toString();
                int height=Integer.parseInt(s3);
                String date="0";
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Make sure user insert date into edittext in this format.
                Date dateObject;

                try{
                    s4=e4.getText().toString();
                    dateObject = formatter.parse(s4);

                   date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);
                }

                catch (java.text.ParseException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("", e.toString());
                }
                db.CompleteRegistration(user,name,gender,height,date);
                Intent i= new Intent(CompleteRegister.this,InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}
