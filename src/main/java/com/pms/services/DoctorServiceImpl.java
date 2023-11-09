package com.pms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.dto.DoctorDto;
import com.pms.models.Doctor;
import com.pms.repo.DoctorRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.pms.constants.Constants.SAVE_SUCCESSFUL;
import static com.pms.constants.Constants.SUCCESS_STATUS;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements IDoctorService {

    private final DoctorRepo doctorRepo;
    private final ObjectMapper mapper;
    @Override
    public ResponseObject save(DoctorDto doctorDto) {
        try {
            String doctorStr = mapper.writeValueAsString(doctorDto);
            Doctor doctor = mapper.readValue(doctorStr, Doctor.class);
            doctorRepo.save(doctor);
            return new ResponseObject(SUCCESS_STATUS, SAVE_SUCCESSFUL, null);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e );
        }catch (Exception e) {
            throw new RuntimeException(e );
        }
    }
}
