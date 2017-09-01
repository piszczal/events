package com.example.service;

import com.example.model.*;

import java.util.List;

/**
 * Created by Szymon on 26.03.2017.
 */
public interface EventService {

    String twitterAuthorization(String user_token, String secret_token, String token_module);
    void verifyUserToken(String token);
    void setHttpHeaders(String token);

    String getUserData(String token);

    String getEvents();
    Event getEventById(Integer id);
    List<Comment> getComments(Integer id);
    String addComment(Integer id, Comment comment);

    String addTicket(Integer eventID, String access_token);

    String getTickets(String access_token);

    void addSubscription(String event_pk, String sub_type, String body);

    String getSubscriptionHistory(String access_token);

    void deleteTicket(Integer id);

    String sendLog(Log log);
}
