package com.healthcare.service;

import com.healthcare.dto.MedicineDto;
import com.healthcare.dto.PrescriptionRequestDto;
import com.healthcare.entity.Appointment;
import com.healthcare.entity.Prescription;
import com.healthcare.entity.PrescriptionMedicine;
import com.healthcare.repository.AppointmentRepository;
import com.healthcare.repository.PrescriptionMedicineRepository;
import com.healthcare.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionMedicineRepository prescriptionMedicineRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create a basic prescription
    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    // Retrieve a prescription by ID
    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }

    // Add an individual medicine to a prescription
    public PrescriptionMedicine addMedicineToPrescription(Long prescriptionId, PrescriptionMedicine medicine) {
        Prescription prescription = getPrescriptionById(prescriptionId);
        if (prescription != null) {
            medicine.setPrescription(prescription);
            return prescriptionMedicineRepository.save(medicine);
        }
        return null;
    }

    /**
     * Transactional Method:
     * When a doctor completes an appointment:
     * 1. Creates and saves the prescription.
     * 2. Saves all prescribed medicines.
     * 3. Updates the appointment status to "COMPLETED".
     *
     * If ANY of these steps fail, @Transactional ensures all operations roll back.
     */
    @Transactional
    public Prescription completeAppointmentWithPrescription(PrescriptionRequestDto dto) {
        if (dto.getAppointmentId() == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null.");
        }

        // 1. Verify Appointment exists
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + dto.getAppointmentId()));

        // 2. Create and save the Prescription
        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setInstructions(dto.getInstructions());
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // 3. Save Prescription Medicines
        List<PrescriptionMedicine> savedMedicines = new ArrayList<>();
        if (dto.getMedicines() != null && !dto.getMedicines().isEmpty()) {
            for (MedicineDto medDto : dto.getMedicines()) {
                if (medDto.getMedicineName() == null || medDto.getMedicineName().trim().isEmpty()) {
                    throw new IllegalArgumentException("Medicine name cannot be empty.");
                }
                PrescriptionMedicine med = new PrescriptionMedicine();
                med.setPrescription(savedPrescription);
                med.setMedicineName(medDto.getMedicineName());
                med.setDosage(medDto.getDosage());
                med.setDuration(medDto.getDuration());

                savedMedicines.add(prescriptionMedicineRepository.save(med));
            }
            savedPrescription.setMedicines(savedMedicines);
        }

        // 4. Update the Appointment status to COMPLETED
        appointment.setStatus("COMPLETED");
        appointmentRepository.save(appointment);

        return savedPrescription;
    }
}
