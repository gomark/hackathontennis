package org.noxnox.hackathontennis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.multitenancy.TenantAwareFirebaseAuth;

@SpringBootApplication
public class HackathontennisApplication {

	public static void main(String[] args) {
		try {
			String projectId = System.getenv("GOOGLE_CLOUD_PROJECT");
			GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
			FirebaseOptions options = FirebaseOptions.builder().setProjectId(projectId).setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);

			SpringApplication.run(HackathontennisApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Bean
	public TenantAwareFirebaseAuth firebaseAuth() {
		String tenantId = System.getenv("tenantId");
		FirebaseAuth auth = FirebaseAuth.getInstance();
		
		if (tenantId != null && !tenantId.isEmpty()) {
			return auth.getTenantManager().getAuthForTenant(tenantId);
		} else {
			return(null);
		}
		
		
	}	

}
