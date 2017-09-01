package com.example.controller;

import com.example.Constants;
import com.example.JWTFactory;
import com.example.model.*;
import com.example.service.EventService;
import com.example.service.EventServiceImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.swagger.annotations.*;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.*;
import org.springframework.social.NotAuthorizedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Szymon on 20.03.2017.
 */
@RestController
@RequestMapping("api/events")
public class EventsController {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private EventService eventService = new EventServiceImpl();
    private JWTFactory jwtFactory = new JWTFactory();

    @Autowired
    public EmailService emailService;

    @Value("${secret.key.bl}")
    private String secret_key_bl;

    // -------------------Get All Events and sort by type----------------------------
    @ApiOperation(value = "Get all events", notes = "Get all sort events of the app", nickname = "get_sort_list_events")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sort_type", value = "Sort type title/description/tickets/date", required = true, dataType = "string", paramType = "path", defaultValue = "null")
    })
    @RequestMapping(value = "/sort_by/{sort_type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllEvents(@PathVariable("sort_type") String sort_type) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.getEvents();
            List<Event> events = MAPPER.readValue(result, new TypeReference<List<Event>>() {
            });
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_EVENTS + sort_type, Constants.BUSINESS_LOGIC));
            switch (sort_type) {
                case "title":
                    Collections.sort(events, Event.eventTitleComparator);
                    break;
                case "description":
                    Collections.sort(events, Event.eventDescriptionComparator);
                    break;
                case "tickets":
                    Collections.sort(events, Event.eventTicketsComparator);
                    break;
                case "date":
                    Collections.sort(events, Event.eventDateComparator);
                    break;
                default:
                    break;
            }
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(MAPPER.writeValueAsString(events));
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Get Event By Id---------------------------------------------
    @ApiOperation(value = "Get event by ID", response = ResponseEntity.class, notes = "Get some event of the app", nickname = "get_event")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Event.class),
            @ApiResponse(code = 404, message = "Event Not Found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Event's id", required = true, dataType = "int", paramType = "path", defaultValue = "1")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEventById(@PathVariable("id") int id) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            Event event = eventService.getEventById(id);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_EVENT_ID + id, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(event);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Get comments for Event-----------------------------------------------
    @ApiOperation(value = "Get comments for Event", response = List.class, notes = "Get comments for Event", nickname = "get_list_comments")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Event.class),
            @ApiResponse(code = 404, message = "Event Not Found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Event's id", required = true, dataType = "int", paramType = "path", defaultValue = "1")
    })
    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<?> getCommentsForEvent(@PathVariable("id") Integer id) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            List<Comment> comments = eventService.getComments(id);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_COMMENTS + id, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(comments);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Add comment for Event-----------------------------------------------
    @ApiOperation(value = "Add comment for Event", response = String.class, notes = "Add new comment", nickname = "post_comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Comment.class),
            @ApiResponse(code = 404, message = "Event Not Found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Event's id", required = true, dataType = "int", paramType = "path", defaultValue = "1"),
            @ApiImplicitParam(name = "comment", value = "Comment object", required = true, dataType = "comment", paramType = "body")
    })
    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCommentForEvent(@PathVariable("id") Integer id, @RequestBody Comment comment) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.addComment(id, comment);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_ADD_COMMENT_ID + id, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Get tickets for User-----------------------------------------------
    @ApiOperation(value = "Get ticket user", response = String.class, notes = "Get ticket user", nickname = "get_tickets")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParam(name = "access_token", value = "String object", required = true, dataType = "string", paramType = "body")
    @RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTickets(@RequestBody String access_token) throws JSONException, IOException {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.getTickets(access_token);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonTicket = jsonObject.getJSONArray("tickets_lists");
            List<Ticket> ticketsList = MAPPER.readValue(jsonTicket.toString(), new TypeReference<List<Ticket>>() {
            });
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_TICKETS, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(ticketsList);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Add event ticket for User-----------------------------------------------
    @ApiOperation(value = "Add ticket event", response = String.class, notes = "Add new ticket", nickname = "post_ticket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "event_id", value = "Event id", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "token", value = "User token", required = true, dataType = "string", paramType = "body")}
    )
    @RequestMapping(value = "/{id}/ticket", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTicket(@PathVariable("id") Integer eventID, @RequestBody String token) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.addTicket(eventID, token);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_ADD_TICKET + eventID, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Delete event ticket for User-----------------------------------------------
    @ApiOperation(value = "Delete ticket event", response = String.class, notes = "Delete new ticket", nickname = "delete_ticket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Ticket's id", required = true, dataType = "int", paramType = "path", defaultValue = "1"),
    })
    @RequestMapping(value = "/{ticket_pk}/ticket", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTicket(@PathVariable("ticket_pk") Integer ticket_id) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            eventService.deleteTicket(ticket_id);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_DELETE_TICKET + ticket_id, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("DELETE");
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Add subscription to user  ----------------------------------------------------------
    @ApiOperation(value = "Add subscription to user", response = String.class, notes = "Add subscription to user", nickname = "add_subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "Event id", required = true, dataType = "string", paramType = "path", defaultValue = "0"),
            @ApiImplicitParam(name = "type", value = "Sub type", required = true, dataType = "string", paramType = "path", defaultValue = "null"),
            @ApiImplicitParam(name = "body", value = "User token", required = true, dataType = "string", paramType = "body", defaultValue = "null")
    })
    @RequestMapping(value = "/{event_pk}/subscription/{sub_type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSubscription(@PathVariable("event_pk") String event_pk, @PathVariable("sub_type") String sub_type, @RequestBody String body) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            eventService.addSubscription(event_pk, sub_type, body);
            eventService.sendLog(new Log(0, new Date().toString(), "Add new subscription", Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("SUCCESS");
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    // -------------------Get subscription history for User-----------------------------------------------
    @ApiOperation(value = "Get subscription history for user", response = String.class, notes = "Get ticket user", nickname = "get_sub_history")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParam(name = "access_token", value = "String object", required = true, dataType = "string", paramType = "body")
    @RequestMapping(value = "/subscription/history", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSubscriptionHistory(@RequestBody String access_token) throws JSONException, IOException {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.getSubscriptionHistory(access_token);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonTicket = jsonObject.getJSONArray("subscription_list");
            //List<Ticket> ticketsList = MAPPER.readValue(jsonTicket.toString(), new TypeReference<List<Ticket>>() {
            //});
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_TICKETS, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(jsonTicket.toString());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getResponseBodyAsString());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

}
