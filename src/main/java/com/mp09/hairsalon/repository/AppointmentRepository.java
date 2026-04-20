package com.mp09.hairsalon.repository;

import com.mp09.hairsalon.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    // Ek stylist ke ek date ke booked slots
    List<Appointment> findByStylistAndDate(
            String stylist, LocalDate date
    );
}
