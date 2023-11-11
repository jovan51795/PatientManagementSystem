package com.pms.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.models.Patient;
import com.pms.models.PatientFiles;
import com.pms.models.PatientRecord;
import com.pms.repo.PatientRecordRepo;
import com.pms.repo.PatientRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.Deflater;

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
            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, "New record has been added", null);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject addPatientRecord(String id, PatientRecord record, List<MultipartFile> files) {
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

            newPatientRecord.setFile(setPatientFiles(files));
            patient.get().getPatientRecords().add(newPatientRecord);

            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, "New record has been added", null);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<PatientFiles> setPatientFiles(List<MultipartFile> file) throws IOException {
        List<PatientFiles> files = new ArrayList<>();
        for (var fileData : file) {
            PatientFiles patientFile = new PatientFiles();
            patientFile.setFilename(fileData.getOriginalFilename());
            patientFile.setFile(compressImage(fileData.getBytes()));
            files.add(patientFile);
        }

        return files;
    }

    private byte[] compressImage(byte[] data) {

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return outputStream.toByteArray();
    }



}
