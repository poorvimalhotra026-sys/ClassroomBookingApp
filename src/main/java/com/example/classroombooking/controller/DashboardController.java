package com.example.classroombooking.controller;

import com.example.classroombooking.model.*;
import com.example.classroombooking.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Controller
public class DashboardController {
    @Autowired private BookingRepository bookingRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private TimetableRepository timetableRepo;

    @GetMapping("/bookings/committee/dashboard")
    public String committeeDash(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        model.addAttribute("teachers", userRepo.findByRole("TEACHER_INCHARGE"));
        // Show ONLY requests from this specific committee
        model.addAttribute("requests", bookingRepo.findAll());
        return "committee-dashboard";
    }
    // UPDATE THIS METHOD IN YOUR DashboardController.java
    @GetMapping("/bookings/teacher/dashboard")
    public String teacherDash(@RequestParam(required = false) String viewDate, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("requests", bookingRepo.findByTeacherEmail(user.getEmail()));

        // --- SHARED MATRIX LOGIC (COPY FROM ADMIN DASH) ---
        String day = (viewDate == null || viewDate.isEmpty()) ?
                LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) :
                LocalDate.parse(viewDate).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        List<String> times = Arrays.asList("8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-1:00", "1:00-2:00", "2:00-3:00", "3:00-4:00", "4:00-5:00", "5:00-6:00");
        List<String> rooms = Arrays.asList("401","402","403","404","501","502","503","504","505","601","603","605","Mini Audi","Seminar Hall","AC Canteen");

        List<Timetable> dayData = timetableRepo.findByDayOfWeek(day);
        Map<String, Map<String, String>> matrix = new LinkedHashMap<>();

        for (String r : rooms) {
            Map<String, String> row = new LinkedHashMap<>();
            for (String t : times) {
                boolean occ = dayData.stream().anyMatch(e -> e.getRoomNumber().equals(r) && e.getTimeSlot().equals(t));
                row.put(t, occ ? "Yes" : "No");
            }
            matrix.put(r, row);
        }

        model.addAttribute("times", times);
        model.addAttribute("matrix", matrix);
        model.addAttribute("selectedDay", day);
        model.addAttribute("selectedDate", viewDate != null ? viewDate : LocalDate.now().toString());
// Committee: Only show requests they personally sent
        model.addAttribute("requests", bookingRepo.findAll().stream()
                .filter(r -> r.getRequesterEmail().equals(user.getEmail()))
                .toList());

// Teacher: Show requests they sent (Adjustments) OR oversee (Committee Approvals)
        model.addAttribute("requests", bookingRepo.findAll().stream()
                .filter(r -> r.getRequesterEmail().equals(user.getEmail()) || r.getTeacherEmail().equals(user.getEmail()))
                .toList());
        return "teacher-dashboard";
    }
    // Inside DashboardController.java
    @GetMapping("/bookings/admin/dashboard")
    public String adminDash(@RequestParam(required = false) String viewDate,
                            @RequestParam(required = false) String filter,
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        // 1. ACTIVE QUEUE
        model.addAttribute("queue", bookingRepo.findByStatus("PENDING_ADMIN"));

        // 2. REFINED HISTORY FILTERING
        // We fetch everything that is NOT pending
        List<BookingRequest> allHistory = bookingRepo.findAll().stream()
                .filter(r -> r.getStatus() != null &&
                        (r.getStatus().equals("APPROVED") ||
                                r.getStatus().equals("REJECTED") ||
                                r.getStatus().equals("CANCELLED")))
                .collect(java.util.stream.Collectors.toList());

        LocalDate now = LocalDate.now();
        List<BookingRequest> filteredHistory;

        // Apply the filters strictly
        if ("week".equals(filter)) {
            filteredHistory = allHistory.stream()
                    .filter(r -> r.getSubmissionDate() != null && !r.getSubmissionDate().isBefore(now.minusWeeks(1)))
                    .toList();
        } else if ("month".equals(filter)) {
            filteredHistory = allHistory.stream()
                    .filter(r -> r.getSubmissionDate() != null && !r.getSubmissionDate().isBefore(now.minusMonths(1)))
                    .toList();
        } else if ("teacher".equals(filter)) {
            filteredHistory = allHistory.stream()
                    .filter(r -> "LECTURE_ADJUSTMENT".equals(r.getRequestType()))
                    .toList();
        } else if ("committee".equals(filter)) {
            filteredHistory = allHistory.stream()
                    .filter(r -> "COMMITTEE".equals(r.getRequestType()))
                    .toList();
        } else {
            filteredHistory = allHistory; // Default to "All"
        }

        model.addAttribute("history", filteredHistory);
        model.addAttribute("currentFilter", filter); // To highlight the active filter in UI

        // 3. MATRIX LOGIC (Keep exactly as before)
        String day = (viewDate == null || viewDate.isEmpty()) ?
                LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) :
                LocalDate.parse(viewDate).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        List<String> times = Arrays.asList("8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-1:00", "1:00-2:00", "2:00-3:00", "3:00-4:00", "4:00-5:00", "5:00-6:00");
        List<String> rooms = Arrays.asList("401","402","403","404","501","502","503","504","505","601","603","605","Mini Audi","Seminar Hall","AC Canteen");

        List<Timetable> dayData = timetableRepo.findByDayOfWeek(day);
        Map<String, Map<String, String>> matrix = new LinkedHashMap<>();
        for (String r : rooms) {
            Map<String, String> row = new LinkedHashMap<>();
            for (String t : times) {
                boolean occ = dayData.stream().anyMatch(e -> e.getRoomNumber().equals(r) && e.getTimeSlot().equals(t));
                row.put(t, occ ? "Yes" : "No");
            }
            matrix.put(r, row);
        }
        model.addAttribute("times", times);
        model.addAttribute("matrix", matrix);
        model.addAttribute("selectedDay", day);
        model.addAttribute("selectedDate", viewDate != null ? viewDate : LocalDate.now().toString());

        return "admin-dashboard";
    }
}