package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Delete_Medication extends AppCompatActivity {

    EditText e1;
    Button b1,b2;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__medication);
        e1 = (EditText)findViewById(R.id.text_name);
        b1 = (Button) findViewById(R.id.button_reset);
        b2 = (Button) findViewById(R.id.button_return);
        db = new DatabaseHelper(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user =getIntent().getIntExtra("key_email",1);
                String name = e1.getText().toString();
                db.deleteMedication(name,user);
                Intent i= new Intent(Delete_Medication.this,InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                Intent i= new Intent(Delete_Medication.this, InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}