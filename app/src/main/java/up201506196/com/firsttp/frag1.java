package up201506196.com.firsttp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class frag1 extends Fragment {




    public frag1() {
        // Required empty public constructor
    }

    Button b1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_frag1, container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        b1 = (Button) view.findViewById(R.id.button_add);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = getActivity().getIntent().getExtras().getString("key_email");
                Intent i= new Intent(getActivity(),Add_Medication.class);
                i.putExtra("key_email", user);
                startActivity(i);

            }
        });
    }


}






