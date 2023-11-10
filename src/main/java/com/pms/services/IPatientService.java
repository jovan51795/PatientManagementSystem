package com.pms.services;

import com.pms.models.Patient;
import com.pms.response.ResponseObject;
import org.springframework.stereotype.Component;

@Component
public interface IPatientService {
    public ResponseObject save(Patient patient);
    public ResponseObject delete(String id);
    public ResponseObject update(Patient patient);
    public ResponseObject getAllPatients();
    public ResponseObject getDetails(String id);

}
