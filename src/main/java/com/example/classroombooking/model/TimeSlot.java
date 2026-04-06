package com.example.classroombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "slot_day")
    private String day;
    private String timeRange;  // e.g., "08:00 - 09:00"
    private String roomNumber; // e.g., "401"
    private String roomType;   // CR or CL
    private String occupant;   // e.g., "B.Tech Div B"
    private String status;     // OCCUPIED or VACANT
}