package com.pms.services;

import com.pms.models.PatientRecord;
import com.pms.response.ResponseObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface IPatientRecordService {
    public ResponseObject addPatientRecord(String id, PatientRecord record);
    public ResponseObject addPatientRecord(String id, PatientRecord record, List<MultipartFile> files);
}
