package com.example.controller;

import com.example.JWTFactory;
import com.example.model.Conspiration;
import com.example.service.EventService;
import com.example.service.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 30.05.2017.
 */
@RestController
@RequestMapping("api/subscription")
public class SubscriptionController {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private EventService eventService = new EventServiceImpl();
    private JWTFactory jwtFactory = new JWTFactory();

    @Autowired
    public EmailService emailService;

    @Value("${secret.key.bl}")
    private String secret_key_bl;

    // -------------------Get subscriptions list and send email-----------------------------------------------
    @ApiOperation(value = "Get subscriptions list and send email", response = String.class, notes = "Get subscriptions list and send email", nickname = "send_subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "Subscription type", required = true, dataType = "string", paramType = "path", defaultValue = "low"),
            @ApiImplicitParam(name = "list", value = "Conspirations list", required = true, dataType = "Conspirations", paramType = "body", defaultValue = "null")
    })
    @RequestMapping(value = "/{parameter}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendSubscription(@PathVariable("parameter") String parameter, @RequestBody List<Conspiration> conspirations) throws UnsupportedEncodingException {
        String subject = null;
        String body = null;
        List<InternetAddress> internetAddresses = new ArrayList<>();
        for (int i = 0; i < conspirations.size(); i++) {
            internetAddresses.add(new InternetAddress(conspirations.get(i).getEmail(), conspirations.get(i).getScreen_name()));
        }
        if (parameter.equals("low")) {
            subject = "Mała ilość biletów do Twojego wydarzenia";
            body = "Do Twojego zapisanego wydarzenia odnotowaliśmy małą liczbę dostępnych biletów! Spiesz się!";
        }
        if (!parameter.equals("low")) {
            subject = "Dodaliśmy nowe wydarzenie";
            body = "Witaj! Dodaliśmy nowe wydarzenie.";
        }

        final Email email = DefaultEmail.builder()
                .from(new InternetAddress("eventsrsiteam@gmail.com", "RSI EVENTS "))
                .to(internetAddresses)
                .subject(subject)
                .body(body)
                .encoding("UTF-8").build();

        emailService.send(email);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Email was send");
    }
}
