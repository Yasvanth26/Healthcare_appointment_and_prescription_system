package com.healthcare.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("prescription")
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String diagnosis;
    private String instructions;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private List<PrescriptionMedicine> medicines;

    public Prescription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<PrescriptionMedicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<PrescriptionMedicine> medicines) {
        this.medicines = medicines;
    }
}
