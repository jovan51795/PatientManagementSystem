package com.pms.controllers;

import com.pms.dto.DoctorDto;
import com.pms.response.ResponseObject;
import com.pms.services.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/doctor")
public class DoctorController {
    private final IDoctorService doctorService;
    @PostMapping()
    public ResponseObject save(@RequestBody DoctorDto doctorDto) {
        return  doctorService.save(doctorDto);
    }
}
