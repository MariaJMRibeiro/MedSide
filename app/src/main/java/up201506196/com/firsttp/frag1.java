package up201506196.com.firsttp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class frag1 extends Fragment {

    public frag1() {
        // Required empty public constructor
    }

    DatabaseHelper db;
    EditText e1,e2;
    Button b1, b2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_frag1, container, false);

        db = new DatabaseHelper(null);
        e1=(EditText)rootView.findViewById(R.id.text_name);
        e2=(EditText)rootView.findViewById(R.id.text_quantity);
        b1=(Button)rootView.findViewById(R.id.button_add);
        b2=(Button)rootView.findViewById(R.id.button_reset);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                db.deleteMedication(s1);
            }
        });
        b1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = e1.getText().toString();
                String s2 = e2.getText().toString();
                int m2 = Integer.parseInt(s2);
                if (s1.equals("")||s2.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Medication medication= new Medication();
                    medication.setMedicationName(s1);
                    medication.setQuantity(m2);
                    db.addMedication(medication);
                    Toast.makeText(getActivity().getApplicationContext(), "Medication correcty insert", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}






