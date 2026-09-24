package com.healthcare.dto;

import jakarta.validation.constraints.NotBlank;

public class MedicineDto {

    @NotBlank(message = "Medicine name is required")
    private String medicineName;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Duration is required")
    private String duration;

    public MedicineDto() {
    }

    public MedicineDto(String medicineName, String dosage, String duration) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.duration = duration;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
