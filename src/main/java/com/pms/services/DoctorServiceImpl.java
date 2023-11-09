package com.pms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pms.dto.DoctorDto;
import com.pms.models.Doctor;
import com.pms.repo.DoctorRepo;
import com.pms.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.pms.constants.Constants.*;

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

    @Override
    public ResponseObject getAllDoctor() {
        try {
            Optional<List<Doctor>> doctors = doctorRepo.findAllActive();
            return new ResponseObject(SUCCESS_STATUS, null, doctors.get());
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject delete(String id) {
        try {
            Optional<Doctor> doctor = doctorRepo.findById(id);
            if(doctor.isEmpty()) {
                return new ResponseObject(ERROR_STATUS,USER_NOT_FOUND_MSG,null );
            }

            doctor.get().setActive(false);
            doctorRepo.save(doctor.get());
            return new ResponseObject(SUCCESS_STATUS, DELETE_MSG, null);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResponseObject update(DoctorDto doctorDto) {
        try {
            Optional<Doctor> doctor = doctorRepo.findById(doctorDto.id());
            if(doctor.isEmpty()) {
                return new ResponseObject(ERROR_STATUS,USER_NOT_FOUND_MSG,null );
            }
            doctor.get().setLast_name(doctorDto.first_name());
            doctor.get().setLast_name(doctorDto.last_name());
            doctor.get().setSpecialization(doctorDto.specialization());
            doctorRepo.save(doctor.get());
            return new ResponseObject(SUCCESS_STATUS,UPDATE_MSG, null );
        }catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
    }
}
