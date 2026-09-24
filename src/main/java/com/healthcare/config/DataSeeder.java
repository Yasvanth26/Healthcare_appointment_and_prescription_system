package com.healthcare.config;

import com.healthcare.dto.MedicineDto;
import com.healthcare.dto.PrescriptionRequestDto;
import com.healthcare.entity.*;
import com.healthcare.repository.*;
import com.healthcare.service.AppointmentService;
import com.healthcare.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Override
    public void run(String... args) throws Exception {
        if (doctorRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        // Create Admin
        User adminUser = new User();
        adminUser.setName("System Admin");
        adminUser.setEmail("admin@healthcare.com");
        adminUser.setPassword("admin123");
        adminUser.setRole("ADMIN");
        userRepository.save(adminUser);

        // Create 5 Doctors
        String[][] doctorData = {
            {"Dr. Alice Smith", "alice.smith@hospital.com", "Cardiologist"},
            {"Dr. Bob Jones", "bob.jones@hospital.com", "Neurologist"},
            {"Dr. Carol White", "carol.white@hospital.com", "Pediatrician"},
            {"Dr. David Brown", "david.brown@hospital.com", "Dermatologist"},
            {"Dr. Eve Davis", "eve.davis@hospital.com", "General Physician"}
        };

        for (String[] data : doctorData) {
            User user = new User();
            user.setName(data[0]);
            user.setEmail(data[1]);
            user.setPassword("doc123");
            user.setRole("DOCTOR");
            
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setSpecialization(data[2]);
            doctorRepository.save(doctor);
        }

        // Create 8 Patients
        String[][] patientData = {
            {"Frank Miller", "frank.m@gmail.com", "555-0101"},
            {"Grace Wilson", "grace.w@gmail.com", "555-0102"},
            {"Harry Moore", "harry.m@gmail.com", "555-0103"},
            {"Ivy Taylor", "ivy.t@gmail.com", "555-0104"},
            {"Jack Anderson", "jack.a@gmail.com", "555-0105"},
            {"Karen Thomas", "karen.t@gmail.com", "555-0106"},
            {"Leo Jackson", "leo.j@gmail.com", "555-0107"},
            {"Mia White", "mia.w@gmail.com", "555-0108"}
        };

        for (String[] data : patientData) {
            User user = new User();
            user.setName(data[0]);
            user.setEmail(data[1]);
            user.setPassword("pat123");
            user.setRole("PATIENT");
            
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setContactNumber(data[2]);
            patientRepository.save(patient);
        }

        // Seed Appointments
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();

        if (doctors.isEmpty() || patients.isEmpty()) return;

        LocalDate today = LocalDate.now();

        // Appointment 1: PENDING
        Appointment app1 = new Appointment();
        app1.setDoctor(doctors.get(0));
        app1.setPatient(patients.get(0));
        app1.setAppointmentDate(today.plusDays(1));
        app1.setTimeSlot("09:00 AM");
        appointmentService.createAppointment(app1);

        // Appointment 2: CONFIRMED
        Appointment app2 = new Appointment();
        app2.setDoctor(doctors.get(1));
        app2.setPatient(patients.get(1));
        app2.setAppointmentDate(today.plusDays(2));
        app2.setTimeSlot("10:00 AM");
        app2 = appointmentService.createAppointment(app2);
        appointmentService.updateAppointmentStatus(app2.getId(), "CONFIRMED");

        // Appointment 3: COMPLETED WITH PRESCRIPTION
        Appointment app3 = new Appointment();
        app3.setDoctor(doctors.get(2));
        app3.setPatient(patients.get(2));
        app3.setAppointmentDate(today.minusDays(1));
        app3.setTimeSlot("11:00 AM");
        app3 = appointmentService.createAppointment(app3);
        
        PrescriptionRequestDto dto1 = new PrescriptionRequestDto();
        dto1.setAppointmentId(app3.getId());
        dto1.setDiagnosis("Viral Fever");
        dto1.setInstructions("Rest for 3 days and drink plenty of fluids.");
        dto1.setMedicines(Arrays.asList(
            new MedicineDto("Paracetamol", "500mg twice daily", "3 Days"),
            new MedicineDto("Vitamin C", "1 tablet daily", "5 Days")
        ));
        prescriptionService.completeAppointmentWithPrescription(dto1);

        // Appointment 4: COMPLETED WITH PRESCRIPTION
        Appointment app4 = new Appointment();
        app4.setDoctor(doctors.get(0));
        app4.setPatient(patients.get(3));
        app4.setAppointmentDate(today.minusDays(2));
        app4.setTimeSlot("02:00 PM");
        app4 = appointmentService.createAppointment(app4);

        PrescriptionRequestDto dto2 = new PrescriptionRequestDto();
        dto2.setAppointmentId(app4.getId());
        dto2.setDiagnosis("Mild Hypertension");
        dto2.setInstructions("Reduce salt intake, exercise daily.");
        dto2.setMedicines(Arrays.asList(
            new MedicineDto("Lisinopril", "10mg once daily", "30 Days")
        ));
        prescriptionService.completeAppointmentWithPrescription(dto2);

        System.out.println("✅ Database seeded with 1 Admin, 5 Doctors, 8 Patients, and 4 Appointments.");
    }
}
