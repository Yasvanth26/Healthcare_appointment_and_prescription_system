package com.healthcare.controller;

import com.healthcare.entity.Appointment;
import com.healthcare.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) {
        Appointment newAppointment = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
        String newStatus = requestBody.get("status");
        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, newStatus);

        if (updatedAppointment != null) {
            return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}
