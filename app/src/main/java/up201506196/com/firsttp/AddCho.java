package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCho extends AppCompatActivity {

    EditText e1;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cho);
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.text_name);
        b1 = (Button) findViewById(R.id.submit_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                String val = e1.getText().toString();
                if (val.equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
                else {
                    int value = Integer.parseInt(val);
                    String type = "cholesterol";
                    Record regist =
                            new Record(value, user, type);
                    db.addRecord(regist);
                    Intent i = new Intent(AddCho.this, InitialPage.class);
                    i.putExtra("key_email", user);
                    i.putExtra("toOpen", 1);
                    i.putExtra("toGraph", 0);
                    startActivity(i);
                }
            }
        });
    }
}
