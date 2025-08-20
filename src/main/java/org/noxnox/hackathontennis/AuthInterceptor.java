package org.noxnox.hackathontennis;

import java.util.List; 
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.multitenancy.TenantAwareFirebaseAuth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class AuthInterceptor implements HandlerInterceptor {
    protected Log logger = LogFactory.getLog(AuthInterceptor.class);
    
    @Autowired
    private TenantAwareFirebaseAuth auth;

    public AuthInterceptor() {

    }

    private String getProvider(FirebaseToken decodedToken) {
        Object firebaseObj = decodedToken.getClaims().get("firebase");
        if (firebaseObj instanceof Map) {
            Map<?, ?> firebaseMap = (Map<?, ?>) firebaseObj;
            Object providerObj = firebaseMap.get("sign_in_provider");
            if (providerObj != null) {
                String signInProvider = providerObj.toString();
                return(signInProvider);
            }
        }

        return(null);
    }    

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("doIntercept");
        logger.info("uri=" + request.getRequestURI());
        
        // Allow OPTIONS requests (CORS preflight) to pass through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        try {
            String authHeader = (request.getHeader("authorization") != null)
            ? request.getHeader("authorization")
            : request.getHeader("Authorization");
            if (authHeader != null) {
                String idToken = authHeader.split(" ")[1];
                FirebaseToken decodedToken = this.auth.verifyIdToken(idToken);

                request.setAttribute("decodedToken", decodedToken);
                logger.info("here1");

                long userPk;
                /*
                Optional<User> user = this.userService.findByEmail(decodedToken.getEmail());
                if (user.isPresent() == false) {
                    User newUser = this.userService.createUser(decodedToken.getUid(), decodedToken.getEmail(), decodedToken.getName(), decodedToken.getTenantId(), this.getProvider(decodedToken));
                    logger.info("not found this user, inserted to user table");
                    request.setAttribute("user_pk", newUser.getId());
                    userPk = newUser.getId();
                } else {
                    userPk = user.get().getId();
                    request.setAttribute("user_pk", userPk);
                }

                // Then, check subscriptions. if not exist, start with free trial
                List<Subscription> l = this.subscriptionService.findActiveSubscriptionsByUser(userPk);
                if (l.isEmpty()) {
                    // Create free subscription package
                    Package freeTrialPackage = this.packageService.findById(2L).orElse(null);
                    this.subscriptionService.createSubscription(userPk, freeTrialPackage.getPrimaryKey());
                    logger.info("not found subscription, create the new one");
                } else {
                    logger.info("found subscription.");
                }
                */
            } else {

                response.setStatus(401);
                response.setContentType("text/html; charset=UTF-8");
                response.getOutputStream().write("Unauthorized".getBytes());
                logger.info("here2");
                return(false);                
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(401);
            response.setContentType("text/html; charset=UTF-8");
            response.getOutputStream().write("Unauthorized exception".getBytes());
            logger.info("here3");
            return(false);
        }


        return(true);
    }     
}
