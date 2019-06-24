package up201506196.com.firsttp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText e1,e2;
    Button b1;
    TextView t1;
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
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean Chkemail = db.chkemail(email);
                    if (!Chkemail) {
                        Boolean Chkemailpass = db.emailpassword(email,password);
                            if (Chkemailpass) {
                                int user=db.getUserId(email);
                                Intent i = new Intent(Login.this, InitialPage.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), "Successfully Login ", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Password Incorrect ", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getApplicationContext(), "Email not exists ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        t1=(TextView) findViewById(R.id.NRegister);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setTextColor(Color.parseColor("#6491B9"));
                Intent i= new Intent(Login.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
}
