package com.example.szymon.events_mobile.Fragment;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szymon.events_mobile.Adapter.CommentsListAdapter;
import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Comment;
import com.example.szymon.events_mobile.R;
import com.example.szymon.events_mobile.Service.RestClient;
import com.example.szymon.events_mobile.Service.RestClientImpl;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {

    private ListView listCommentsView;
    private CommentsListAdapter commentsListAdapter;
    private FloatingActionButton fabComment;
    private RestClient restClient;
    private List<Comment> commentsList;
    private HttpRequestTask mAuthTask;
    private Integer event_id;
    private String user_token;
    private EditText commentText;

    public CommentsFragment newInstance(Integer event_id, String user_token) {
        CommentsFragment fragment = new CommentsFragment();
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
            event_id = 0;
            user_token = "";
        } else {
            event_id = getEventID();
            user_token = getUserToken();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        listCommentsView = (ListView) view.findViewById(R.id.comments_list);
        fabComment = (FloatingActionButton) view.findViewById(R.id.fab_comment);

        if (savedInstanceState == null) {
            restClient = new RestClientImpl(getActivity().getApplicationContext());
            mAuthTask = new HttpRequestTask(user_token, event_id, null, 0);
            mAuthTask.execute((Void) null);
        } else {
            commentsList = savedInstanceState.getParcelableArrayList(Constants.COMMENTS_STATE);
            setAdapter();
        }

        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialog();
            }
        });

        return view;
    }

    private void commentDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Wpisz komentarz");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.comment_dialog, null);
        commentText = (EditText) dialogLayout.findViewById(R.id.commentText);

        builder.setPositiveButton("Wyślij", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Comment comment = new Comment(0, commentText.getText().toString(), commentText.getText().toString(), 0, event_id);
                mAuthTask = new HttpRequestTask(user_token, event_id, comment, 1);
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

    private class HttpRequestTask extends AsyncTask<Void, Void, Boolean> {

        String user_token;
        Integer event_id;
        Integer type_request;
        Comment comment;

        HttpRequestTask(String userToken, Integer eventID, Comment comment, Integer typeRequest) {
            this.user_token = userToken;
            this.event_id = eventID;
            this.type_request = typeRequest;
            this.comment = comment;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                restClient.setBearerAuth(user_token);
                if (type_request == 0)
                    commentsList = restClient.getComments(event_id);
                else
                    restClient.sendComment(comment, event_id);
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
                if (type_request == 0) {
                    setAdapter();
                } else {
                    commentsListAdapter.add(comment);
                    commentText.setText("");
                    commentsListAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private void setAdapter() {
        commentsListAdapter = new CommentsListAdapter(getActivity().getApplicationContext(), R.layout.comment_item, commentsList);
        listCommentsView.setAdapter(commentsListAdapter);
    }

    private Integer getEventID() {
        return getArguments().getInt(Constants.EVENT_ID);
    }

    private String getUserToken() {
        return getArguments().getString(Constants.USER_TOKEN);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.COMMENTS_STATE, (ArrayList<? extends Parcelable>) commentsList);
        super.onSaveInstanceState(outState);
    }
}
