package org.noxnox.hackathontennis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "booking", 
       uniqueConstraints = {
           @UniqueConstraint(name = "booking_unique", columnNames = {"booked_date", "booked_hour", "court"})
       })
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primarykey")
    private Long primaryKey;
    
    @NotNull
    @Column(name = "court", nullable = false)
    private Long court;
    
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @NotNull
    @Column(name = "booked_hour", nullable = false)
    private Integer bookedHour;
    
    @NotNull
    @Column(name = "booked_date", nullable = false)
    private LocalDate bookedDate;
    
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
    
    public Booking() {}
    
    public Booking(Long court, Long userId, Integer bookedHour, LocalDate bookedDate) {
        this.court = court;
        this.userId = userId;
        this.bookedHour = bookedHour;
        this.bookedDate = bookedDate;
    }
    
    public Long getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public Long getCourt() {
        return court;
    }
    
    public void setCourt(Long court) {
        this.court = court;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getBookedHour() {
        return bookedHour;
    }
    
    public void setBookedHour(Integer bookedHour) {
        this.bookedHour = bookedHour;
    }
    
    public LocalDate getBookedDate() {
        return bookedDate;
    }
    
    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate = bookedDate;
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
    
    @Override
    public String toString() {
        return "Booking{" +
                "primaryKey=" + primaryKey +
                ", court=" + court +
                ", userId=" + userId +
                ", bookedHour=" + bookedHour +
                ", bookedDate=" + bookedDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}