package com.mp09.hairsalon.config;

import com.mp09.hairsalon.model.Admin;
import com.mp09.hairsalon.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Sirf tab insert karo jab koi admin na ho
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("mp09@admin"));
            admin.setRole("ADMIN");
            adminRepository.save(admin);
            System.out.println("✅ Default admin created!");
        }
    }
}