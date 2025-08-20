package org.noxnox.hackathontennis.service;

import org.noxnox.hackathontennis.entity.Court;
import org.noxnox.hackathontennis.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourtService {
    
    @Autowired
    private CourtRepository courtRepository;
    
    public List<Court> findAll() {
        return courtRepository.findAll();
    }
    
    public List<Court> findAllOrderByCreatedAtDesc() {
        return courtRepository.findAllOrderByCreatedAtDesc();
    }
    
    public Optional<Court> findById(Long id) {
        return courtRepository.findById(id);
    }
    
    public Optional<Court> findByCourtName(String courtName) {
        return courtRepository.findByCourtName(courtName);
    }
    
    public List<Court> findByCourtNameContainingIgnoreCase(String courtName) {
        return courtRepository.findByCourtNameContainingIgnoreCase(courtName);
    }
    
    public List<Court> searchCourts(String searchTerm) {
        return courtRepository.findByCourtNameOrDescriptionContaining(searchTerm, searchTerm);
    }
    
    public Court createCourt(String courtName, String courtDesc) {
        Court court = new Court(courtName, courtDesc);
        return courtRepository.save(court);
    }
    
    public Court save(Court court) {
        return courtRepository.save(court);
    }
    
    public Court updateCourt(Long id, String courtName, String courtDesc) {
        Optional<Court> existingCourt = courtRepository.findById(id);
        if (existingCourt.isPresent()) {
            Court court = existingCourt.get();
            if (courtName != null) {
                court.setCourtName(courtName);
            }
            if (courtDesc != null) {
                court.setCourtDesc(courtDesc);
            }
            court.setUpdatedAt(OffsetDateTime.now());
            return courtRepository.save(court);
        }
        throw new RuntimeException("Court not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        courtRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return courtRepository.existsById(id);
    }
    
    public boolean existsByCourtName(String courtName) {
        return courtRepository.findByCourtName(courtName).isPresent();
    }
    
    public long count() {
        return courtRepository.count();
    }
}