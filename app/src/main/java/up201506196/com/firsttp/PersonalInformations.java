package up201506196.com.firsttp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PersonalInformations extends AppCompatActivity {

    EditText new_e, new_g, new_h, new_b;
    Button change_p;
    DatabaseHelper db;
    ArrayList <String> current_informations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_informations);
        final int user = getIntent().getIntExtra("key_email",0);
        db = new DatabaseHelper(this);
        current_informations=db.getPInformation(user);
        new_e=findViewById(R.id.n_email);
        new_e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    if((new_e.getText().toString()).equals(""))
                    new_e.setText(current_informations.get(0));
            }
        });
        new_g=findViewById(R.id.n_gender);
        new_g.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if((new_g.getText().toString()).equals("")){
                        if ((current_informations.get(1).equals("1")))
                            new_g.setText("F");
                        else
                            new_g.setText("M");
                    }
                }
            }
        });

        new_h=findViewById(R.id.n_height);
        new_h.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    if((new_h.getText().toString()).equals(""))
                        new_h.setText(current_informations.get(2));
            }
        });

        new_b=findViewById(R.id.n_birthdate);
        new_b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    if((new_b.getText().toString()).equals(""))
                        new_b.setText(current_informations.get(3));
            }
        });

        change_p=findViewById(R.id.submit_pchange);
        change_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=new_e.getText().toString();
                String gender=new_g.getText().toString();
                String s3=new_h.getText().toString();
                String s4=new_b.getText().toString();

                if (email.equals("")||gender.equals("")||s3.equals("")||s4.equals(""))
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                else {
                    if (isValidEmail(email)) {
                        boolean chkemail = db.chkotheremail(email, user);
                        if (chkemail == true) {
                            if (gender.equals("F") || gender.equals("M")) {
                                int height = Integer.parseInt(s3);
                                if (height > 120 && height < 230) {
                                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Make sure user insert date into edittext in this format.
                                    Date dateObject;

                                    try {
                                        dateObject = formatter.parse(s4);
                                        String date = new SimpleDateFormat("yyyy-MM-dd").format(dateObject);
                                        db.ChangePersonal(user, email, gender, height, date);
                                        Intent i = new Intent(PersonalInformations.this, InitialPage.class);
                                        i.putExtra("key_email", user);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), "Personal Information Changed Successfully", Toast.LENGTH_SHORT).show();
                                    } catch (java.text.ParseException e) {
                                        // TODO Auto-generated catch block
                                        Toast toast=Toast.makeText(getApplicationContext(), "Please field a valid Birthdate (YYYY-MM-DD)", Toast.LENGTH_SHORT);
                                        TextView tv = toast.getView().findViewById(android.R.id.message);
                                        if (tv != null) tv.setGravity(Gravity.CENTER);
                                        toast.show();
                                    }

                                } else
                                    Toast.makeText(getApplicationContext(), "Height should be between 120 and 230 cm", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "Invalid Gender (F or M)", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
