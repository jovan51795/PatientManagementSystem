package com.pms.services;

import com.pms.dto.DoctorDto;
import com.pms.response.ResponseObject;
import org.springframework.stereotype.Component;

@Component
public interface IDoctorService {
    public ResponseObject save(DoctorDto  doctorDto);
    public ResponseObject getAllDoctor();
    public ResponseObject delete(String id);
    public ResponseObject update(DoctorDto doctorDto);
}
