package up201506196.com.firsttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Settings extends AppCompatActivity {

    ListView listView;

    // Define string array.
    String[] listValue = new String[] {"Change Password","Customise Doctor Communication", "Personal Information", "Deactivate Account"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView)findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listValue);

        listView.setAdapter(adapter);

        // ListView on item selected listener.
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String user = getIntent().getExtras().getString("key_email");

                if (position == 0) {
                    //code specific to first list item
                    Intent myIntent = new Intent(view.getContext(), ChangePassword.class);
                    myIntent.putExtra("key email", user);
                    startActivityForResult(myIntent, 0);
                }
                if (position == 1) {
                    //code specific to first list item
                    Intent myIntent = new Intent(view.getContext(),DoctorComunication.class);
                    myIntent.putExtra("key email", user);
                    startActivityForResult(myIntent, 0);
                }
                if (position == 3) {
                    //code specific to first list item
                }
                if (position == 2) {
                    //code specific to first list item
                    Intent myIntent = new Intent(view.getContext(), PersonalInformations.class);
                    myIntent.putExtra("key email", user);
                    startActivityForResult(myIntent, 0);
                }
            }
        });

    }
}