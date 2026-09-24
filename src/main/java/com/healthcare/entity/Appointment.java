package com.healthcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(
    name = "appointment",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_doctor_date_slot",
            columnNames = {"doctor_id", "appointment_date", "time_slot"}
        )
    }
)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor is required")
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @NotNull(message = "Patient is required")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull(message = "Appointment date is required")
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @NotBlank(message = "Time slot is required")
    @Column(name = "time_slot")
    private String timeSlot;
    
    private String status; // e.g., PENDING, CONFIRMED, COMPLETED, CANCELLED

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("appointment")
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Prescription prescription;

    public Appointment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
