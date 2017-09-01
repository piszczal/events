package com.example.szymon.events_mobile.Service;

import com.example.szymon.events_mobile.Model.Comment;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.Model.Subscription;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.Model.User;

import org.json.JSONException;
import org.springframework.http.HttpAuthentication;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Szymon on 01.05.2017.
 */

public interface RestClient {

    RestTemplate getRestTemplate();
    void setRestTemplate(RestTemplate restTemplate);
    void setAuthentication(HttpAuthentication auth);
    void setBearerAuth(String token);

    String authorizationTwitter(String user_token, String token_secret);

    User getUserData(Token token);
    List<Event> getEvents(String sort_type);
    Event getEvent(Integer eventID);
    List<Comment> getComments(Integer eventID);
    void sendComment(Comment comment, Integer eventID);

    List<Ticket> getTickets(Token token);

    String sendTicket(Integer event_id, Token token);

    void deleteTicket(Integer ticket_id);

    void subscriptionEvent(Integer event_pk, String sub_type, Token token);

    List<Subscription> getSubscriptions(Token token);
}
