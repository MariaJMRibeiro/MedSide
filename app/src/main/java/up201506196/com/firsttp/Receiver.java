package up201506196.com.firsttp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

    DatabaseHelper db;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO code executed on alarm trigger
        db = new DatabaseHelper(context);
        db.StockUpdate();
    }
}