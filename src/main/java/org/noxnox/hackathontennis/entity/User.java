package org.noxnox.hackathontennis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_google_uid", columnList = "google_uid")
})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "google_uid", nullable = false, unique = true)
    private String googleUid;
    
    @Email
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "tenantid", nullable = false)
    private String tenantId;
    
    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
    
    public User() {}
    
    public User(String googleUid, String email, String displayName, String tenantId) {
        this.googleUid = googleUid;
        this.email = email;
        this.displayName = displayName;
        this.tenantId = tenantId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getGoogleUid() {
        return googleUid;
    }
    
    public void setGoogleUid(String googleUid) {
        this.googleUid = googleUid;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", googleUid='" + googleUid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}