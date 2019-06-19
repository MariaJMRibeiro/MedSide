package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Set_Appointment extends AppCompatActivity {


    TextView date;
    EditText description;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__appointment);
        final String add_date=getIntent().getStringExtra("date");
        date=findViewById(R.id.current_date);
        date.setText(add_date);
        description=findViewById(R.id.app_description);
        b1=findViewById(R.id.add_appointment);
        db = new DatabaseHelper(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user=getIntent().getIntExtra("key_email",1);
                String adescription = description.getText().toString();
                App app =
                        new App(adescription, add_date, user);
                db.addApp(app);
                Intent i= new Intent(Set_Appointment.this, InitialPage.class);
                i.putExtra("key_email", user);
                i.putExtra("toOpen", 2);
                startActivity(i);



            }
        });

    }
}
