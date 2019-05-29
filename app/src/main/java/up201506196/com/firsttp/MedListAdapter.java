package up201506196.com.firsttp;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MedListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> name;
    ArrayList<String> quantity;


    public MedListAdapter(
            Context context2,
            ArrayList<String> name,
            ArrayList<String> quantity
    )
    {

        this.context = context2;
        this.name = name;
        this.quantity = quantity;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return name.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.adapter_med, null);

            holder = new Holder();

            holder.name_TextView = (TextView) child.findViewById(R.id.show_name);
            holder.quantity_TextView = (TextView) child.findViewById(R.id.show_quantity);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.name_TextView.setText(name.get(position));
        holder.quantity_TextView.setText(quantity.get(position));

        return child;
    }

    public class Holder {

        TextView name_TextView;
        TextView quantity_TextView;
    }

}