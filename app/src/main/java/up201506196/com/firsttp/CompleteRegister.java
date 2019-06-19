package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CompleteRegister extends AppCompatActivity {

    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_register);
        b1=findViewById(R.id.final_register_button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                Intent i= new Intent(CompleteRegister.this,InitialPage.class);
                i.putExtra("key_email", user);
                startActivity(i);
            }
        });

    }
}
