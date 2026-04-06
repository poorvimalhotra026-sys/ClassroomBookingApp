package com.example.classroombooking.repository;

import com.example.classroombooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}