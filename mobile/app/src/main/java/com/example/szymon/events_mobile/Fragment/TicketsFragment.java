package com.example.szymon.events_mobile.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.szymon.events_mobile.Activity.TicketActivity;
import com.example.szymon.events_mobile.Adapter.TicketsListAdapter;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicketsFragment extends Fragment implements TicketsListAdapter.ViewHolder.ClickListener {

    private List<Ticket> ticketsList;
    private TicketsListAdapter ticketsListAdapter;

    public RecyclerView recyclerView;
    private HttpRequestTask mAuthTask;
    private SessionManagement sessionManagement;
    private RestClient restClient;

    public TicketsFragment() {
        // Required empty public constructor
    }

    private void setTicketAdapter(List<Ticket> ticketsList) {
        if (ticketsList != null) {
            ticketsListAdapter.setEventsList(ticketsList);
            recyclerView.setAdapter(ticketsListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        }
    }

    public void refreshAdapter(String sort_type) {
        switch (sort_type) {
            case "title":
                Collections.sort(ticketsList, Ticket.ticketTitleComparator);
                break;
            case "date":
                Collections.sort(ticketsList, Ticket.ticketDateComparator);
                break;
            case "refresh":
                mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails().get(SessionManagement.KEY_ACCESS_TOKEN));
                mAuthTask.execute((Void) null);
                return;
        }
        ticketsListAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        ticketsListAdapter = new TicketsListAdapter(this, getActivity().getApplicationContext());
        sessionManagement = new SessionManagement(getActivity().getApplicationContext());
        restClient = new RestClientImpl(getActivity().getApplicationContext());
        mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails().get(SessionManagement.KEY_ACCESS_TOKEN));
        mAuthTask.execute((Void) null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_tickets_view);
        return view;
    }

    private void showTicketDetails(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), TicketActivity.class);
        intent.putExtra(Constants.TICKET_BUNDLE, ticketsList.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {
        showTicketDetails(position);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        String user_token;

        HttpRequestTask(String userToken) {
            this.user_token = userToken;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                restClient.setBearerAuth(user_token);
                ticketsList = restClient.getTickets(new Token(user_token));
                return true;//RuntimeException
            } catch (final HttpClientErrorException e) {
                Log.d("error", e.getMessage());
                return false;
            } catch (final HttpServerErrorException se) {
                Toast.makeText(getActivity().getApplicationContext(), "Timeout", Toast.LENGTH_SHORT);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            mAuthTask = null;
            if (status) {
                setTicketAdapter(ticketsList);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
