package com.example.classroombooking.config;

import com.example.classroombooking.model.*;
import com.example.classroombooking.repository.*;
import com.example.classroombooking.service.CsvService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DataInitializer {
    @Autowired private CsvService csvService;

    @Bean
    public CommandLineRunner initData(UserRepository uRepo, TimetableRepository tRepo) {
        return args -> {
            if (uRepo.count() == 0) {
                // TEACHERS: Jash, Shailja, Ishani
                String[][] ts = {{"Prof. Jash Mehta","jash.mehta@nmims.in"},{"Prof. Shailja","shailja@nmims.in"},{"Prof. Ishani Saha","ishani.saha@nmims.in"}};
                for(String[] t : ts){
                    User u = new User(); u.setName(t[0]); u.setEmail(t[1]); u.setPassword("teacher123"); u.setRole("TEACHER_INCHARGE"); uRepo.save(u);
                }
                // 7 COMMITTEES
                String[][] cs = {
                        {"Technical Research Cell", "technical.research.cell@nmims.in"},
                        {"Cultural Committee", "cultural.committee@nmims.in"},
                        {"Social Impact", "social.impact@nmims.in"},
                        {"Editorial Board", "editorial.board@nmims.in"},
                        {"Colloquium", "colloquium@nmims.in"},
                        {"Sports Committee", "sports.committee@nmims.in"},
                        {"Student Council MPSTME", "student.council.mpstme@nmims.in"}
                };
                for(String[] c : cs){
                    User u = new User(); u.setName(c[0]); u.setEmail(c[1]); u.setPassword("comm123"); u.setRole("COMMITTEE"); uRepo.save(u);
                }
                User a = new User(); a.setName("Admin"); a.setEmail("admin@nmims.in"); a.setPassword("admin123"); a.setRole("ADMIN"); uRepo.save(a);
            }
            // Load CSV Timetables and seed Venues
            if (tRepo.count() == 0) {
                csvService.loadAllTimetables();
            }
        };
    }
}