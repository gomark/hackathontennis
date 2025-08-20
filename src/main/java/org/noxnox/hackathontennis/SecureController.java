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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
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
            
            logger.info(b.toString());
            if ( (b.getBookedHour() == hh) && (b.getCourt() == court) ) {
                logger.info("matched!");
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
                logger.info("checking hh=" + String.valueOf(i) + ", court=" + court + ", bookedDate=" + bookingDate.toString());
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
