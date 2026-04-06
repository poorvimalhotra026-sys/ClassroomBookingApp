package com.example.classroombooking.controller;

import com.example.classroombooking.model.User;
import com.example.classroombooking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @Autowired private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getRole())) return "redirect:/bookings/admin/dashboard";
            if ("TEACHER_INCHARGE".equals(user.getRole())) return "redirect:/bookings/teacher/dashboard";
            return "redirect:/bookings/committee/dashboard";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
}