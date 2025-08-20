package org.noxnox.hackathontennis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Entity
@Table(name = "courts")
public class Court {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primarykey")
    private Long primaryKey;
    
    @Size(max = 255)
    @Column(name = "court_name")
    private String courtName;
    
    @Size(max = 255)
    @Column(name = "court_desc")
    private String courtDesc;
    
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
    
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    
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
    
    public Court() {}
    
    public Court(String courtName, String courtDesc) {
        this.courtName = courtName;
        this.courtDesc = courtDesc;
    }
    
    public Long getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public String getCourtName() {
        return courtName;
    }
    
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    
    public String getCourtDesc() {
        return courtDesc;
    }
    
    public void setCourtDesc(String courtDesc) {
        this.courtDesc = courtDesc;
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
}