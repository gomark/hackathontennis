package org.noxnox.hackathontennis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {

    protected Log logger = LogFactory.getLog(MyConfig.class);

    @Autowired
    private AuthInterceptor authInterceptor;
 
    public MyConfig() {
        super();
        logger.info("MyConfig() is created.");
        
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("addInterceptors() is called.");
        registry.addInterceptor(authInterceptor).addPathPatterns("/secure/**", "/**/secure/**", "/api/**");
        logger.info("added AuthInterceptor for /secure/**");
        //registry.addInterceptor(authInterceptor);
    }


}

