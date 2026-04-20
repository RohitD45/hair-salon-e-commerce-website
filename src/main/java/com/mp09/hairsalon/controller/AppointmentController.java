package com.mp09.hairsalon.controller;

import com.mp09.hairsalon.model.Appointment;
import com.mp09.hairsalon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Booked slots fetch
    @GetMapping("/booked-slots")
    public List<String> getBookedSlots(
            @RequestParam String stylist,
            @RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);
        List<Appointment> appointments =
                appointmentRepository.findByStylistAndDate(stylist, localDate);

        return appointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toList());
    }

    // Appointment save
    @PostMapping("/book")
    public String bookAppointment(@RequestBody Appointment appointment) {
        appointment.setStatus("PENDING");
        appointmentRepository.save(appointment);
        return "SUCCESS";
    }


}
