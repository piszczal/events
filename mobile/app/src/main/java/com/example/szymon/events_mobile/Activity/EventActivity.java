package com.example.szymon.events_mobile.Activity;

import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.szymon.events_mobile.Adapter.SectionsPagerAdapter;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Fragment.CommentsFragment;
import com.example.szymon.events_mobile.Fragment.EventFragment;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;

public class EventActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private AppBarLayout appBar;
    private TabLayout tabLayout;
    private Integer event_id;
    private String user_token;
    private SessionManagement sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        sessionManagement = new SessionManagement(getApplicationContext());
        if (!sessionManagement.isLoggedIn()) {
            finish();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            event_id = extras.getInt(Constants.EVENT_ID);

        if (savedInstanceState != null) {
            event_id = savedInstanceState.getInt("event_id");
            user_token = savedInstanceState.getString("user_token");
        }

        sessionManagement.checkLogin();
        user_token = sessionManagement.getUserDetails().get(SessionManagement.KEY_ACCESS_TOKEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new EventFragment().newInstance(event_id, user_token), "Informacje");
        mSectionsPagerAdapter.addFragment(new CommentsFragment().newInstance(event_id, user_token), "Komentarze");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //appBar = (AppBarLayout) findViewById(R.id.appbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        //appBar.addView(tabLayout);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("user_token", user_token);
        outState.putInt("event_id", event_id);
        super.onSaveInstanceState(outState);
    }
    
}
