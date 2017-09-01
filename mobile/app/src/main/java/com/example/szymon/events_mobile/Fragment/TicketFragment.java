package com.example.szymon.events_mobile.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Activity.MainActivity;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;
import com.github.clans.fab.FloatingActionButton;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * A simple {@link Fragment} subclass.
 */
public class TicketFragment extends Fragment {

    private Ticket ticket;
    private String user_token;
    private HttpRequestTask mAuthTask;

    private TextView ticketColor;
    private TextView ticketBuyDate;
    private TextView ticketExpDate;
    private TextView eventColor;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventDate;
    private TextView ticketArtists;

    private RestClient restClient;
    private SessionManagement sessionManagement;

    private com.github.clans.fab.FloatingActionMenu eventFloatingMenu;
    private com.github.clans.fab.FloatingActionButton fabDelete;


    public TicketFragment newInstance(Ticket ticket) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.TICKET_INSTANCE, ticket);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            ticket = savedInstanceState.getParcelable(Constants.TICKET_INSTANCE);
        } else {
            ticket = getTicket();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        eventFloatingMenu = (com.github.clans.fab.FloatingActionMenu) view.findViewById(R.id.fab_event);
        fabDelete = (FloatingActionButton) view.findViewById(R.id.fab_ticket_delete);

        ticketColor = (TextView) view.findViewById(R.id.ticket_color);
        ticketBuyDate = (TextView) view.findViewById(R.id.ticket_buy_date);
        ticketExpDate = (TextView) view.findViewById(R.id.ticket_date2);
        eventColor = (TextView) view.findViewById(R.id.ticket_color2);
        eventTitle = (TextView) view.findViewById(R.id.ticket_title);
        eventDescription = (TextView) view.findViewById(R.id.ticket_description);
        eventDate = (TextView) view.findViewById(R.id.ticket_date);
        ticketArtists = (TextView) view.findViewById(R.id.ticket_artists);

        restClient = new RestClientImpl(getActivity());
        sessionManagement = new SessionManagement(getActivity().getApplicationContext());
        user_token = sessionManagement.getUserDetails().get(SessionManagement.KEY_ACCESS_TOKEN);

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTicketDialog();
            }
        });

        setContent();

        return view;
    }

    public void deleteTicketDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Usunięcie biletu");
        builder.setMessage("Czy napewno chcesz usunąć bilet?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuthTask = new HttpRequestTask(ticket.getId(), user_token);
                mAuthTask.execute((Void) null);
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

    private void setContent() {
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        eventColor.setBackgroundColor(colorGenerator.getColor(ticket.getDate()));
        ticketBuyDate.setText(ticket.getDate());
        ticketExpDate.setText(ticket.getEvent().getStart_date());
        eventColor.setBackgroundColor(colorGenerator.getColor(ticket.getEvent().getId()));
        eventTitle.setText(ticket.getEvent().getTitle());
        eventDescription.setText(ticket.getEvent().getDescription());
        eventDate.setText(ticket.getEvent().getStart_date());
    }

    private Ticket getTicket() {
        return getArguments().getParcelable(Constants.TICKET_INSTANCE);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        private Integer ticket_id;
        private String user_token;

        HttpRequestTask(Integer ticket_id, String user_token) {
            this.user_token = user_token;
            this.ticket_id = ticket_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                restClient.setBearerAuth(user_token);
                restClient.deleteTicket(ticket_id);
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
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}