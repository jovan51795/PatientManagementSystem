package com.pms.services;

import com.pms.dto.ChartData;
import com.pms.dto.Dashboard;
import com.pms.dto.DataSets;
import com.pms.models.Patient;
import com.pms.models.PatientFiles;
import com.pms.models.PatientRecord;
import com.pms.repo.PatientRecordRepo;
import com.pms.repo.PatientRepo;
import com.pms.response.ResponseObject;
import com.pms.util.AESEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final SecretKey key;

    @Override
    public ResponseObject save(Patient patient, List<MultipartFile> file) {
        try {
            patient.getPatientRecords().get(0).setFile(setPatientFiles(file));
            encryptPatient(patient);
            patientRepo.save(patient);

            return new ResponseObject(SUCCESS_STATUS, SAVE_SUCCESSFUL, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void encryptPatient(Patient patient) throws Exception {
        patient.setFirst_name(encrypt(patient.getFirst_name()));
        patient.setMiddle_name(encrypt(patient.getMiddle_name()));
        patient.setLast_name(encrypt(patient.getLast_name()));
        patient.setBirthday(encrypt(patient.getBirthday()));
        patient.setGender(patient.getGender());
        patient.setPlace_of_birth(encrypt(patient.getPlace_of_birth()));
        patient.setContact(encrypt(patient.getContact()));
        patient.setEmergency_contact(encrypt(patient.getEmergency_contact()));
    }

    private void decryptPatient(Patient patient) throws Exception {
        patient.setFirst_name(decrypt(patient.getFirst_name()));
        patient.setMiddle_name(decrypt(patient.getMiddle_name()));
        patient.setLast_name(decrypt(patient.getLast_name()));
        patient.setBirthday(decrypt(patient.getBirthday()));
        patient.setGender(patient.getGender());
        patient.setPlace_of_birth(decrypt(patient.getPlace_of_birth()));
        patient.setContact(decrypt(patient.getContact()));
        patient.setEmergency_contact(decrypt(patient.getEmergency_contact()));
    }

    private String encrypt(String value) throws Exception {
        return AESEncryption.encrypt(value, key);
    }

    private String decrypt(String value) throws Exception {
        return AESEncryption.decrypt(value, key);
    }

    public ResponseObject save(Patient patient) {
        try {
            encryptPatient(patient);
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
            encryptPatient(patient.get());
            patientRepo.save(patient.get());
            PatientRecord patientRecord = p.getPatientRecords().get(0);

            if (file != null) {
                //patientRecord.setFile(setPatientFiles(file));
                //patientRecord = patient.get()
                patient.get().getPatientRecords().forEach( pt -> {
                    if(pt.getId().equals(patientRecord.getId())) {
                        patientRecord.setFile(pt.getFile());
                    }
                });
                setPatientFiles(patientRecord.getFile() ,file);
            } else {
                PatientRecord pRec = p.getPatientRecords().get(0);
                patientRecord.setId(pRec.getId());
                patientRecord.setPrescriptions(pRec.getPrescriptions());
                patientRecord.setDiagnose(pRec.getDiagnose());
                patientRecord.setNotes(pRec.getNotes());
                pRec.getFile().forEach(f -> {
                    f.setFile(compressImage(f.getFile()));
                });
                patientRecord.setFile(pRec.getFile());
                patientRecord.setDate(pRec.getDate());

            }

            recordRepo.save(patientRecord);

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
                try {
                    decryptPatient(p);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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

            decryptPatient(patient.get());

            return new ResponseObject(SUCCESS_STATUS, null, patient.get());
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject getPatientStatusReport() {
        try {
            Dashboard dashboard = new Dashboard(
                    patientRepo.getPatientReport(),
                    generateChartData()
            );
            return new ResponseObject(SUCCESS_STATUS, null, dashboard);

        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<String> getMonths() {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy"));
        var result = patientRepo.getChartReport(date);
        List<String> labels = new ArrayList<>();
        result.forEach(c -> {
            labels.add(c.getMonths().substring(8));
        });
        return labels;
    }

    public DataSets getDataSets() {
        var result = patientRepo.getChartReportDataSets();
        List<Integer> data = new ArrayList<>();
        result.forEach(c-> {
            data.add(c.getCount());
        });
        String label = "Monthly patient report";

        //General Datasets
        DataSets dataSets = new DataSets(
                label,
                data,
                false,
                "documentStyle.getPropertyValue('--green-600')",
                "documentStyle.getPropertyValue('--green-600')",
                0.4

        );

        return dataSets;
    }

    public void getDataSetsByGender(List<DataSets> sets) {
        String[] gender = {"Male", "Female", "Other"};
        String[] color = {"--blue-300", "--pink-300", "--purple-400"};
        for ( int i = 0; i < gender.length; i++) {
            var result = patientRepo.getChartReportDataSetsByGender(gender[i]);
            List<Integer> data = new ArrayList<>();
            result.forEach(c-> {
                data.add(c.getCount());
            });
            String label = "";
            if("Male".equals(gender[i])) {
                label = "Male monthly report";
            } else if("Female".equals(gender[i])) {
                label = "Female monthly report";
            } else {
                label = "Non-binary monthly report";
            }
            DataSets dataSets = dataSets = new DataSets(
                    label,
                    data,
                    false,
                    "documentStyle.getPropertyValue("+ "'" + color[i] +"'" +")",
                    "documentStyle.getPropertyValue("+ "'" + color[i] +"'" +")",
                    0.4

            );

            sets.add(dataSets);
        }
    }
    public ChartData generateChartData() {
        List<DataSets> dataSets = new ArrayList<>();
        getDataSetsByGender(dataSets);
        dataSets.add(getDataSets());
//        getDataSetsByGender(dataSets);
        ChartData chartData = new ChartData(
                getMonths(),
                dataSets
        );

        return chartData;
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

    private List<PatientFiles> setPatientFiles(List<PatientFiles> files,List<MultipartFile> file) throws IOException {

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
