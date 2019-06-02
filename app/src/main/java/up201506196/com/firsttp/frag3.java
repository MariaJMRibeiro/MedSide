package up201506196.com.firsttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;


public class frag3 extends Fragment {

    CalendarView calender;
    TextView currentDate;
    Button b1;
    boolean setdate=false;



    public frag3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View View=inflater.inflate(R.layout.fragment_frag3, container, false);
        return View;
    }

    public void onViewCreated(final View view, Bundle saveInstanceState){
        calender=(CalendarView)view.findViewById(R.id.calender);
        currentDate=(TextView)view.findViewById(R.id.current_date);
        b1=(Button)view.findViewById(R.id.add_appointment);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final String date= dayOfMonth + "/" + month + "/" + year;
                currentDate.setText(date);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user = getActivity().getIntent().getExtras().getString("key_email");
                        Intent i= new Intent(getActivity(),Set_Appointment.class);
                        i.putExtra("date",date);
                        i.putExtra("key_email",user);
                        startActivity(i);
                    }
                });
            }

        });

    }
}
