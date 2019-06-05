package up201506196.com.firsttp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorComunication extends AppCompatActivity {

    Button send;
    EditText e1;
    Uri URI = null;
    String user, doctor_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_comunication);

        user=getIntent().getStringExtra("key_email");
        e1 = (EditText)findViewById(R.id.Doctor_Email);
        doctor_email=e1.getText().toString();
        send = (Button)findViewById(R.id.bsend_email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });


    }

    public void sendEmail()
    {
        try
        {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { doctor_email });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Mar");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "ajhahahah");
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
        }
        catch (Throwable t)
        {
            Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
