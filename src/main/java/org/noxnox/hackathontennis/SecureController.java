package org.noxnox.hackathontennis;

import java.time.OffsetDateTime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class SecureController {
    protected Log logger = LogFactory.getLog(SecureController.class);

    @GetMapping("/hello")
    public String hello() {
        return("hello world of api");
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> doBooking(@RequestBody String entity) {
        logger.info("secured booking...");
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        try {
            returnJson.addProperty("status", "OK");
            re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
        } catch (Exception e) {
            if (e instanceof HttpServerErrorException) {
                HttpServerErrorException he = (HttpServerErrorException) e;

                returnJson.addProperty("message", he.getResponseBodyAsString());
            } else {
                returnJson.addProperty("message", e.getMessage());
            }

            logger.error(e, e);
            returnJson.addProperty("status", "ERROR");
            
            re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return(re);
    }
    
}
