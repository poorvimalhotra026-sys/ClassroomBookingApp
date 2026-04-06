package com.example.classroombooking.repository;

import com.example.classroombooking.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // THIS IS THE FIX

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDayAndTimeRangeAndStatus(String day, String time, String status);
}