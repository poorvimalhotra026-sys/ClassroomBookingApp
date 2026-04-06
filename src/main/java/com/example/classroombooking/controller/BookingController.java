package com.example.classroombooking.controller;

import com.example.classroombooking.model.*;
import com.example.classroombooking.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Controller
public class BookingController {
    @Autowired private BookingRepository bookingRepo;
    @Autowired private TimetableRepository timetableRepo;

    @PostMapping("/bookings/committee/request")
    public String submitRequest(@ModelAttribute BookingRequest req, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LocalDate date = LocalDate.parse(req.getEventDate());
        req.setDayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        req.setRequesterEmail(user.getEmail());
        req.setSubmissionDate(LocalDate.now());
        req.setRequestType("COMMITTEE");
        req.setStatus("PENDING_TEACHER");
        bookingRepo.save(req);
        return "redirect:/bookings/committee/dashboard";
    }

    @PostMapping("/bookings/teacher/lecture-adjustment")
    public String lectureAdjustment(@ModelAttribute BookingRequest req, HttpSession session) {
        User user = (User) session.getAttribute("user");
        LocalDate date = LocalDate.parse(req.getEventDate());
        req.setDayOfWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        req.setTeacherEmail(user.getEmail());
        req.setRequesterEmail(user.getEmail());
        req.setSubmissionDate(LocalDate.now());
        req.setRequestType("LECTURE_ADJUSTMENT");
        req.setStatus("PENDING_ADMIN");
        bookingRepo.save(req);
        return "redirect:/bookings/teacher/dashboard";
    }

    @PostMapping("/bookings/teacher/approve/{id}")
    public String approve(@PathVariable Long id) {
        BookingRequest req = bookingRepo.findById(id).orElseThrow();
        req.setStatus("PENDING_ADMIN");
        bookingRepo.save(req);
        return "redirect:/bookings/teacher/dashboard";
    }

    @PostMapping("/bookings/admin/allot/{id}")
    public String allot(@PathVariable Long id, @RequestParam(required = false) String roomNumber) {
        BookingRequest req = bookingRepo.findById(id).orElseThrow();

        // Remove old lecture if it's an adjustment
        if ("LECTURE_ADJUSTMENT".equals(req.getRequestType()) && req.getOriginalLectureDetails() != null) {
            String[] parts = req.getOriginalLectureDetails().split(" \\| ");
            if (parts.length == 3) {
                timetableRepo.findByDayOfWeekAndTimeSlotAndRoomNumber(parts[0].trim(), parts[1].trim(), parts[2].trim())
                        .ifPresent(t -> timetableRepo.delete(t));
            }
        }

        if ("CANCEL_ONLY".equals(req.getAdjustmentType())) {
            req.setStatus("CANCELLED");
        } else {
            Timetable entry = new Timetable();
            entry.setDayOfWeek(req.getDayOfWeek());
            entry.setTimeSlot(req.getTimeSlot());
            entry.setRoomNumber(roomNumber);
            entry.setSubject(req.getRequestType().equals("COMMITTEE") ? req.getEventName() : "RESCHEDULED: " + req.getBatchName());
            entry.setExternalBooking(true);
            timetableRepo.save(entry);
            req.setAllottedRoomNumber(roomNumber);
            req.setStatus("APPROVED");
        }
        bookingRepo.save(req);
        return "redirect:/bookings/admin/dashboard";
    }
}