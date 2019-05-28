package up201506196.com.firsttp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class frag1 extends Fragment {

    public frag1() {
        // Required empty public constructor
    }

    EditText e1,e2;
    Button b1;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_frag1, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        e1 = (EditText) view.findViewById(R.id.text_name);
        e2 = (EditText)view.findViewById(R.id.text_quantity);
        b1 = (Button) view.findViewById(R.id.button_add);
        db = new DatabaseHelper(getActivity());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=e1.getText().toString();
                String no=e2.getText().toString();
                int quantity=Integer.parseInt(no);
                String user = getActivity().getIntent().getExtras().getString("key_email");
                Medication medication =
                        new Medication(name,quantity,user);
                db.addMedication(medication);
                e1.setText("");
                e2.setText("");
            }
        });
    }


}






