package com.example.classroombooking.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class BookingRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private String eventDate;
    private String dayOfWeek;
    private String timeSlot;
    private String roomType;
    private String teacherEmail; // The teacher overseeing a committee
    private String requesterEmail; // The person/committee who actually clicked submit
    private LocalDate submissionDate; // For history filtering (Past week/month)
    private String status;
    private String rejectionReason;
    private String allottedRoomNumber;

    // Relationship 2: Lecture Management
    private String requestType; // "COMMITTEE" or "LECTURE_ADJUSTMENT"
    private String batchName;
    private String adjustmentType;
    private String originalLectureDetails;

    // Standard Getters and Setters for all fields...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventName() { return eventName; }
    public void setEventName(String n) { this.eventName = n; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String d) { this.eventDate = d; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String d) { this.dayOfWeek = d; }
    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String t) { this.timeSlot = t; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String r) { this.roomType = r; }
    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String e) { this.teacherEmail = e; }
    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String e) { this.requesterEmail = e; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate d) { this.submissionDate = d; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String r) { this.rejectionReason = r; }
    public String getAllottedRoomNumber() { return allottedRoomNumber; }
    public void setAllottedRoomNumber(String r) { this.allottedRoomNumber = r; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String r) { this.requestType = r; }
    public String getBatchName() { return batchName; }
    public void setBatchName(String b) { this.batchName = b; }
    public String getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(String a) { this.adjustmentType = a; }
    public String getOriginalLectureDetails() { return originalLectureDetails; }
    public void setOriginalLectureDetails(String o) { this.originalLectureDetails = o; }
}