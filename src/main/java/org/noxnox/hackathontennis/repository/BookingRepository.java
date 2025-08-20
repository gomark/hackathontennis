package org.noxnox.hackathontennis.repository;

import org.noxnox.hackathontennis.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByCourt(Long court);
    
    List<Booking> findByBookedDate(LocalDate bookedDate);
    
    List<Booking> findByBookedDateAndCourt(LocalDate bookedDate, Long court);
    
    Optional<Booking> findByBookedDateAndBookedHourAndCourt(LocalDate bookedDate, Integer bookedHour, Long court);
    
    List<Booking> findByUserIdAndBookedDate(Long userId, LocalDate bookedDate);
    
    @Query("SELECT b FROM Booking b WHERE b.bookedDate >= :startDate AND b.bookedDate <= :endDate")
    List<Booking> findByBookedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.court = :court AND b.bookedDate >= :startDate AND b.bookedDate <= :endDate")
    List<Booking> findByCourtAndBookedDateBetween(@Param("court") Long court, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.userId = :userId AND b.bookedDate >= :startDate AND b.bookedDate <= :endDate ORDER BY b.bookedDate ASC, b.bookedHour ASC")
    List<Booking> findByUserIdAndBookedDateBetweenOrderByDateAndHour(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT DISTINCT b.bookedHour FROM Booking b WHERE b.bookedDate = :date AND b.court = :court ORDER BY b.bookedHour ASC")
    List<Integer> findBookedHoursByDateAndCourt(@Param("date") LocalDate date, @Param("court") Long court);
    
    boolean existsByBookedDateAndBookedHourAndCourt(LocalDate bookedDate, Integer bookedHour, Long court);
}