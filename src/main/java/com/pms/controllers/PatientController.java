package com.pms.controllers;

import com.pms.models.Patient;
import com.pms.response.ResponseObject;
import com.pms.services.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/patient")
public class PatientController {
    private final IPatientService patientService;

    @PostMapping
    public ResponseObject save(@RequestBody Patient patient) {
        return patientService.save(patient);
    }

    @DeleteMapping("/{id}")
    public ResponseObject delete(@PathVariable("id") String id) {
        return patientService.delete(id);
    }

    @PatchMapping()
    public ResponseObject update(@RequestBody Patient patient) {
        return patientService.update(patient);
    }

    @GetMapping()
    public ResponseObject getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public ResponseObject getDetails(@PathVariable("id") String id) {
        return patientService.getDetails(id);
    }
}
