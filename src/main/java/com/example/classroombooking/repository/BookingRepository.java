package com.example.classroombooking.repository;
import com.example.classroombooking.model.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByTeacherEmail(String email);
    List<BookingRequest> findByStatus(String status);
}