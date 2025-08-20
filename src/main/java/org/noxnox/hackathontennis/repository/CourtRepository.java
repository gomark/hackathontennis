package org.noxnox.hackathontennis.repository;

import org.noxnox.hackathontennis.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    
    Optional<Court> findByCourtName(String courtName);
    
    List<Court> findByCourtNameContainingIgnoreCase(String courtName);
    
    @Query("SELECT c FROM Court c WHERE c.courtName LIKE %:name% OR c.courtDesc LIKE %:description%")
    List<Court> findByCourtNameOrDescriptionContaining(@Param("name") String name, @Param("description") String description);
    
    @Query("SELECT c FROM Court c ORDER BY c.createdAt DESC")
    List<Court> findAllOrderByCreatedAtDesc();
}