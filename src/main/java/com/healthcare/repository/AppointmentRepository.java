package com.healthcare.repository;

import com.healthcare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    
    // Check if a doctor is already booked for a specific date and time slot
    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(Long doctorId, LocalDate appointmentDate, String timeSlot);
}
