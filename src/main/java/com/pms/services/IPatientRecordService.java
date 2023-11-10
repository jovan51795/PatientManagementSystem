package com.pms.services;

import com.pms.models.PatientRecord;
import com.pms.response.ResponseObject;
import org.springframework.stereotype.Component;

@Component
public interface IPatientRecordService {
    public ResponseObject addPatientRecord(String id, PatientRecord record);
}
