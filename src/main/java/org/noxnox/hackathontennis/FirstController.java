package org.noxnox.hackathontennis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/first")
public class FirstController {
    protected Log logger = LogFactory.getLog(FirstController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String hello() {
        return("hello world of api");
    }

    @GetMapping("/testDB2")
    public String testDB2() {
        
        String count = this.jdbcTemplate.queryForObject("select 2", String.class);
        logger.info(count);

        return(count);
    }

    @GetMapping("/getEnv")
    public ResponseEntity<Map<String, String>> getEnv(@RequestParam("key") String key) {
        String value = System.getenv(key);
        if (value != null) {
            Map<String, String> response = Map.of("key", key, "value", value);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = Map.of("error", "Environment variable '" + key + "' not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/getEnvs")
    public ResponseEntity<Map<String, String>> getEnvs(@RequestParam("keys") String keys) {
        List<String> keyList = Arrays.asList(keys.split(","));
        Map<String, String> response = new HashMap<>();
        
        for (String key : keyList) {
            String trimmedKey = key.trim();
            String value = System.getenv(trimmedKey);
            if (value != null) {
                response.put(trimmedKey, value);
            } else {
                response.put(trimmedKey, null);
            }
        }
        
        return ResponseEntity.ok(response);
    }    
    
}
