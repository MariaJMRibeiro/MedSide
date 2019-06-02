package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Set_Appointment extends AppCompatActivity {


    TextView date;
    EditText description;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__appointment);
        String add_date=getIntent().getStringExtra("date");
        String user=getIntent().getStringExtra("key_email");
        date=findViewById(R.id.current_date);
        if(add_date != null) {
            date.setText(add_date);
        }
    }
}
