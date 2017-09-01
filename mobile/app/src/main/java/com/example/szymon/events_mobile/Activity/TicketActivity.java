package com.example.szymon.events_mobile.Activity;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Fragment.TicketFragment;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.SessionManagement;

public class TicketActivity extends AppCompatActivity {

    private SessionManagement sessionManagement;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManagement = new SessionManagement(getApplicationContext());
        if (!sessionManagement.isLoggedIn()) {
            finish();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            ticket = extras.getParcelable(Constants.TICKET_BUNDLE);

        sessionManagement.checkLogin();

        Log.d("TicketActivity: ", ticket.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.activity_ticket, new TicketFragment().newInstance(ticket), "MY FRAGMENT")
                .addToBackStack(null);
        transaction.commit();
    }
}
