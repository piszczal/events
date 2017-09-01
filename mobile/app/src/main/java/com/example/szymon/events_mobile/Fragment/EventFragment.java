package com.example.szymon.events_mobile.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.Model.Subscription;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;
import com.github.clans.fab.FloatingActionMenu;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Szymon on 17.05.2017.
 */

public class EventFragment  extends Fragment {

    private Integer event_id;
    private String user_token;
    private RestClient restClient;
    private HttpRequestTask mAuthTask;
    private Event event;
    private List<Ticket> ticketsList = new ArrayList<>();
    private List<Subscription> subscriptionList = new ArrayList<>();

    private FrameLayout frameLayout;

    private com.github.clans.fab.FloatingActionMenu eventFloatingMenu;
    private com.github.clans.fab.FloatingActionButton fabBuy, fabNewsletter;

    private CharSequence[] dialogValues = {"Mała ilość biletów", "Edycja wydarzenia"};

    /**
     * WIDOK
     **/
    private TextView eventColor;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventDate;
    private TextView eventTicket;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private String sub_type;

    public EventFragment newInstance(Integer event_id, String user_token) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EVENT_ID, event_id);
        args.putString(Constants.USER_TOKEN, user_token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            event = savedInstanceState.getParcelable(Constants.EVENT_STATE);
            ticketsList = savedInstanceState.getParcelableArrayList("ticket_list");
            subscriptionList = savedInstanceState.getParcelableArrayList("subscription_list");
            user_token = savedInstanceState.getString(Constants.USER_TOKEN_STATE);
        } else {
            event_id = getEventID();
            user_token = getUserToken();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventFloatingMenu = (com.github.clans.fab.FloatingActionMenu) view.findViewById(R.id.fab_event);
        fabBuy = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab_buy);
        fabNewsletter = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.fab_newsletter);
        eventColor = (TextView) view.findViewById(R.id.event_color);
        eventTitle = (TextView) view.findViewById(R.id.event_title);
        eventDescription = (TextView) view.findViewById(R.id.event_description);
        eventDate = (TextView) view.findViewById(R.id.event_date);
        eventTicket = (TextView) view.findViewById(R.id.event_ticket);
        frameLayout = (FrameLayout) view.findViewById(R.id.coordinatorLayout);

        if (savedInstanceState == null) {
            restClient = new RestClientImpl(getActivity().getApplicationContext());
            mAuthTask = new HttpRequestTask(user_token, event_id, null, 0);
            mAuthTask.execute((Void) null);
        } else {
            event = savedInstanceState.getParcelable(Constants.EVENT_STATE);
            ticketsList = savedInstanceState.getParcelableArrayList("ticket_list");
            subscriptionList = savedInstanceState.getParcelableArrayList("subscription_list");
            user_token = savedInstanceState.getString(Constants.USER_TOKEN_STATE);
            setContent();
        }

        fabBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyTicketDialog();
            }
        });

        fabNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewsletter();
            }
        });

        return view;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog downloadDialog;
        String user_token;
        String sub_type;
        Integer event_id;
        Integer request_type;

        HttpRequestTask(String userToken, Integer eventID, String sub_type, Integer request_type) {
            this.user_token = userToken;
            this.sub_type = sub_type;
            this.event_id = eventID;
            this.request_type = request_type;
        }

        @Override
        protected void onPreExecute() {
            if (request_type == 0) {
                downloadDialog = new ProgressDialog(getActivity());
                downloadDialog.setMessage("Trwa pobieranie. Proszę czekać!");
                downloadDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                restClient.setBearerAuth(user_token);
                if (request_type == 0) {
                    event = restClient.getEvent(event_id);
                    ticketsList = restClient.getTickets(new Token(user_token));
                    subscriptionList = restClient.getSubscriptions(new Token(user_token));
                } else if (request_type == 1) {
                    Log.d("Buy ticket: ", "kupno biletu");
                    restClient.sendTicket(event_id, new Token(user_token));
                } else if (request_type == 2) {
                    restClient.subscriptionEvent(event_id, sub_type, new Token(user_token));
                }
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
            if (status && request_type == 0) {
                downloadDialog.dismiss();
                setContent();
            } else if (status && request_type == 1) {
                fabBuy.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(frameLayout, "Bilet został zakupiony", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (status && request_type == 2) {
                fabNewsletter.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(frameLayout, "Zostałeś zapisany do newslettera :)", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private void setContent() {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        eventColor.setBackgroundColor(colorGenerator.getColor(event.getId()));
        eventTitle.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getStart_date());
        if (event.getHow_many_tickets() - event.getHow_many_tickets_sold() > 0)
            eventTicket.setText(String.valueOf(event.getHow_many_tickets() - event.getHow_many_tickets_sold()));
        else
            eventTicket.setText(getResources().getString(R.string.tickets_sold));

        getActivity().setTitle(event.getTitle());
        if (checkTickets(ticketsList))
            fabBuy.setVisibility(View.GONE);
        if (event.getHow_many_tickets() == 0)
            fabBuy.setVisibility(View.GONE);
        if (checkSubscription(subscriptionList))
            fabNewsletter.setVisibility(View.GONE);
        if (checkSubscription(subscriptionList) && checkTickets(ticketsList)) {
            fabBuy.setVisibility(View.GONE);
            fabNewsletter.setVisibility(View.GONE);
            eventFloatingMenu.setVisibility(View.GONE);
        }
    }

    public void buyTicketDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Zakup biletu");
        builder.setMessage("Czy napewno chcesz kupić bilet?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuthTask = new HttpRequestTask(user_token, event_id, null, 1);
                mAuthTask.execute((Void) null);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void saveNewsletter() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Newsletter");
        builder.setMessage("Wybierz typ subskrpycji?");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.newsletter_dialog, null);
        radioGroup = (RadioGroup) dialogLayout.findViewById(R.id.newsletter_group);

        builder.setPositiveButton("Zapisz mnie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) dialogLayout.findViewById(selectedId);
                if (radioButton.getId() == R.id.low) {
                    sub_type = "low";
                }
                if (radioButton.getId() == R.id.edit) {
                    sub_type = "edit";
                }
                mAuthTask = new HttpRequestTask(user_token, event_id, sub_type, 2);
                mAuthTask.execute((Void) null);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.setView(dialogLayout);
        builder.show();
    }


    private boolean checkTickets(List<Ticket> ticketsList) {
        for (int i = 0; i < ticketsList.size(); i++) {
            if (ticketsList.get(i).getEvent().getId() == event.getId())
                return true;
        }
        return false;
    }

    private boolean checkSubscription(List<Subscription> subscriptionList) {
        for (int i = 0; i < subscriptionList.size(); i++) {
            if (subscriptionList.get(i).getEvent().getId() == event.getId())
                return true;
        }
        return false;
    }

    private Integer getEventID() {
        return getArguments().getInt(Constants.EVENT_ID);
    }

    private String getUserToken() {
        return getArguments().getString(Constants.USER_TOKEN);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.EVENT_STATE, event);
        outState.putString(Constants.USER_TOKEN_STATE, user_token);
        outState.getParcelableArrayList("ticket_list");
        outState.getParcelableArrayList("subscription_list");
        super.onSaveInstanceState(outState);
    }
}
