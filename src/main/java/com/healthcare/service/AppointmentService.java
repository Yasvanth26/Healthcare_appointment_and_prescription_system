package com.healthcare.service;

import com.healthcare.entity.Appointment;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.exception.DuplicateAppointmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create a new appointment with Layer 1 duplicate check
    public Appointment createAppointment(Appointment appointment) {
        // Layer 1: Application-level check
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null 
                && appointment.getAppointmentDate() != null && appointment.getTimeSlot() != null) {
            
            boolean isBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlot(
                    appointment.getDoctor().getId(),
                    appointment.getAppointmentDate(),
                    appointment.getTimeSlot()
            );

            if (isBooked) {
                throw new DuplicateAppointmentException(
                    "Time slot is already booked for this doctor (Application Check)."
                );
            }
        }

        // Set an initial default status
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get all appointments for a specific doctor
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Get all appointments for a specific patient
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    // Get a specific appointment by its ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    // Cancel an appointment
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus("CANCELLED");
            appointmentRepository.save(appointment);
        }
    }

    // Update an appointment's status
    public Appointment updateAppointmentStatus(Long appointmentId, String newStatus) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment != null) {
            appointment.setStatus(newStatus);
            return appointmentRepository.save(appointment);
        }
        return null;
    }

    // Check whether a slot is already booked for a specific doctor
    public boolean isSlotBooked(Long doctorId, LocalDate date, String timeSlot) {
        return appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlot(doctorId, date, timeSlot);
    }
}
