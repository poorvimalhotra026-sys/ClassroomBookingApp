package com.example.classroombooking.service;

import com.example.classroombooking.model.BookingRequest;
import com.example.classroombooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // 1. Committee submits a request
    public BookingRequest createRequest(BookingRequest request) {
        request.setStatus("PENDING_TEACHER");
        return bookingRepository.save(request);
    }

    // 2. Teacher approves and passes to Admin
    public void teacherApprove(Long requestId) {
        BookingRequest request = bookingRepository.findById(requestId).orElseThrow();
        request.setStatus("PENDING_ADMIN");
        bookingRepository.save(request);
    }

    // 3. Admin allots a room and finalizes
    public void adminFinalApprove(Long requestId, String roomNumber) {
        BookingRequest request = bookingRepository.findById(requestId).orElseThrow();
        request.setAllottedRoomNumber(roomNumber);
        request.setStatus("APPROVED");
        bookingRepository.save(request);
    }

    // 4. Get the "One-by-One" queue for the Admin
    public List<BookingRequest> getPendingForAdmin() {
        return bookingRepository.findByStatus("PENDING_ADMIN");
    }
}