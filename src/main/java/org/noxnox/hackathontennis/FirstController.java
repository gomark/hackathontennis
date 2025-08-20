package org.noxnox.hackathontennis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/first")
public class FirstController {
    protected Log logger = LogFactory.getLog(FirstController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String hello() {
        return("hello world");
    }

    @GetMapping("/testDB2")
    public String testDB2() {
        
        String count = this.jdbcTemplate.queryForObject("select 2", String.class);
        logger.info(count);

        return(count);
    }    
    
}
