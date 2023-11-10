package com.pms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.models.Patient;
import com.pms.response.ResponseObject;
import com.pms.services.IPatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/patient")
public class PatientController {
    private final IPatientService patientService;
    private final ObjectMapper mapper;

    @Transactional
    @PostMapping(produces = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseObject save(@RequestParam("patient") String patientData, @RequestParam("file") List<MultipartFile> file) throws JsonProcessingException {
        Patient patient = mapper.readValue(patientData, Patient.class);
        return patientService.save(patient, file);
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
