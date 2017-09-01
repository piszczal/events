package com.example.szymon.events_mobile.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;
import com.example.szymon.events_mobile.SessionManagement;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class LoginActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;
    private RestClient restClient;
    private HttpRequestTask mAuthTask = null;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManagement = new SessionManagement(getApplicationContext());
        restClient = new RestClientImpl(getApplicationContext());
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                mAuthTask = new HttpRequestTask(String.valueOf(session.getUserId()),session.getUserName(),
                        session.getAuthToken().token,session.getAuthToken().secret);
                mAuthTask.execute((Void) null);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        private ProgressDialog loginDialog;
        private String user_id;
        private String user_name;
        private String user_token;
        private String secret_token;
        private String token;

        HttpRequestTask(String user_id, String user_name, String user_token, String secret_token) {
            this.user_id = user_id;
            this.user_name = user_name;
            this.user_token = user_token;
            this.secret_token = secret_token;
        }

        @Override
        protected void onPreExecute() {
            loginDialog = new ProgressDialog(LoginActivity.this);
            loginDialog.setMessage("Trwa logowanie. Proszę czekać!");
            loginDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                token = restClient.authorizationTwitter(user_token,secret_token);
                return true;
            } catch (final HttpClientErrorException e) {
                Log.d("error", e.getResponseBodyAsString());
                return false;
            }catch(final HttpServerErrorException se){
                Toast.makeText(getApplicationContext(),"Timeout", Toast.LENGTH_SHORT);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            mAuthTask = null;
            loginDialog.dismiss();
            if (status) {
                sessionManagement.createLoginSession(user_id,user_name,token.substring(1,token.length()-1));
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
