package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                int quantity = Integer.parseInt(no);
                String user = getIntent().getExtras().getString("key_email");
                Medication medication =
                        new Medication(name, quantity, user);
                db.addMedication(medication);
                Intent i= new Intent(Add_Medication.this,InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = getIntent().getExtras().getString("key_email");
                Intent i= new Intent(Add_Medication.this, InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}