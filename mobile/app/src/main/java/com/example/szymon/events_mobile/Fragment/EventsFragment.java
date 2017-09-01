package com.example.szymon.events_mobile.Fragment;


import android.app.ProgressDialog;
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

import com.example.szymon.events_mobile.Activity.EventActivity;
import com.example.szymon.events_mobile.Activity.LoginActivity;
import com.example.szymon.events_mobile.Adapter.EventsListAdapter;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements EventsListAdapter.ViewHolder.ClickListener {

    private List<Event> eventsList;
    private EventsListAdapter eventsListAdapter;
    public RecyclerView recyclerView;
    private HttpRequestTask mAuthTask;
    private SessionManagement sessionManagement;
    private RestClient restClient;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventsListAdapter = new EventsListAdapter(this, getActivity().getApplicationContext());
        sessionManagement = new SessionManagement(getActivity().getApplicationContext());
        restClient = new RestClientImpl(getActivity().getApplicationContext());
        mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails());
        mAuthTask.execute((Void) null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    private void setEventAdapter(List<Event> eventsList){
        if (eventsList != null) {
            getActivity().setTitle("Wydarzenia "+" ("+eventsList.size()+")");
            eventsListAdapter.setEventsList(eventsList);
            recyclerView.setAdapter(eventsListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 1));
        }
    }

    public void refreshAdapter(String sort_type){
        switch(sort_type)
        {
            case "title":
                Collections.sort(eventsList, Event.eventTitleComparator);
                break;
            case "description":
                Collections.sort(eventsList, Event.eventDescriptionComparator);
                break;
            case "date":
                Collections.sort(eventsList, Event.eventDateComparator);
                break;
            case "refresh":
                mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails());
                mAuthTask.execute((Void) null);
                return;
        }
        eventsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(int position) {
        showEventDetails(position);
    }

    private void showEventDetails(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), EventActivity.class);
        intent.putExtra(Constants.EVENT_ID, eventsList.get(position).getId());
        startActivity(intent);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        //private ProgressDialog downloadDialog;
        String user_token;
        HttpRequestTask(HashMap<String, String> session) {
            this.user_token = session.get(SessionManagement.KEY_ACCESS_TOKEN);
        }

        /*@Override
        protected void onPreExecute() {
            downloadDialog = new ProgressDialog(getActivity());
            downloadDialog.setMessage("Trwa pobieranie. Proszę czekać!");
            downloadDialog.show();
        }*/

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                restClient.setBearerAuth(user_token);
                eventsList = restClient.getEvents("title");
                return true;//RuntimeException
            } catch (final HttpClientErrorException e) {
                Log.d("error", e.getMessage());
                return false;
            }catch(final HttpServerErrorException se){
                Toast.makeText(getActivity().getApplicationContext(),"Timeout", Toast.LENGTH_SHORT);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            mAuthTask = null;
            //downloadDialog.dismiss();
            if (status) {
                setEventAdapter(eventsList);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
