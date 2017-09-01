package com.example.szymon.events_mobile.Service;

import android.content.Context;

import com.example.szymon.events_mobile.Constants;
import com.example.szymon.events_mobile.Model.Comment;
import com.example.szymon.events_mobile.Model.Event;
import com.example.szymon.events_mobile.Model.Subscription;
import com.example.szymon.events_mobile.Model.Ticket;
import com.example.szymon.events_mobile.Model.Token;
import com.example.szymon.events_mobile.Model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Szymon on 01.05.2017.
 */

public class RestClientImpl implements RestClient {

    private RestTemplate restTemplate;
    private HttpAuthentication authentication;
    private String rootUrl;

    public RestClientImpl(Context context) {
        restTemplate = new RestTemplate(getClientHttpRequestFactory());
        rootUrl = Constants.ADDRESS_URL;
        restTemplate.getMessageConverters();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setBearerAuth(final String token) {
        this.authentication = new HttpAuthentication() {

            @Override
            public String getHeaderValue() {
                return ("Bearer " + token);
            }
        }
        ;
    }

    @Override
    public void setAuthentication(HttpAuthentication auth) {
        this.authentication = auth;
    }

    @Override
    public String authorizationTwitter(String user_token, String token_secret) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("Content-Type", "application/json");
        map.add("user_token",user_token);
        map.add("token_secret",token_secret);
        return restTemplate.postForObject(rootUrl.concat(Constants.AUTHORIZATION),map,String.class);
    }

    @Override
    public User getUserData(Token token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> request = new HttpEntity<Object>(token, httpHeaders);
        return restTemplate.postForObject(rootUrl.concat(Constants.GET_USER_DATA), request, User.class);
    }

    @Override
    public List<Event> getEvents(String sort_type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("sort_type", sort_type);
        return restTemplate.exchange(rootUrl.concat(Constants.GET_SORT_EVENTS),HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Event>>() {
        },urlVariables).getBody();
    }

    @Override
    public Event getEvent(Integer eventID) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("id", eventID);
        return restTemplate.exchange(rootUrl.concat(Constants.GET_EVENT),HttpMethod.GET, requestEntity, Event.class ,urlVariables).getBody();
    }

    @Override
    public List<Comment> getComments(Integer eventID) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("event_pk", eventID);
        return restTemplate.exchange(rootUrl.concat(Constants.GET_COMMENTS),HttpMethod.GET, requestEntity,new ParameterizedTypeReference<List<Comment>>() {
        },urlVariables).getBody();
    }

    @Override
    public void sendComment(Comment comment, Integer eventID) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(comment,httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("id", eventID);
        restTemplate.exchange(rootUrl.concat(Constants.POST_COMMENTS),HttpMethod.POST, requestEntity, String.class ,urlVariables).getBody();
    }

    @Override
    public List<Ticket> getTickets(Token token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(token, httpHeaders);
        return restTemplate.exchange(rootUrl.concat(Constants.GET_TICKETS), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Ticket>>() {
        }).getBody();
    }

    @Override
    public String sendTicket(Integer event_id, Token token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(token, httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("event_id", event_id);
        return restTemplate.postForObject(rootUrl.concat(Constants.SEND_TICKETS), requestEntity, String.class, urlVariables);
    }

    @Override
    public void deleteTicket(Integer ticket_id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("ticket_pk", ticket_id);
        restTemplate.exchange(rootUrl.concat(Constants.DELETE_TICKETS), HttpMethod.DELETE, requestEntity, String.class, urlVariables);
    }

    @Override
    public void subscriptionEvent(Integer event_pk, String sub_type, Token token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(token, httpHeaders);
        HashMap<String, Object> urlVariables = new HashMap<String, Object>();
        urlVariables.put("event_pk", event_pk);
        urlVariables.put("sub_type", sub_type);
        restTemplate.postForObject(rootUrl.concat(Constants.POST_SUBSCRIPTION), requestEntity, String.class, urlVariables);
    }

    @Override
    public List<Subscription> getSubscriptions(Token token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAuthorization(authentication);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(token, httpHeaders);
        return restTemplate.exchange(rootUrl.concat(Constants.GET_SUBSCRIPTION_HISTORY), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Subscription>>() {
        }).getBody();
    }
}
