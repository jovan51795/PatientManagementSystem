package com.pms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.models.PatientRecord;
import com.pms.response.ResponseObject;
import com.pms.services.IPatientRecordService;
import com.pms.services.PatientRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/record")
public class PatientRecordController {
    private final IPatientRecordService recordService;
    private final ObjectMapper mapper;

    @PatchMapping("/{id}")
    public ResponseObject addPatientRecord(@PathVariable("id") String id, @RequestBody PatientRecord record) {
        return recordService.addPatientRecord(id, record);
    }

    @PatchMapping(produces = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseObject addPatientRecord(@RequestParam("id") String id, @RequestParam("record") String record, @RequestParam("file") List<MultipartFile> files) throws JsonProcessingException {
        PatientRecord recordData = mapper.readValue(record, PatientRecord.class);
        return recordService.addPatientRecord(id, recordData, files);
    }
}
