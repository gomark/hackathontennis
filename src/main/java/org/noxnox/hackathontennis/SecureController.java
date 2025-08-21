package org.noxnox.hackathontennis;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.noxnox.hackathontennis.entity.Booking;
import org.noxnox.hackathontennis.entity.Court;
import org.noxnox.hackathontennis.service.BookingService;
import org.noxnox.hackathontennis.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;



@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "http://localhost:8080", "https://hackathontennis-1022559513291.asia-southeast1.run.app/"})
@RestController
@RequestMapping("/api")
public class SecureController {
    protected Log logger = LogFactory.getLog(SecureController.class);

    @Autowired
    private CourtService courtService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/hello")
    public String hello() {
        return("hello world of api");
    }

    private Booking getBooked(List<Booking> bookings, int hh, long court) {
        for (Booking b: bookings) {
            
            
            if ( (b.getBookedHour() == hh) && (b.getCourt() == court) ) {
                logger.info("matched!");
                logger.info(b.toString());
                return(b);
            }
        }
            
        return(null);
    }

    @GetMapping("/timeslots")
    public ResponseEntity<String> getTimeSlots(HttpServletRequest request, @RequestParam String date, @RequestParam String court) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        try {
            long userPk = (Long) request.getAttribute("user_pk");
            // Convert string parameters to proper types
            LocalDate bookingDate = LocalDate.parse(date);
            Long courtId = Long.parseLong(court);
            
            // Get booked hours for this date and court
            List<Booking> bookedHours = this.bookingService.findByBookedDateAndCourt(bookingDate, courtId);
            
            JsonArray ja = new JsonArray();
            int i;
            for (i = 6; i < 22; i++) {
                //logger.info("checking hh=" + String.valueOf(i) + ", court=" + court + ", bookedDate=" + bookingDate.toString());
                JsonObject jSlot = new JsonObject();
                jSlot.addProperty("id", String.valueOf(i));
                
                
                String status = "available";
                Booking b = this.getBooked(bookedHours, i, Long.valueOf(court));
                if (bookedHours != null) {
                    if (b != null) {
                        if (b.getUserId() == userPk) {
                            status = "booked-you";
                        } else if (b.getUserId() != userPk) {
                            status = "booked-other";
                        } else {
                            logger.info("else??");
                        }
                    }
                }

                jSlot.addProperty("status", status);

                String hh = null;
                if (i < 13) {
                    hh = String.valueOf(i) + ":00 AM";
                    if (i < 10) hh = "0" + hh;
                } else if (i >= 13) {
                    hh = String.valueOf(i-12) + ":00 PM";
                    if ( (i-12) < 10) hh = "0" + hh;
                } else if (i == 12) {
                    hh = "12:00 PM";
                }

                jSlot.addProperty("time", hh);

                ja.add(jSlot);
                
            }

            returnJson.add("timeSlots", gson.toJsonTree(ja));
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
    

    @GetMapping("/courts")
    public ResponseEntity<String> getCourts() {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();
        
        try {
            List<Court> courts = this.courtService.findAll();
            returnJson.add("courts", gson.toJsonTree(courts));
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
        return re;
    }
    

    @PostMapping("/bookings")
    public ResponseEntity<String> doBooking(HttpServletRequest request, @RequestBody String entity) {
        logger.info("secured booking...");
        logger.info(entity);
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        try {
            long userPk = (Long) request.getAttribute("user_pk");
            JsonObject jsonEntity = gson.fromJson(entity, JsonObject.class);
            Long courtId = jsonEntity.get("courtId").getAsLong();
            String bookingDateStt = jsonEntity.get("date").getAsString();
            LocalDate bookedDate = LocalDate.parse(bookingDateStt);
            JsonArray jaTimeSlots = jsonEntity.get("timeSlots").getAsJsonArray();

            // For loop for jaTimeSlots to get Long value
            for (int i = 0; i < jaTimeSlots.size(); i++) {
                Long bookedHour = jaTimeSlots.get(i).getAsLong();
                
                Booking booking = new Booking(courtId, userPk, bookedHour.intValue(), bookedDate);
                this.bookingService.save(booking);
            }
            
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

    @GetMapping("/getUserId")
    public ResponseEntity<String> getUserId(HttpServletRequest request) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        try {
            long userPk = (Long) request.getAttribute("user_pk");

            returnJson.addProperty("userId", userPk);
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
        }

        return(re);
    }

    @PostMapping("/createAgentSession")
    public ResponseEntity<String> createAgentSession(HttpServletRequest request, @RequestBody String entity) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        logger.info("createAgentSession:entity=" + entity);
        
        try {
            
            long userPk = (Long) request.getAttribute("user_pk");
            String sessionId = "sessions_" + String.valueOf(userPk);
            String url = System.getenv("HKT_AGENT_URL") + "/apps/" + System.getenv("AGENT_APP_NAME") + "/users/" + String.valueOf(userPk) + "/sessions/" + sessionId;

            // Post to {url} with payload '{}'
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> httpEntity = new HttpEntity<>(entity, headers);

            // 
            ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
            logger.info("Agent Create Session Response: " + response.getBody());            
            // Log the result of POST


            returnJson.addProperty("status", "OK");
            re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
            logger.info("createAgentSession success with " + entity);
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

        return re;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> doChat(HttpServletRequest request, @RequestBody String entity) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        logger.info("doChat: " + entity);
        
        try {
            JsonObject jsonEntity = gson.fromJson(entity, JsonObject.class);
            String message = jsonEntity.get("message").getAsString();
            long userPk = (Long) request.getAttribute("user_pk");
            String sessionId = "sessions_" + String.valueOf(userPk);

            JsonObject jPayload = new JsonObject();
            jPayload.addProperty("appName", System.getenv("AGENT_APP_NAME"));
            jPayload.addProperty("userId", String.valueOf(userPk));
            jPayload.addProperty("sessionId", sessionId);

            JsonObject jMessage = new JsonObject();
            JsonArray jaParts = new JsonArray();
            JsonObject jPart = new JsonObject();
            jPart.addProperty("text", message);
            jaParts.add(jPart);
            jMessage.add("parts", jaParts);
            jMessage.addProperty("role", "user");
            jPayload.add("newMessage", jMessage);
            jPayload.addProperty("streaming", false);

            JsonObject stateDelta = new JsonObject();
            stateDelta.add("additionalProp1", new JsonObject());
            //jPayload.add("stateDelta", stateDelta);

            String jsonString = gson.toJson(jPayload);
            String url = System.getenv("HKT_AGENT_URL") + "/run";
            logger.info("posting: " + jsonString + " to:" + url);
            // Prepare HTTP POST
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> req = new HttpEntity<>(jsonString, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, req, String.class);
            logger.info("response:" + response.getBody());

            JsonArray rootArray = JsonParser.parseString(response.getBody()).getAsJsonArray();
            StringBuilder sb = new StringBuilder();

            for (JsonElement rootElem : rootArray) {
                JsonObject rootObj = rootElem.getAsJsonObject();
                if (rootObj.has("content")) {
                    JsonObject content = rootObj.getAsJsonObject("content");
                    if (content.has("parts")) {
                        JsonArray parts = content.getAsJsonArray("parts");
                        for (JsonElement partElem : parts) {
                            JsonObject partObj = partElem.getAsJsonObject();
                            if (partObj.has("text")) {
                                sb.append(partObj.get("text").getAsString());
                            }
                        }
                    }
                }
            }
            String allText = sb.toString();
            logger.info("allText=" + allText);

            returnJson.addProperty("response", allText);

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


        return re;
    }
    
    @GetMapping("/deleteAgentSession")
    public ResponseEntity<String> deleteAgentSession(HttpServletRequest request) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        logger.info("deleteAgentSession..");

        try {
            long userPk = (Long) request.getAttribute("user_pk");
            String sessionId = "sessions_" + String.valueOf(userPk);
            String url = System.getenv("HKT_AGENT_URL") + "/apps/" + System.getenv("AGENT_APP_NAME") + "/users/" + String.valueOf(userPk) + "/sessions/" + sessionId;

            // implement HTTP delete to {url}
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(url);
            logger.info("Agent Session Deleted: " + sessionId);
            
            returnJson.addProperty("found", true);
            returnJson.addProperty("status", "OK");            
            re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
            logger.info("deleteAgentSession.. done");       
        } catch (Exception e) {
            returnJson.addProperty("found", false);

            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException he = (HttpClientErrorException) e;
                if (he.getStatusCode().value() == 404) {
                    
                    re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
                    return(re);                
                }
            }

            if (e instanceof HttpServerErrorException) {
                HttpServerErrorException he = (HttpServerErrorException) e;
                logger.info(he.getStatusCode().value());
                if (he.getStatusCode().value() == 404) {
                    
                    re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.NOT_FOUND);
                    return(re);                
                }

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
    
    

    @GetMapping("/checkAgentSession")
    public ResponseEntity<String> checkAgentSession(HttpServletRequest request) {
        Gson gson = new Gson();
        gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter()).create();
        ResponseEntity<String> re = null;
        JsonObject returnJson = new JsonObject();

        try {
            long userPk = (Long) request.getAttribute("user_pk");
            String sessionId = "sessions_" + String.valueOf(userPk);
            String url = System.getenv("HKT_AGENT_URL") + "/apps/" + System.getenv("AGENT_APP_NAME") + "/users/" + String.valueOf(userPk) + "/sessions/" + sessionId;

            // perform HTTP GET to {url}
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Make the GET request and check HTTP status
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            logger.info("checkAgentStatus HTTP=" + response.getStatusCode().value());

            if (response.getStatusCode().is2xxSuccessful()) {
                returnJson.addProperty("found", true);
            } else {
                returnJson.addProperty("found", false);
            }
            
            // and log the response payload
            // logger.info("Agent Session Check Response: " + response.getBody());            
            

            returnJson.addProperty("status", "OK");            
            re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
        } catch (Exception e) {
            returnJson.addProperty("found", false);

            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException he = (HttpClientErrorException) e;
                if (he.getStatusCode().value() == 404) {
                    
                    re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.OK);
                    return(re);                
                }
            }

            if (e instanceof HttpServerErrorException) {
                HttpServerErrorException he = (HttpServerErrorException) e;
                logger.info(he.getStatusCode().value());
                if (he.getStatusCode().value() == 404) {
                    
                    re = new ResponseEntity<String>(gson.toJson(returnJson), HttpStatus.NOT_FOUND);
                    return(re);                
                }

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
