package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddHeaB extends AppCompatActivity {

    EditText e1;
    Button b1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hea_b);
        db = new DatabaseHelper(this);
        e1 = (EditText) findViewById(R.id.text_name);
        b1 = (Button) findViewById(R.id.submit_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = getIntent().getStringExtra("key_email");
                String val = e1.getText().toString();
                int value = Integer.parseInt(val);
                String type= "heart_beat";
                Regist regist =
                        new  Regist(value,user,type);
                db.addRegist(regist);
                Intent i= new Intent(AddHeaB.this, InitialPage.class);
                i.putExtra("key_email", user);
                i.putExtra("toOpen", 1);
                startActivity(i);
            }
        });

    }
}
