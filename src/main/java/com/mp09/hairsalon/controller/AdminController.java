package com.mp09.hairsalon.controller;

import com.mp09.hairsalon.model.Admin;
import com.mp09.hairsalon.model.Appointment;
import com.mp09.hairsalon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.mp09.hairsalon.repository.AdminRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    // Admin dashboard
    @GetMapping
    public String adminPanel(Model model) {
        List<Appointment> appointments = appointmentRepository.findAll();

        long pending = appointments.stream()
                .filter(a -> "PENDING".equals(a.getStatus())).count();
        long confirmed = appointments.stream()
                .filter(a -> "CONFIRMED".equals(a.getStatus())).count();
        long cancelled = appointments.stream()
                .filter(a -> "CANCELLED".equals(a.getStatus())).count();

        model.addAttribute("appointments", appointments);
        model.addAttribute("pendingCount", pending);
        model.addAttribute("confirmedCount", confirmed);
        model.addAttribute("cancelledCount", cancelled);

        return "admin";
    }

    // Status update karo
    @PostMapping("/update-status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam String status) {
        Appointment apt = appointmentRepository
                .findById(id).orElse(null);
        if (apt != null) {
            apt.setStatus(status);
            appointmentRepository.save(apt);
        }
        return "redirect:/admin";
    }

    // Password change page
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    // Password change karo
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            org.springframework.security.core.Authentication authentication,
            org.springframework.ui.Model model) {

        String username = authentication.getName();
        Admin admin = adminRepository.findByUsername(username).orElse(null);

        // Current password check karo
        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            model.addAttribute("error", "Current password is incorrect!");
            return "change-password";
        }

        // New password match check karo
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match!");
            return "change-password";
        }

        // Save karo
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
        model.addAttribute("success", "Password changed successfully!");
        return "change-password";
    }

    // Add admin page
    @GetMapping("/add-admin")
    public String addAdminPage() {
        return "add-admin";
    }

    // Admin save karo
    @PostMapping("/add-admin")
    public String addAdmin(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        // Check karo username already exist toh nahi karta
        if (adminRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "add-admin";
        }

        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setRole("ADMIN");
        adminRepository.save(newAdmin);

        model.addAttribute("success", "Admin added successfully!");
        return "add-admin";
    }
}