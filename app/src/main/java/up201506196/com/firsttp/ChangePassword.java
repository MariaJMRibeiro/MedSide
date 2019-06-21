package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    EditText e,e2,e3;
    Button change;
    String actual, new_p, confirm_p;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db= new DatabaseHelper(this);
        setContentView(R.layout.activity_change_password);
        e=findViewById(R.id.password_change);
        e2=findViewById(R.id.new_password);
        e3=findViewById(R.id.confirm_new_password);
        change=findViewById(R.id.changep_button);
        change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int user = getIntent().getIntExtra("key_email",1);
                actual=e.getText().toString();
                new_p=e2.getText().toString();
                confirm_p=e3.getText().toString();
                if(actual.equals("")||new_p.equals("")||confirm_p.equals("")){
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    boolean chkpass= db.chkpass(actual,user);
                    if(chkpass){
                        if(new_p.equals(confirm_p)){
                            if(new_p.equals(actual)){
                                Toast.makeText(getApplicationContext(), "Cannot change to equal Password", Toast.LENGTH_SHORT).show();
                                e2.setText("");
                                e3.setText("");
                            }
                            else{
                                db.changepass(new_p,user);
                                Intent i = new Intent(ChangePassword.this, InitialPage.class);
                                i.putExtra("key_email", user);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), "Successfully Changed Password", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "New Passwords do not match", Toast.LENGTH_SHORT).show();
                            e2.setText("");
                            e3.setText("");
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Current password incorrect", Toast.LENGTH_SHORT).show();
                        e.setText("");
                    }
                }
            }
        });

    }
}
