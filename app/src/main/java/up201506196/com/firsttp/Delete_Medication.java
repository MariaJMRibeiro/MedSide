package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Delete_Medication extends AppCompatActivity {

    EditText e1,e2;
    Button b1,b2;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__medication);
        e1 = (EditText)findViewById(R.id.text_name);
        e2 = (EditText)findViewById(R.id.text_quantity);
        b1 = (Button) findViewById(R.id.button_reset);
        b2 = (Button) findViewById(R.id.button_return);
        db = new DatabaseHelper(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user =getIntent().getIntExtra("key_email",0);
                String name = e1.getText().toString();
                boolean chkmed=db.chkmed(name,user);
                if(!chkmed)
                    Toast.makeText(getApplicationContext(), "Medication doesn't exists yet", Toast.LENGTH_SHORT).show();
                else {
                    String s2 = e2.getText().toString();
                    if (s2.equals("")) {
                        db.deleteMedication(name, user);
                        Intent i = new Intent(Delete_Medication.this, InitialPage.class);
                        i.putExtra("key_email", user);
                        startActivity(i);
                        Toast t= Toast.makeText(getApplicationContext(), name + " was deleted successfully", Toast.LENGTH_SHORT);
                        TextView tv = t.getView().findViewById(android.R.id.message);
                        if (tv != null) tv.setGravity(Gravity.CENTER);
                        t.show();
                    }
                    else {
                        int quantity = Integer.parseInt(s2);
                        Medication medication = new Medication(name, quantity, user);
                        boolean delete = db.deleteQuantityMedication(medication);
                        Intent i = new Intent(Delete_Medication.this, InitialPage.class);
                        i.putExtra("key_email", user);
                        startActivity(i);
                        if (delete) {
                            Toast t2=Toast.makeText(getApplicationContext(), quantity+" "+name + "(s) successfully deleted", Toast.LENGTH_SHORT);
                            TextView tv = t2.getView().findViewById(android.R.id.message);
                            if (tv != null) tv.setGravity(Gravity.CENTER);
                            t2.show();
                        }
                        else{
                            Toast t3=Toast.makeText(getApplicationContext(), "Error: not able to delete "+quantity+" "+name + "(s)", Toast.LENGTH_SHORT);
                            TextView tv = t3.getView().findViewById(android.R.id.message);
                            if (tv != null) tv.setGravity(Gravity.CENTER);
                            t3.show();
                        }
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",0);
                Intent i= new Intent(Delete_Medication.this, InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}