package com.example.classroombooking.repository;
import com.example.classroombooking.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByDayOfWeek(String dayOfWeek);
    boolean existsByDayOfWeekAndTimeSlotAndRoomNumber(String d, String t, String r);
    Optional<Timetable> findByDayOfWeekAndTimeSlotAndRoomNumber(String d, String t, String r);
}