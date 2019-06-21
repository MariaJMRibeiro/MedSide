package up201506196.com.firsttp;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> title;
    ArrayList<String> description;
    ArrayList<String> date;


    public AppListAdapter(Context context2, ArrayList<String> title, ArrayList<String> description, ArrayList<String> date) {
        this.context = context2;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.size();
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

            child = layoutInflater.inflate(R.layout.activity_app_list_adapter, null);

            holder = new Holder();

            holder.title_TextView= child.findViewById(R.id.show_title);
            holder.description_TextView = child.findViewById(R.id.show_description);
            holder.date_TextView = child.findViewById(R.id.show_date);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.title_TextView.setText(title.get(position));
        holder.description_TextView.setText(description.get(position));
        holder.date_TextView.setText(date.get(position));

        return child;
    }

    public class Holder {

        TextView title_TextView;
        TextView description_TextView;
        TextView date_TextView;
    }

}