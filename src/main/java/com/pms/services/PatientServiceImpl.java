package com.pms.services;

import com.pms.models.Patient;
import com.pms.repo.PatientRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pms.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements IPatientService{
    private final PatientRepo patientRepo;
    @Override
    public ResponseObject save(Patient patient) {
        try {
            patientRepo.save(patient);
            return new ResponseObject(SUCCESS_STATUS, SAVE_SUCCESSFUL, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject delete(String id) {
        try {
            Optional<Patient> patient = patientRepo.findById(id);
            if(patient.isEmpty()) {
                return new ResponseObject(ERROR_STATUS, PATIENT_NOT_FOUND_MSG, null);
            }
            patient.get().setActive(false);
            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, DELETE_MSG, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject update(Patient p) {
        try {
            Optional<Patient> patient = patientRepo.findById(p.getId());
            if(patient.isEmpty()) {
                return new ResponseObject(ERROR_STATUS, PATIENT_NOT_FOUND_MSG, null);
            }
            patient.get().setFirst_name(p.getFirst_name());
            patient.get().setMiddle_name(p.getMiddle_name());
            patient.get().setLast_name(p.getLast_name());
            patient.get().setBirthday(p.getBirthday());
            patient.get().setGender(p.getGender());
            patient.get().setPlace_of_birth(p.getPlace_of_birth());
            patient.get().setContact(p.getContact());
            patient.get().setStatus(p.getStatus());
            patient.get().setEmergency_contact(p.getEmergency_contact());
            patient.get().setPatientRecords(p.getPatientRecords());
            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, DELETE_MSG, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject getAllPatients() {
        try {
            return new ResponseObject(SUCCESS_STATUS, null, patientRepo.findAll());
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
