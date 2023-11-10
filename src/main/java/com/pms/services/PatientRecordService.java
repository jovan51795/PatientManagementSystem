package com.pms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.models.Patient;
import com.pms.models.PatientRecord;
import com.pms.repo.PatientRecordRepo;
import com.pms.repo.PatientRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pms.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class PatientRecordService implements IPatientRecordService {
    private final PatientRecordRepo recordRepo;
    private final PatientRepo patientRepo;
    private final ObjectMapper mapper;

    @Override
    public ResponseObject addPatientRecord(String id, PatientRecord record) {
        try {
            Optional<Patient> patient = patientRepo.findById(id) ;
            if(patient.isEmpty()) {
                return new ResponseObject(ERROR_STATUS, PATIENT_NOT_FOUND_MSG, null);
            }

            PatientRecord newPatientRecord = new PatientRecord();
            newPatientRecord.setFile(record.getFile());
            newPatientRecord.setDiagnose(record.getDiagnose());
            newPatientRecord.setPrescriptions(record.getPrescriptions());
            newPatientRecord.setNotes(record.getNotes());
            newPatientRecord.setPhysician(record.getPhysician());
            patient.get().getPatientRecords().add(newPatientRecord);
            System.out.println(mapper.writeValueAsString(patient.get()));
            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, "New record has been added", null);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
