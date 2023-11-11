package com.pms.services;

import com.pms.models.Patient;
import com.pms.models.PatientFiles;
import com.pms.repo.PatientRecordRepo;
import com.pms.repo.PatientRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static com.pms.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements IPatientService{
    private final PatientRepo patientRepo;
    private final PatientRecordRepo recordRepo;

    @Override
    public ResponseObject save(Patient patient, List<MultipartFile> file) {
        try {
            patient.getPatientRecords().get(0).setFile(setPatientFiles(file));
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
    public ResponseObject update(Patient p, List<MultipartFile> file) {
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
            //patient.get().setPatientRecords(p.getPatientRecords());
            //patient.get().getPatientRecords().get(0).setFile(setPatientFiles(file));
            p.getPatientRecords().get(0).setFile(setPatientFiles(file));
            recordRepo.save(p.getPatientRecords().get(0));
            patientRepo.save(patient.get());
            return new ResponseObject(SUCCESS_STATUS, UPDATE_MSG, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject getAllPatients() {
        try {
            Optional<List<Patient>> patient = patientRepo.findActivePatient();
            patient.get().forEach( p -> {
                p.getPatientRecords().forEach( f-> {
                    f.getFile().forEach(file -> {
                        file.setFile(decompressImage(file.getFile()));
                    });
                });
            });
            return new ResponseObject(SUCCESS_STATUS, null, patient.get());
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject getDetails(String id) {
        try {
            Optional<Patient> patient = patientRepo.findById(id);
            if(patient.isEmpty()) {
                return new ResponseObject(ERROR_STATUS, PATIENT_NOT_FOUND_MSG, null);
            }
            patient.get().getPatientRecords().forEach( p-> {
                p.getFile().forEach(f -> {
                    var fileDe = decompressImage(f.getFile());
                    f.setFile(fileDe);
                });
            });

            return new ResponseObject(SUCCESS_STATUS, null, patient.get());
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
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

    private byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception exception) {
        }
        return outputStream.toByteArray();
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
        }
        return outputStream.toByteArray();
    }

}
