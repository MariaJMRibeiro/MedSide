package up201506196.com.firsttp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class frag2 extends Fragment {

    EditText e1;
    Button b1;
    DatabaseHelper db;

    public frag2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_frag2, container, false);

        return view;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        e1= (EditText) view.findViewById(R.id.text_name);
        b1= (Button) view.findViewById(R.id.submit_button);
        db = new DatabaseHelper(getActivity());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = getActivity().getIntent().getExtras().getString("key_email");
                String val = e1.getText().toString();
                int value = Integer.parseInt(val);
                Cholesterol cholesterol =
                        new Cholesterol(value, user);
                db.addCholesterol(cholesterol);
                e1.setText("");
            }
        });

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 5),
                new DataPoint(5, 3),
                new DataPoint(6, 2),
                new DataPoint(7, 6)
        });
        graph.addSeries(series);
    }


}
