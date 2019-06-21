package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_Medication extends AppCompatActivity {


    EditText e1,e2;
    Button b1,b2;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__medication);
        e1 = (EditText)findViewById(R.id.text_name);
        e2 = (EditText)findViewById(R.id.text_quantity);
        b1 = (Button) findViewById(R.id.button_add);
        b2 = (Button) findViewById(R.id.button_return);
        db = new DatabaseHelper(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = e1.getText().toString();
                String no = e2.getText().toString();
                if (name.equals("")||no.equals(""))
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                else {
                    int user = getIntent().getIntExtra("key_email", 0);
                    boolean chkmed = db.chkmed(name,user);
                    int quantity = Integer.parseInt(no);
                    Medication medication =
                            new Medication(name, quantity, user);
                    if (!chkmed) {
                        db.addMedication(medication);
                        Toast.makeText(getApplicationContext(), "New Medication Added", Toast.LENGTH_SHORT).show();
                    } else {
                        db.updateMedication(medication);
                        Toast.makeText(getApplicationContext(), name+" quantity Updated", Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(Add_Medication.this, InitialPage.class);
                    i.putExtra("key_email", user);
                    startActivity(i);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",0);
                Intent i= new Intent(Add_Medication.this, InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}