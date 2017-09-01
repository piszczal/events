package com.example.controller;

import com.example.Constants;
import com.example.JWTFactory;
import com.example.model.Log;
import com.example.model.User;
import com.example.service.EventService;
import com.example.service.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Szymon on 06.04.2017.
 */
@RestController
@RequestMapping("api/authorization")
public class AuthorizationController {

    private EventService eventService = new EventServiceImpl();
    @Value("${secret.key.bl}")
    private String secret_key_bl;

    private JWTFactory jwtFactory = new JWTFactory();

    // -------------------Check user authorization (twitter) ------------------------------------------
    @ApiOperation(value = "Authorization Twitter", notes = "User Authorization for Twitter and get token from db", nickname = "authorization_twitter")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "No Authorization", response = String.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_token", value = "User token from twitter", required = true, dataType = "string", paramType = "param", defaultValue = "null"),
            @ApiImplicitParam(name = "token_secret", value = "User token secret from twitter", required = true, dataType = "string", paramType = "param", defaultValue = "null")
    })
    @RequestMapping(value = "/twitter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity authorizationTwitter(@RequestParam String user_token, @RequestParam String token_secret) {
        try {
            String module_token = jwtFactory.createHS256Token(Constants.LOGS_LOGIN, "Login Twitter", secret_key_bl);
            String access_token = eventService.twitterAuthorization(user_token, token_secret, module_token);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(access_token);
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public ResponseEntity getToken() {
        String token = jwtFactory.createHS256Token("DB", "LOG", secret_key_bl);
        //jwtModule.parseJWT(token,secret_key_db);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<User> request = new HttpEntity<User>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://quiet-meadow-18469.herokuapp.com/api/test_bl/", HttpMethod.GET, request, String.class);
        return ResponseEntity.ok(token);
    }
}
