package com.example.controller;

import com.example.Constants;
import com.example.JWTFactory;
import com.example.model.Log;
import com.example.service.EventService;
import com.example.service.EventServiceImpl;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.NotAuthorizedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Date;

/**
 * Created by Szymon on 20.05.2017.
 */
@RestController
@RequestMapping("api/user")
public class UserController {

    private EventService eventService = new EventServiceImpl();
    private JWTFactory jwtFactory = new JWTFactory();

    @Value("${secret.key.bl}")
    private String secret_key_bl;

    // -------------------Get User data---------------------------------------------
    @ApiOperation(value = "Get User data", response = ResponseEntity.class, notes = "Get User data", nickname = "get_user_data")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 404, message = "User Not Found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "User token", required = true, dataType = "string", paramType = "body")
    })
    @RequestMapping(value = "/data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEventById(@RequestBody String access_token) {
        try {
            eventService.setHttpHeaders(jwtFactory.createHS256Token("BL", "Get Events", secret_key_bl));
            String result = eventService.getUserData(access_token);
            eventService.sendLog(new Log(0, new Date().toString(), Constants.LOGS_GET_USER_DATA, Constants.BUSINESS_LOGIC));
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        } catch (NotAuthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
        }
    }
}
