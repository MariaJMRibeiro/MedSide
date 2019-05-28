package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText e1,e2;
    Button b1,b2;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db= new DatabaseHelper(this);
        e1=(EditText)findViewById(R.id.lemail);
        e2=(EditText)findViewById(R.id.lpass);
        b1=(Button)findViewById(R.id.TLogin);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean Chkemailpass = db.emailpassword(email,password);
                if (Chkemailpass==true) {
                    Toast.makeText(getApplicationContext(), "Successfully Login ", Toast.LENGTH_SHORT).show();
                    Intent i= new Intent(Login.this,InitialPage.class);
                    i.putExtra("key_email", email);
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(), "Email or Password incorrect ", Toast.LENGTH_SHORT).show();
            }
        });
        b2=(Button)findViewById(R.id.NRegister);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Login.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
