package com.example.szymon.events_mobile.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.Model.User;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private SessionManagement sessionManagement;
    private RestClient restClient;
    private HttpRequestTask mAuthTask;
    private User user;

    private ImageView circleProfile;
    private TextView screenName;
    private TextView socialID;
    private TextView userName;
    private TextView userEmail;
    private TextView userLocalization;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        circleProfile = (ImageView) view.findViewById(R.id.circleProfile);
        screenName = (TextView) view.findViewById(R.id.screen_name);
        socialID = (TextView) view.findViewById(R.id.social_id);
        userName = (TextView) view.findViewById(R.id.user_name);
        userEmail = (TextView) view.findViewById(R.id.user_email);
        userLocalization = (TextView) view.findViewById(R.id.user_localization);

        sessionManagement = new SessionManagement(getActivity().getApplicationContext());
        restClient = new RestClientImpl(getActivity().getApplicationContext());
        mAuthTask = new HttpRequestTask(sessionManagement.getUserDetails().get(SessionManagement.KEY_ACCESS_TOKEN));
        mAuthTask.execute((Void) null);
        return view;
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
                user = restClient.getUserData(new Token(user_token));
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

                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(user.getScreen_name().charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(String.valueOf(user.getScreen_name().charAt(0)), color);
                circleProfile.setImageDrawable(drawable);
                screenName.setText(user.getScreen_name());
                socialID.setText(String.valueOf(user.getSocial_id()));
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());
                userLocalization.setText(user.getLocation());
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
