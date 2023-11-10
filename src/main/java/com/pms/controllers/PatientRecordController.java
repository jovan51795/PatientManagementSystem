package com.pms.controllers;

import com.pms.models.PatientRecord;
import com.pms.response.ResponseObject;
import com.pms.services.PatientRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/record")
public class PatientRecordController {
    private final PatientRecordService recordService;

    @PatchMapping()
    public ResponseObject addPatientRecord(@PathVariable("id") String id, @RequestBody PatientRecord record) {
        return recordService.addPatientRecord(id, record);
    }
}
