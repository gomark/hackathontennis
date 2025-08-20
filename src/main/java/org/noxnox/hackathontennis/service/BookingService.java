package org.noxnox.hackathontennis.service;

import org.noxnox.hackathontennis.entity.Booking;
import org.noxnox.hackathontennis.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }
    
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public List<Booking> findByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public List<Booking> findByCourt(Long court) {
        return bookingRepository.findByCourt(court);
    }
    
    public List<Booking> findByBookedDate(LocalDate bookedDate) {
        return bookingRepository.findByBookedDate(bookedDate);
    }
    
    public List<Booking> findByBookedDateAndCourt(LocalDate bookedDate, Long court) {
        return bookingRepository.findByBookedDateAndCourt(bookedDate, court);
    }
    
    public List<Booking> findByUserIdAndBookedDate(Long userId, LocalDate bookedDate) {
        return bookingRepository.findByUserIdAndBookedDate(userId, bookedDate);
    }
    
    public List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByBookedDateBetween(startDate, endDate);
    }
    
    public List<Booking> findByCourtAndDateRange(Long court, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByCourtAndBookedDateBetween(court, startDate, endDate);
    }
    
    public List<Booking> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByUserIdAndBookedDateBetweenOrderByDateAndHour(userId, startDate, endDate);
    }
    
    public List<Integer> getBookedHoursForDateAndCourt(LocalDate date, Long court) {
        return bookingRepository.findBookedHoursByDateAndCourt(date, court);
    }
    
    public boolean isTimeSlotAvailable(LocalDate bookedDate, Integer bookedHour, Long court) {
        return !bookingRepository.existsByBookedDateAndBookedHourAndCourt(bookedDate, bookedHour, court);
    }
    
    public Booking createBooking(Long court, Long userId, Integer bookedHour, LocalDate bookedDate) {
        if (!isTimeSlotAvailable(bookedDate, bookedHour, court)) {
            throw new RuntimeException("Time slot is already booked");
        }
        
        Booking booking = new Booking(court, userId, bookedHour, bookedDate);
        return bookingRepository.save(booking);
    }
    
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    public Booking updateBooking(Long id, Long court, Integer bookedHour, LocalDate bookedDate) {
        Optional<Booking> existingBooking = bookingRepository.findById(id);
        if (existingBooking.isPresent()) {
            Booking booking = existingBooking.get();
            
            // Check if new time slot is available (if changed)
            if (!booking.getCourt().equals(court) || 
                !booking.getBookedHour().equals(bookedHour) || 
                !booking.getBookedDate().equals(bookedDate)) {
                
                if (!isTimeSlotAvailable(bookedDate, bookedHour, court)) {
                    throw new RuntimeException("Time slot is already booked");
                }
            }
            
            booking.setCourt(court);
            booking.setBookedHour(bookedHour);
            booking.setBookedDate(bookedDate);
            booking.setUpdatedAt(OffsetDateTime.now());
            
            return bookingRepository.save(booking);
        }
        throw new RuntimeException("Booking not found with id: " + id);
    }
    
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
    
    public void cancelBooking(Long id, Long userId) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            if (!booking.get().getUserId().equals(userId)) {
                throw new RuntimeException("User is not authorized to cancel this booking");
            }
            bookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Booking not found with id: " + id);
        }
    }
    
    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }
    
    public long count() {
        return bookingRepository.count();
    }
    
    public long countByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).size();
    }
    
    public long countByCourt(Long court) {
        return bookingRepository.findByCourt(court).size();
    }
}