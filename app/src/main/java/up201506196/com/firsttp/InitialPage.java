package up201506196.com.firsttp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class InitialPage extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        int index = getIntent().getIntExtra("toOpen", 0);
        mViewPager.setCurrentItem(index);

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(InitialPage.this, Receiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),0, receiverIntent, 0); //The second parameter is unique to this PendingIntent,



        int hour = 8;
        int minute =30;

        Calendar alarmCalendarTime = Calendar.getInstance();
        alarmCalendarTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendarTime.set(Calendar.MINUTE, minute);
        alarmCalendarTime.set(Calendar.SECOND, 0);

        if (alarmCalendarTime.before(Calendar.getInstance())) {
            alarmCalendarTime.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendarTime.getTimeInMillis(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), alarmIntent);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                int user = getIntent().getIntExtra("key_email",0);
                Intent intent2 = new Intent(this, Settings.class);
                intent2.putExtra("key_email",user);
                this.startActivity(intent2);
                break;
            case R.id.action_logout:
                Intent intent3 = new Intent(this, Login.class);
                this.startActivity(intent3);
                Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment= new frag1();
                    break;
                case 1:
                    fragment= new frag2();
                    break;
                case 2:
                    fragment=new frag3();
                    break;
                case 3:
                    fragment=new frag4();
                    break;
            }
            return fragment;

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
