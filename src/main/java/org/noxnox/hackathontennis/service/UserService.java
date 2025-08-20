package org.noxnox.hackathontennis.service;

import org.noxnox.hackathontennis.entity.User;
import org.noxnox.hackathontennis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByGoogleUid(String googleUid) {
        return userRepository.findByGoogleUid(googleUid);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByGoogleUidAndTenantId(String googleUid, String tenantId) {
        return userRepository.findByGoogleUidAndTenantId(googleUid, tenantId);
    }
    
    public Optional<User> findByEmailAndTenantId(String email, String tenantId) {
        return userRepository.findByEmailAndTenantId(email, tenantId);
    }
    
    public User createUser(String googleUid, String email, String displayName, String tenantId) {
        User user = new User(googleUid, email, displayName, tenantId);
        return userRepository.save(user);
    }
    
    public User createUser(String googleUid, String email, String displayName, String tenantId, String provider) {
        User user = new User(googleUid, email, displayName, tenantId);
        return userRepository.save(user);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, String email, String displayName) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (email != null) {
                user.setEmail(email);
            }
            if (displayName != null) {
                user.setDisplayName(displayName);
            }
            user.setUpdatedAt(OffsetDateTime.now());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    public boolean existsByGoogleUid(String googleUid) {
        return userRepository.findByGoogleUid(googleUid).isPresent();
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    
    public long count() {
        return userRepository.count();
    }
}