package com.pms.services;

import com.pms.models.Patient;
import com.pms.response.ResponseObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface IPatientService {
    public ResponseObject save(Patient patient, List<MultipartFile> file);
    public ResponseObject save(Patient patient);

    public ResponseObject delete(String id);
    public ResponseObject update(Patient patient, List<MultipartFile> file);
    public ResponseObject getAllPatients();
    public ResponseObject getDetails(String id);

    public ResponseObject getPatientStatusReport();


}
