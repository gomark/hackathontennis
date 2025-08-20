package org.noxnox.hackathontennis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class SecureController {
    protected Log logger = LogFactory.getLog(SecureController.class);

    @GetMapping("/hello")
    public String hello() {
        return("hello world of api");
    }
}
