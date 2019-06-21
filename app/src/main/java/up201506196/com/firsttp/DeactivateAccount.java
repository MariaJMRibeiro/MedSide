package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DeactivateAccount extends AppCompatActivity {

    EditText ed1,ed2;
    Button d_button;
    DatabaseHelper db;
    CheckBox save_data;
    Boolean save=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);
        db= new DatabaseHelper(this);
        ed1=(EditText)findViewById(R.id.d_email);
        ed2=(EditText)findViewById(R.id.d_pass);
        save_data=findViewById(R.id.chksave);
        d_button=(Button)findViewById(R.id.d_submit_button);
        d_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",0);
                String email = ed1.getText().toString();
                String password = ed2.getText().toString();
                if(save_data.isChecked())
                    save=true;
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields are empty ", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean Chkemailpass = db.useremailpassword(user,email,password);
                    if (Chkemailpass) {
                        db.DeleteUser(user,email,save);
                        Intent i = new Intent(DeactivateAccount.this, MainActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Account Successfully Deactivated ", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Email or Password Incorrect ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
