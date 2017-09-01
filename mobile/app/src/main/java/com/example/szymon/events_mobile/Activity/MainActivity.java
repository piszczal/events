package com.example.szymon.events_mobile.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.szymon.events_mobile.Fragment.EventsFragment;
import com.example.szymon.events_mobile.Fragment.ProfileFragment;
import com.example.szymon.events_mobile.Fragment.TicketsFragment;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;
import com.example.szymon.events_mobile.Adapter.ViewPagerAdapter;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "i31qtGLCToSYZ2QBd31QNJcVK";
    private static final String TWITTER_SECRET = "8yfp9nwPurPlaouTfF0ARwRofLggnC412sEpDEG2oDVBRP6DOu";

    private RestClient restClient;
    private SessionManagement sessionManagement;
    //private HttpRequestTask mAuthTask;

    private Boolean events_fragment = true;
    private Boolean if_refresh = false;
    private List<Event> eventsList;

    private ViewPager viewPager;
    //Fragments
    private EventsFragment eventsFragment;
    private TicketsFragment ticketsFragment;
    private ProfileFragment profileFragment;

    private BottomNavigationView navigation;
    private MenuItem prevMenuItem;
    private MenuItem menuItem;
    private ViewPagerAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    events_fragment = true;
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    events_fragment = false;
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    events_fragment = false;
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_logout:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                    sessionManagement.logoutUser();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        sessionManagement = new SessionManagement(getApplicationContext());
        restClient = new RestClientImpl(getApplicationContext());
        eventsList = new ArrayList<>();
        //mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails());
        //mAuthTask.execute((Void) null);

        menuItem = (MenuItem) findViewById(R.id.sort);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (!sessionManagement.isLoggedIn()) {
            finish();
        }
        sessionManagement.checkLogin();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        eventsFragment=new EventsFragment();
        ticketsFragment=new TicketsFragment();
        profileFragment = new ProfileFragment();
        adapter.addFragment(eventsFragment);
        adapter.addFragment(ticketsFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort_title:
                eventsFragment.refreshAdapter("title");
                ticketsFragment.refreshAdapter("title");
                return true;
            case R.id.sort_description:
                eventsFragment.refreshAdapter("description");
                return true;
            case R.id.sort_date:
                eventsFragment.refreshAdapter("date");
                ticketsFragment.refreshAdapter("date");
                return true;
            case R.id.refresh:
                eventsFragment.refreshAdapter("refresh");
                ticketsFragment.refreshAdapter("refresh");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (events_fragment) {
            menu.getItem(0).setVisible(true);
            menu.getItem(0).setEnabled(true);
        } else {
            menu.getItem(0).setVisible(false);
            menu.getItem(0).setEnabled(false);
        }
        return true;
    }


}
