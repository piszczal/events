package com.example;

/**
 * Created by Szymon on 20.03.2017.
 */
public class Constants {

    public final static String BUSINESS_LOGIC = "BL";

    //Constants String for DB Model
    public final static String URL_DB = "https://quiet-meadow-18469.herokuapp.com/";
    public final static String DB_GET_USER_DATA = "api/user/data/";
    public final static String DB_GET_EVENTS = "api/events/";
    public final static String DB_GET_EVENT = "api/events/{id}";
    public final static String DB_POST_EVENT = "api/events/";
    public final static String DB_GET_COMMENTS = "api/events/{id}/comments";
    public final static String DB_POST_COMMENT = "api/events/{id}/comments";
    public final static String DB_GET_TICKETS = "api/user/tickets-history/";
    public final static String DB_POST_TICKET = "api/events/{event_pk}/ticket";
    public final static String DB_DELETE_TICKET = "api/tickets/{ticket_pk}";
    public final static String DB_POST_REGISTER = "api/user/register/";
    public final static String DB_POST_VERIFY_TOKEN = "api/verify-token/";
    public final static String DB_ADD_SUBSCRIPTION = "api/events/{event_pk}/subscribe/{sub_type}/";
    public final static String DB_GET_SUBSCRIPTION_HISTORY = "api/user/subscription-history/";

    //Constants String for Authorization Twitter
    public final static String CONSUMER_KEY = "i31qtGLCToSYZ2QBd31QNJcVK";
    public final static String CONSUMER_SECRET = "8yfp9nwPurPlaouTfF0ARwRofLggnC412sEpDEG2oDVBRP6DOu";
    public final static String AUTH_TWITTER_EMAIL = "https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true";

    //Constants String for Logs Model
    public final static String URL_LOGS = "https://protected-oasis-51228.herokuapp.com/";
    public final static String LOGS_POST_INFO = "api/logs/addLog";
    public final static String LOGS_LOGIN = "Login user: ";
    public final static String LOGS_GET_EVENTS = "Get event sort by: ";
    public final static String LOGS_GET_EVENT_ID = "Get event ID: ";
    public final static String LOGS_GET_COMMENTS = "Get comments for event ID: ";
    public final static String LOGS_ADD_COMMENT_ID = "Add comment for event ID: ";
    public final static String LOGS_ADD_TICKET = "Buy ticket for event ID: ";
    public final static String LOGS_GET_TICKETS = "Get tickets: ";
    public final static String LOGS_GET_USER_DATA = "Get user data: ";
    public final static String LOGS_DELETE_TICKET = "Delete ticket ID: ";
}
