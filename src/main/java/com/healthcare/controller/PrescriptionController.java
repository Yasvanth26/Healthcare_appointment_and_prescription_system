package com.healthcare.controller;

import com.healthcare.dto.PrescriptionRequestDto;
import com.healthcare.entity.Prescription;
import com.healthcare.entity.PrescriptionMedicine;
import com.healthcare.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    // Single Transaction Endpoint: Creates prescription, saves medicines, updates appointment to COMPLETED
    @PostMapping("/complete")
    public ResponseEntity<Prescription> completeAppointmentWithPrescription(@Valid @RequestBody PrescriptionRequestDto dto) {
        Prescription savedPrescription = prescriptionService.completeAppointmentWithPrescription(dto);
        return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        return new ResponseEntity<>(prescriptionService.createPrescription(prescription), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        Prescription prescription = prescriptionService.getPrescriptionById(id);
        if (prescription != null) {
            return new ResponseEntity<>(prescription, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/medicines")
    public ResponseEntity<PrescriptionMedicine> addMedicineToPrescription(
            @PathVariable Long id,
            @RequestBody PrescriptionMedicine medicine) {
        
        PrescriptionMedicine savedMedicine = prescriptionService.addMedicineToPrescription(id, medicine);
        if (savedMedicine != null) {
            return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
