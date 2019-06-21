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
import android.widget.Toast;

public class Set_Appointment extends AppCompatActivity {


    TextView date;
    EditText title, description;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__appointment);
        final String add_date=getIntent().getStringExtra("date");
        date=findViewById(R.id.current_date);
        date.setText(add_date);
        title=findViewById(R.id.appt_title);
        description=findViewById(R.id.app_description);
        b1=findViewById(R.id.add_appointment);
        db = new DatabaseHelper(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user=getIntent().getIntExtra("key_email",1);
                String adescription = description.getText().toString();
                String atitle = title.getText().toString();
                if (atitle.equals(""))
                    Toast.makeText(getApplicationContext(), "Please fill Title", Toast.LENGTH_SHORT).show();
                else {
                    App app = new App(atitle,adescription, add_date, user);
                    db.addApp(app);
                    Intent i = new Intent(Set_Appointment.this, InitialPage.class);
                    i.putExtra("key_email", user);
                    i.putExtra("toOpen", 2);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Appointment set successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
