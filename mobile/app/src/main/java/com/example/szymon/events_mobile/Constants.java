package com.example.szymon.events_mobile;

/**
 * Created by Szymon on 01.05.2017.
 */

public class Constants {

    public final static String ADDRESS_URL = "https://peaceful-caverns-69997.herokuapp.com/";
    public final static String AUTHORIZATION = "api/authorization/twitter";
    public final static String GET_SORT_EVENTS = "api/events/sort_by/{sort_type}";
    public final static String GET_COMMENTS = "api/events/{event_pk}/comments";
    public final static String GET_EVENT = "api/events/{id}";
    public final static String POST_COMMENTS = "api/events/{id}/comments";
    public final static String GET_TICKETS = "api/events/tickets";
    public final static String DELETE_TICKETS = "api/events/{ticket_pk}/ticket";
    public final static String SEND_TICKETS = "api/events/{event_id}/ticket";
    public final static String GET_USER_DATA = "api/user/data";
    public final static String GET_SUBSCRIPTION_HISTORY = "api/events/subscription/history";
    public final static String POST_SUBSCRIPTION = "api/events/{event_pk}/subscription/{sub_type}";

    public final static String EVENTS_NEW_INSTANCE = "events_list_instance";
    public final static String TICKET_BUNDLE = "ticket";
    public final static String TICKET_INSTANCE = "ticket_instance";
    public final static String EVENT_STATE = "event_state";
    public final static String COMMENTS_STATE = "comments_state";
    public final static String USER_TOKEN_STATE = "user_token_state";

    public final static String EVENT_ID = "event_id";
    public final static String USER_TOKEN = "user_token";
}
