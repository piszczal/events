package com.example.service;

import com.example.Constants;
import com.example.model.*;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.client.RestTemplate;
import sun.net.www.protocol.http.HttpAuthenticator;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Szymon on 26.03.2017.
 */
public class EventServiceImpl implements EventService {

    private RestTemplate restTemplate;
    private String rootUrl;
    private HttpHeaders httpHeaders;

    public EventServiceImpl(){
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
        rootUrl = Constants.URL_DB;
        restTemplate.getMessageConverters();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void setHttpHeaders(String token) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + token);
    }

    @Override
    public String twitterAuthorization(String user_token, String secret_token, String token_module) {
        TwitterTemplate twitterTemplate = new TwitterTemplate(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, user_token, secret_token);
        restTemplate = twitterTemplate.getRestTemplate();
        User userProfile = restTemplate.getForObject(Constants.AUTH_TWITTER_EMAIL, User.class);
        userProfile.setAuthorizationToken(user_token, secret_token);
        setHttpHeaders(token_module);
        restTemplate = new RestTemplate();
        sendLog(new Log(0, new Date().toString(), Constants.LOGS_LOGIN + userProfile.getScreen_name(), Constants.BUSINESS_LOGIC));
        HttpEntity<User> request = new HttpEntity<User>(userProfile, httpHeaders);
        return restTemplate.postForObject(Constants.URL_DB.concat(Constants.DB_POST_REGISTER), request, String.class);
    }

    @Override
    public void verifyUserToken(String token) {
        HttpEntity<String> request = new HttpEntity<String>(token, httpHeaders);
        restTemplate.postForObject(rootUrl.concat(Constants.DB_POST_VERIFY_TOKEN), request, String.class);
    }

    @Override
    public String getUserData(String token) {
        HttpEntity<String> request = new HttpEntity<String>(token, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.DB_GET_USER_DATA), request, String.class);
    }

    @Override
    public String getEvents(){
        HttpEntity<String> request = new HttpEntity<String>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat(Constants.DB_GET_EVENTS), HttpMethod.GET, request, String.class).getBody();
    }

    @Override
    public Event getEventById(Integer id){
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("id", id);
        HttpEntity<String> request = new HttpEntity<String>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat(Constants.DB_GET_EVENT), HttpMethod.GET, request, Event.class, urlVariables).getBody();
    }

    @Override
    public List<Comment> getComments(Integer id) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("id", id);
        HttpEntity<String> request = new HttpEntity<String>(httpHeaders);
        return restTemplate.exchange(rootUrl.concat(Constants.DB_GET_COMMENTS), HttpMethod.GET, request, List.class, urlVariables).getBody();
    }

    @Override
    public String addComment(Integer id, Comment comment) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("id",id);
        HttpEntity<Comment> request = new HttpEntity<Comment>(comment, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.DB_POST_COMMENT), request, String.class, urlVariables);
    }

    @Override
    public String addTicket(Integer eventId, String access_token) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("event_pk", eventId);
        HttpEntity<String> request = new HttpEntity<String>(access_token, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.DB_POST_TICKET), request, String.class, urlVariables);
    }

    @Override
    public String getTickets(String access_token) {
        HttpEntity<String> request = new HttpEntity<String>(access_token, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.DB_GET_TICKETS), request, String.class);
    }

    @Override
    public void deleteTicket(Integer id) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("ticket_pk", id);
        HttpEntity<String> request = new HttpEntity<String>(httpHeaders);
        restTemplate.exchange(Constants.URL_DB.concat(Constants.DB_DELETE_TICKET), HttpMethod.DELETE, request, String.class, urlVariables).getBody();
    }

    @Override
    public void addSubscription(String event_pk, String sub_type, String body) {
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("event_pk", event_pk);
        urlVariables.put("sub_type", sub_type);
        HttpEntity<String> request = new HttpEntity<String>(body, httpHeaders);
        restTemplate.postForObject(Constants.URL_DB.concat(Constants.DB_ADD_SUBSCRIPTION), request, String.class, urlVariables);
    }

    @Override
    public String getSubscriptionHistory(String access_token) {
        HttpEntity<String> request = new HttpEntity<String>(access_token, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.DB_GET_SUBSCRIPTION_HISTORY), request, String.class);
    }

    @Override
    public String sendLog(Log log) {
        HttpEntity<Log> request = new HttpEntity<Log>(log, httpHeaders);
        return restTemplate.postForObject(Constants.URL_LOGS.concat(Constants.LOGS_POST_INFO), request, String.class);
    }
}
