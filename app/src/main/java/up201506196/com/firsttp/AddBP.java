package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBP extends AppCompatActivity {

    EditText e1,e2;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bp);
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.text_name1);
        e2 = (EditText) findViewById(R.id.text_name2);
        b1 = (Button) findViewById(R.id.submit_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                String val1 = e1.getText().toString();
                String val2 = e2.getText().toString();
                if (val1.equals("") || val2.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter both Diastolic and Systolic Blood Pressure", Toast.LENGTH_SHORT).show();
                } else {
                    int value1 = Integer.parseInt(val1);
                    int value2 = Integer.parseInt(val2);
                    String type1 = "high_blood_pressure";
                    String type2 = "low_blood_pressure";
                    Record regist1 =
                            new Record(value1, user, type1);
                    db.addRecord(regist1);
                    Record regist2 =
                            new Record(value2, user, type2);
                    db.addRecord(regist2);
                    Intent i = new Intent(AddBP.this, InitialPage.class);
                    i.putExtra("key_email", user);
                    i.putExtra("toOpen", 1);
                    startActivity(i);
                }
            }
        });

    }
}
