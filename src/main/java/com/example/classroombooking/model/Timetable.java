package com.example.classroombooking.model;
import jakarta.persistence.*;

@Entity
public class Timetable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dayOfWeek;
    private String timeSlot;
    private String roomNumber;
    private String subject;
    private boolean externalBooking; // TRUE for approved bookings/adjustments

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String d) { this.dayOfWeek = d; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String t) { this.timeSlot = t; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String r) { this.roomNumber = r; }
    public String getSubject() { return subject; }
    public void setSubject(String s) { this.subject = s; }
    public boolean isExternalBooking() { return externalBooking; }
    public void setExternalBooking(boolean b) { this.externalBooking = b; }
}