package org.noxnox.hackathontennis.repository;

import org.noxnox.hackathontennis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByGoogleUid(String googleUid);
    
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.googleUid = :googleUid AND u.tenantId = :tenantId")
    Optional<User> findByGoogleUidAndTenantId(@Param("googleUid") String googleUid, @Param("tenantId") String tenantId);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.tenantId = :tenantId")
    Optional<User> findByEmailAndTenantId(@Param("email") String email, @Param("tenantId") String tenantId);
}